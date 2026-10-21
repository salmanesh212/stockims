package ma.esith.stockims.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class MouvementStock {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "article_id", nullable = false)
	private Article article;

	@Column(nullable = false)
	private int quantite;

	@Column(nullable = false)
	private LocalDate dateMouvement;

	protected MouvementStock(Article article, int quantite, LocalDate dateMouvement) {
		if (article == null || dateMouvement == null) {
			throw new IllegalArgumentException("Article et date requis.");
		}
		if (quantite <= 0) {
			throw new IllegalArgumentException("Quantité positive requise.");
		}
		this.article = article;
		this.quantite = quantite;
		this.dateMouvement = dateMouvement;
	}

	public abstract int quantiteSignee();
}
