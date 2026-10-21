package ma.esith.stockims.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "article")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 20)
	private String reference;

	@Column(nullable = false, length = 80)
	private String designation;

	public Article(String reference, String designation) {
		if (reference == null || reference.isBlank() || reference.length() > 20) {
			throw new IllegalArgumentException("Référence invalide.");
		}
		if (designation == null || designation.isBlank() || designation.length() > 80) {
			throw new IllegalArgumentException("Désignation invalide.");
		}
		this.reference = reference.strip();
		this.designation = designation.strip();
	}
}
