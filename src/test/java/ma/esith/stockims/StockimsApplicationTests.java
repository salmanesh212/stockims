package ma.esith.stockims;

import ma.esith.stockims.repository.ArticleRepository;
import ma.esith.stockims.repository.EntreeRepository;
import ma.esith.stockims.repository.SortieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class StockimsApplicationTests {

	@Autowired private MockMvc mvc;
	@Autowired private ArticleRepository articles;
	@Autowired private EntreeRepository entrees;
	@Autowired private SortieRepository sorties;

	@BeforeEach
	void nettoyerBaseDeTest() {
		sorties.deleteAll();
		entrees.deleteAll();
		articles.deleteAll();
	}

	@Test
	void parcoursEntreeSortieEtRefusDuStockInsuffisant() throws Exception {
		long id = creerArticle("TEST-001", "Article de test");

		mvc.perform(get("/stock/{id}", id))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("STOCK DISPONIBLE")))
				.andExpect(content().string(containsString(">0</span>")));

		mouvement("/stock/" + id + "/entrees", 20, true, null);
		mvc.perform(get("/stock/" + id)).andExpect(status().isOk())
				.andExpect(content().string(containsString(">20</span>")));

		mouvement("/stock/" + id + "/sorties", 7, true, null);
		mvc.perform(get("/stock/" + id)).andExpect(status().isOk())
				.andExpect(content().string(containsString(">13</span>")));

		mouvement("/stock/" + id + "/sorties", 15, false, "Stock insuffisant");
		mvc.perform(get("/stock/" + id)).andExpect(status().isOk())
				.andExpect(content().string(containsString(">13</span>")));
	}

	@Test
	void refuseQuantitesNullesEtNegatives() throws Exception {
		long id = creerArticle("TEST-002", "Validation quantité");
		mouvement("/stock/" + id + "/entrees", 0, false, "La quantité doit être supérieure ou égale à 1.");
		mouvement("/stock/" + id + "/entrees", -2, false, "La quantité doit être supérieure ou égale à 1.");
		mvc.perform(get("/stock/" + id)).andExpect(status().isOk())
				.andExpect(content().string(containsString(">0</span>")));
	}

	@Test
	void valideLesChampsEtLaReferenceUnique() throws Exception {
		creerArticle("TEST-003", "Article unique");
		mvc.perform(post("/articles").param("reference", "TEST-003").param("designation", "Doublon"))
				.andExpect(status().isOk()).andExpect(view().name("articles/formulaire"))
				.andExpect(content().string(containsString("Cette référence existe déjà")));
		mvc.perform(post("/articles").param("reference", "").param("designation", ""))
				.andExpect(status().isOk()).andExpect(view().name("articles/formulaire"));
	}

	@Test
	void afficheLaRechercheEtLesPagesSansDonneesDeDemonstration() throws Exception {
		mvc.perform(get("/")).andExpect(status().isOk())
				.andExpect(content().string(containsString("Bienvenue sur")));
		mvc.perform(get("/articles")).andExpect(status().isOk())
				.andExpect(content().string(containsString("Aucun article pour le moment")))
				.andExpect(content().string(containsString("article-search")));
	}

	private long creerArticle(String reference, String designation) throws Exception {
		mvc.perform(post("/articles").param("reference", reference).param("designation", designation))
				.andExpect(status().is3xxRedirection());
		return articles.findAll().stream().filter(a -> a.getReference().equals(reference))
				.findFirst().orElseThrow().getId();
	}

	private void mouvement(String route, int quantite, boolean succes, String erreurAttendue) throws Exception {
		var resultat = mvc.perform(post(route).param("quantite", Integer.toString(quantite)));
		if (succes) {
			resultat.andExpect(status().is3xxRedirection());
		} else {
			resultat.andExpect(status().isOk()).andExpect(view().name("stock/fiche"))
					.andExpect(content().string(containsString(erreurAttendue)));
		}
	}
}
