package ma.esith.stockims.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

import lombok.RequiredArgsConstructor;
import ma.esith.stockims.model.Article;
import ma.esith.stockims.model.Entree;
import ma.esith.stockims.model.MouvementStock;
import ma.esith.stockims.model.Sortie;
import ma.esith.stockims.repository.ArticleRepository;
import ma.esith.stockims.repository.EntreeRepository;
import ma.esith.stockims.repository.SortieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {

	private final ArticleRepository articles;
	private final EntreeRepository entrees;
	private final SortieRepository sorties;

	@Transactional(readOnly = true)
	public long calculerStock(Long articleId) {
		List<MouvementStock> mouvements = new ArrayList<>();
		mouvements.addAll(entrees.findByArticle_Id(articleId));
		mouvements.addAll(sorties.findByArticle_Id(articleId));
		return mouvements.stream().mapToLong(MouvementStock::quantiteSignee).sum();
	}

	@Transactional(readOnly = true)
	public Optional<Article> chercher(Long id) {
		return articles.findById(id);
	}

	@Transactional(readOnly = true)
	public Map<Long, Long> calculerStocks(Collection<Article> listeArticles) {
		Map<Long, Long> stocks = new HashMap<>();
		for (Object[] total : entrees.totalParArticle()) {
			stocks.put((Long) total[0], ((Number) total[1]).longValue());
		}
		for (Object[] total : sorties.totalParArticle()) {
			stocks.merge((Long) total[0], -((Number) total[1]).longValue(), Long::sum);
		}
		for (Article article : listeArticles) {
			stocks.putIfAbsent(article.getId(), 0L);
		}
		return stocks;
	}

	private Article verrouiller(Long id) {
		return articles.verrouiller(id)
				.orElseThrow(() -> new IllegalArgumentException("Article introuvable."));
	}

	@Transactional
	public void entrer(Long id, int quantite) {
		if (quantite <= 0) {
			throw new IllegalArgumentException("La quantité doit être supérieure ou égale à 1.");
		}
		Article article = verrouiller(id);
		entrees.save(new Entree(article, quantite, LocalDate.now()));
	}

	@Transactional
	public void sortir(Long id, int quantite) {
		if (quantite <= 0) {
			throw new IllegalArgumentException("La quantité doit être supérieure ou égale à 1.");
		}
		Article article = verrouiller(id);
		long stock = calculerStock(id);
		if (quantite > stock) {
			throw new IllegalArgumentException("Stock insuffisant : " + stock + " disponible(s).");
		}
		sorties.save(new Sortie(article, quantite, LocalDate.now()));
	}
}
