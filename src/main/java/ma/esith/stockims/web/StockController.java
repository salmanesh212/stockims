package ma.esith.stockims.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.esith.stockims.service.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

	private final StockService service;

	@GetMapping("/{id}")
	public String fiche(@PathVariable Long id, Model model) {
		model.addAttribute("mouvement", new MouvementForm());
		return preparer(id, model);
	}

	@PostMapping("/{id}/entrees")
	public String entree(@PathVariable Long id,
			@Valid @ModelAttribute("mouvement") MouvementForm form,
			BindingResult erreurs, Model model, RedirectAttributes redirect) {
		if (erreurs.hasErrors()) {
			model.addAttribute("mouvementType", "entree");
			return preparer(id, model);
		}
		try {
			service.entrer(id, form.getQuantite());
		} catch (IllegalArgumentException e) {
			model.addAttribute("erreurMetier", e.getMessage());
			model.addAttribute("mouvementType", "entree");
			return preparer(id, model);
		}
		redirect.addFlashAttribute("succes", "L'entrée de stock a été enregistrée.");
		return "redirect:/stock/" + id;
	}

	@PostMapping("/{id}/sorties")
	public String sortie(@PathVariable Long id,
			@Valid @ModelAttribute("mouvement") MouvementForm form,
			BindingResult erreurs, Model model, RedirectAttributes redirect) {
		if (erreurs.hasErrors()) {
			model.addAttribute("mouvementType", "sortie");
			return preparer(id, model);
		}
		try {
			service.sortir(id, form.getQuantite());
		} catch (IllegalArgumentException e) {
			model.addAttribute("erreurMetier", e.getMessage());
			model.addAttribute("mouvementType", "sortie");
			return preparer(id, model);
		}
		redirect.addFlashAttribute("succes", "La sortie de stock a été enregistrée.");
		return "redirect:/stock/" + id;
	}

	private String preparer(Long id, Model model) {
		var article = service.chercher(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		model.addAttribute("article", article);
		model.addAttribute("stock", service.calculerStock(id));
		model.addAttribute("mouvement", model.containsAttribute("mouvement")
				? model.getAttribute("mouvement") : new MouvementForm());
		return "stock/fiche";
	}
}
