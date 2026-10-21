package ma.esith.stockims.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.esith.stockims.service.ArticleService;
import ma.esith.stockims.service.StockService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {

	private final ArticleService service;
	private final StockService stockService;

	@GetMapping
	public String liste(Model model) {
		var articles = service.lister();
		model.addAttribute("articles", articles);
		model.addAttribute("stocks", stockService.calculerStocks(articles));
		return "articles/liste";
	}

	@GetMapping("/nouveau")
	public String formulaire(Model model) {
		model.addAttribute("article", new ArticleForm());
		return "articles/formulaire";
	}

	@PostMapping
	public String ajouter(@Valid @ModelAttribute("article") ArticleForm form,
			BindingResult erreurs, RedirectAttributes redirect) {
		if (erreurs.hasErrors()) {
			return "articles/formulaire";
		}

		try {
			service.creer(form.getReference(), form.getDesignation());
		} catch (DataIntegrityViolationException e) {
			erreurs.rejectValue("reference", "doublon", "Cette référence existe déjà.");
			return "articles/formulaire";
		}

		redirect.addFlashAttribute("succes", "L'article a été enregistré avec succès.");
		return "redirect:/articles";
	}
}
