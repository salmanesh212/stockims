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

Le layout Thymeleaf partagé se trouve dans `src/main/resources/templates/fragments/layout.html`. La feuille [style.css](src/main/resources/static/css/style.css) apporte une identité visuelle indigo/cyan, une navigation latérale en dégradé, des cartes avec ombres douces, des boutons d'action distincts pour les entrées et sorties, ainsi qu'un affichage responsive. Le fichier `app.js` gère la recherche, le menu mobile et la confirmation des sorties.

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

- [01-articles.png](docs/captures/01-articles.png) — interface personnalisée et stocks calculés.
- [02-fiche-stock.png](docs/captures/02-fiche-stock.png) — fiche avec stock égal à 13.
- [03-stock-insuffisant.png](docs/captures/03-stock-insuffisant.png) — message de validation après tentative de sortie de 15.



