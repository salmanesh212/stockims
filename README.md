# StockIMS

Application de gestion de stock réalisée avec Spring Boot et Thymeleaf.

## Participants

- Mohamed Salmane SHAME
- SIFFEDINE Youssra

## Fonctionnalités

- Référencement des articles avec référence unique et champs validés.
- Calcul du stock réel : somme des entrées moins somme des sorties.
- Enregistrement des mouvements dans MySQL.
- Refus d'une quantité nulle ou négative et d'une sortie supérieure au stock.
- Recherche instantanée des articles.
- Interface française responsive et personnalisée.

## Technologies et prérequis

- Java 21
- Spring Boot 4.1.1, Spring MVC, Spring Data JPA
- Thymeleaf, Jakarta Validation, Lombok
- MySQL 8.x
- Maven Wrapper inclus (`mvnw`, `mvnw.cmd`)

## Configuration MySQL

Créez une base `stock_ims` et un compte ayant les droits sur cette base. Adaptez `src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/stock_ims
spring.datasource.username=stock_app
spring.datasource.password=${STOCKIMS_DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
```

Sous Windows PowerShell, définissez le mot de passe sans le versionner :

```powershell
$env:STOCKIMS_DB_PASSWORD = "votre-mot-de-passe"
```

Le fichier [database/stockims_test_data.sql](database/stockims_test_data.sql) crée la structure et trois articles de test. Il prépare `DEMO-001` avec une entrée de 20 et une sortie de 7, soit un stock final de 13. Le script peut être rejoué sans doublons.

## Lancement

Depuis le dossier contenant `pom.xml` :

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd package
.\mvnw.cmd spring-boot:run
```

L'application est disponible sur <http://localhost:8080/>.

## Personnalisation réalisée

**OBSIDIENNE** est un tableau de bord sombre, calme et précis : le fond noir bleuté réduit la fatigue visuelle, les surfaces en niveaux donnent la profondeur sans glassmorphism et l'émeraude signale les actions positives. Le rose est réservé aux sorties et erreurs, le bleu ciel aux repères informatifs ; ces couleurs restent lisibles sur leurs surfaces dédiées.

- **Palette et composants** : `#0B0D10`, `#12161B`, `#181D24`, `#232A33` et `#E8ECF1`, avec un unique halo émeraude discret. Les cartes ont un rayon de 12 px, les contrôles 8 px, une bordure de 1 px et un filet interne.
- **Typographies** : Inter auto-hébergée dans `static/fonts` pour l'interface, JetBrains Mono pour références, quantités et KPI avec chiffres tabulaires. Les fallbacks système garantissent un rendu même si une police est indisponible.
- **HTML/Thymeleaf** : navigation limitée à Accueil / Articles / Nouvel article, icônes SVG Lucide dans `fragments/icons.html`, KPI calculées dans la vue depuis `stocks`, tableau transformé en cartes via `data-label` sous 560 px. Les liaisons Thymeleaf existantes sont conservées.
- **CSS** : focus-visible en anneau émeraude, états hover/disabled/erreur/succès, grille bento d'accueil et responsive vérifié pour 360, 768 et 1440 px. `prefers-reduced-motion` désactive les transitions et le comptage animé.
- **JavaScript progressif** : recherche, raccourci `/`, tiroir mobile, confirmation `<dialog>` avec fallback `confirm()`, toast de succès après 4 secondes, bascule clair/sombre mémorisée dans `localStorage` et animation de comptage qui termine sur la valeur serveur. Sans JS, les formulaires et messages restent utilisables.

Les éléments supplémentaires hors guide sont la bascule de thème, le raccourci `/` et l'animation de comptage ; ils n'altèrent aucune règle métier.

## Vérifications

La suite Maven contient 5 tests d'intégration avec H2 et MockMvc. Elle vérifie notamment :

- stock initial 0 ;
- entrée de 20 ;
- sortie de 7 ;
- stock obtenu 13 ;
- refus d'une sortie de 15 avec stock conservé à 13 ;
- refus des quantités nulles ou négatives ;
- présence d'au moins trois articles de test.

Commande :

```powershell
.\mvnw.cmd -q test
```

## Captures d'écran

- [01-articles.png](docs/captures/01-articles.png) — KPI et liste des trois articles.
- [02-fiche-stock.png](docs/captures/02-fiche-stock.png) — fiche DEMO-001 avec stock égal à 13.
- [03-stock-insuffisant.png](docs/captures/03-stock-insuffisant.png) — refus d'une sortie de 15, stock conservé à 13.

Les captures sont réalisées en 1440 × 900 avec le thème sombre. Pour vérifier la persistance : ajouter une entrée ou une sortie, arrêter l'application, la relancer, puis rouvrir la fiche ; le stock doit rester identique.



