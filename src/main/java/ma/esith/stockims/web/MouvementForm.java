package ma.esith.stockims.web;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MouvementForm {

	@NotNull(message = "La quantité est obligatoire.")
	@Min(value = 1, message = "La quantité doit être supérieure ou égale à 1.")
	private Integer quantite;
}
