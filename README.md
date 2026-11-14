# StockIMS

StockIMS est une application web de gestion de stock développée avec Spring
Boot . Elle permet de référencer des articles, d'enregistrer les
entrées et sorties, puis de consulter le stock calculé à partir des mouvements.

## Participants

### Mohamed Salmane SHAME

### SIFFEDINE Youssra

## Sommaire

- [Fonctionnalités](#fonctionnalités)
- [Technologies](#technologies)
- [Installation et lancement](#installation-et-lancement)
- [Personnalisation OBSIDIENNE](#personnalisation-obsidienne)
- [Vérifications](#vérifications)
- [Captures](#captures)

## Fonctionnalités

- Création d'articles avec référence unique et champs validés.
- Calcul du stock réel : total des entrées moins total des sorties.
- Refus des quantités nulles ou négatives.
- Refus d'une sortie supérieure au stock disponible.
- Recherche instantanée par référence ou désignation.
- Interface française responsive, utilisable avec JavaScript désactivé.

La navigation principale reste volontairement courte : **Accueil**, **Articles**
et **Nouvel article**. La fiche de stock est accessible depuis chaque ligne du
tableau.

## Technologies

- Java 21
- Spring Boot 4.1.1, Spring MVC, Spring Data JPA
- Thymeleaf, Jakarta Validation, Lombok
- MySQL 8.x en production
- H2 et MockMvc pour les tests
- Maven Wrapper (`mvnw`, `mvnw.cmd`)

## Installation et lancement

### 1. Configurer MySQL

Créer la base et un compte applicatif, puis adapter
`src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/stock_ims
spring.datasource.username=stock_app
spring.datasource.password=${STOCKIMS_DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
```

Sous Windows PowerShell, le mot de passe reste hors du dépôt :

```powershell
$env:STOCKIMS_DB_PASSWORD = "votre-mot-de-passe"
```

### 2. Initialiser les données de démonstration (optionnel)

Le script [database/stockims_test_data.sql](database/stockims_test_data.sql)
crée trois articles : `DEMO-001`, `DEMO-002` et `DEMO-003`. DEMO-001 possède
une entrée de 20 et une sortie de 7, soit un stock final de 13.

### 3. Lancer l'application

Depuis le dossier contenant `pom.xml` :

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd package
.\mvnw.cmd spring-boot:run
```

L'application est disponible sur <http://localhost:8080/>.

## Personnalisation OBSIDIENNE

### Intention de design

**OBSIDIENNE** est un tableau de bord sombre, calme et précis, inspiré de
Linear, Vercel et Raycast. Le noir bleuté réduit la fatigue visuelle, les
surfaces en niveaux structurent l'information et les détails lumineux donnent
une impression premium sans surcharger l'écran.

### Palette et états

Les variables CSS sont regroupées au début de
[style.css](src/main/resources/static/css/style.css) :

- `#0B0D10` : fond principal ;
- `#12161B` et `#181D24` : surfaces et surfaces élevées ;
- `#232A33` : bordures ;
- `#E8ECF1` et `#8A94A3` : texte principal et atténué ;
- `#34D399` : action positive et entrée de stock ;
- `#FB7185` : sortie et erreur ;
- `#60A5FA` : information et références.

Les contrastes sont renforcés par des surfaces dédiées aux alertes. Le seul
halo décoratif est un radial-gradient émeraude très discret en haut à gauche ;
aucun effet glassmorphism ni empilement de dégradés n'est utilisé.

### Typographie et composants

- Inter est auto-hébergée dans `src/main/resources/static/fonts` pour
  l'interface.
- JetBrains Mono est utilisée pour les références, quantités et KPI, avec
  `font-variant-numeric: tabular-nums`.
- Les cartes utilisent un rayon de 12 px, les champs et boutons 8 px, avec une
  bordure d'un pixel et un filet de lumière interne.
- Les icônes sont des SVG inline de style Lucide dans
  [fragments/icons.html](src/main/resources/templates/fragments/icons.html) :
  aucun caractère Unicode décoratif n'est utilisé.

### Structure des écrans

- **Accueil** : titre court, sous-titre, action principale et grille bento des
  trois opérations essentielles.
- **Articles** : trois KPI calculées dans le template à partir de la map
  `stocks`, barre de recherche avec raccourci `/`, puis tableau dense.
- **Fiche de stock** : stock disponible en grand format, formulaires entrée et
  sortie côte à côte, et erreur placée au-dessus du formulaire concerné.
- **Responsive** : sous 560 px, chaque ligne du tableau devient une carte grâce
  aux attributs `data-label`.

### JavaScript progressif

Le fichier [app.js](src/main/resources/static/js/app.js) ajoute sans rendre
l'application dépendante du JavaScript :

- recherche instantanée et raccourci `/` ;
- tiroir de navigation mobile ;
- confirmation de sortie avec `<dialog>` natif et fallback `confirm()` ;
- toast de succès qui disparaît après 4 secondes ;
- bascule sombre/clair mémorisée dans `localStorage` ;
- animation de comptage du stock, désactivée avec
  `prefers-reduced-motion` et terminée sur la valeur exacte du serveur.

Ces trois derniers éléments sont des **extras hors guide** : ils améliorent
l'expérience sans modifier les règles métier. Les formulaires HTML, erreurs
Thymeleaf et messages restent fonctionnels sans script.

## Vérifications

Lancer la suite complète :

```powershell
.\mvnw.cmd -q test
```

Les cinq tests d'intégration utilisent H2 et MockMvc. Ils vérifient le parcours
entrée/sortie, le refus d'une sortie insuffisante, les validations de quantité,
la référence unique, la recherche et l'état vide.

### Vérifier la persistance

1. Démarrer l'application avec MySQL.
2. Ouvrir la fiche de `DEMO-001` et noter le stock.
3. Enregistrer une entrée ou une sortie valide.
4. Arrêter l'application sans supprimer la base.
5. Relancer `.\mvnw.cmd spring-boot:run`.
6. Rouvrir la même fiche : le stock doit être identique à celui observé avant
   l'arrêt.

## Captures

Les captures ont été produites en thème sombre, avec Chrome headless en
1440 × 900 CSS pixels, sur une base H2 propre utilisée uniquement pour la
démonstration :

- [01-articles.png](docs/captures/01-articles.png) — KPI et trois articles,
  stocks 13, 0 et 0 ;
- [02-fiche-stock.png](docs/captures/02-fiche-stock.png) — fiche DEMO-001 avec
  stock disponible de 13 ;
- [03-stock-insuffisant.png](docs/captures/03-stock-insuffisant.png) — sortie
  de 15 refusée, alerte rose visible, stock conservé à 13.

Le détail de la procédure est disponible dans
[docs/captures/README.md](docs/captures/README.md).

