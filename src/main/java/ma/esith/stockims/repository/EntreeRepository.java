package ma.esith.stockims.repository;

import java.util.List;
import ma.esith.stockims.model.Entree;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntreeRepository extends JpaRepository<Entree, Long> {
	List<Entree> findByArticle_Id(Long articleId);

	@org.springframework.data.jpa.repository.Query("select e.article.id, sum(e.quantite) from Entree e group by e.article.id")
	List<Object[]> totalParArticle();
}
