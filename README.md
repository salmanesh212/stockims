# StockIMS

Application de gestion de stock réalisée avec Spring Boot et Thymeleaf.

## Équipe

- Étudiant(e) 1 : à renseigner
- Étudiant(e) 2 : à renseigner

## Fonctionnalités

- Référencement des articles avec référence unique et champs validés.
- Consultation du stock réel calculé comme la somme des entrées moins la somme des sorties.
- Enregistrement des entrées et sorties persistées dans MySQL.
- Refus serveur d'une quantité invalide ou d'une sortie supérieure au stock.
- Recherche instantanée des articles par référence ou désignation.
- Interface française responsive avec navigation commune.

Le modèle métier conserve les trois tables `article`, `entree` et `sortie`. Chaque entrée et chaque sortie est liée à un article. Les pages utilisent Thymeleaf ; le JavaScript sert à filtrer le tableau et demander confirmation avant une sortie. Les validations métier restent côté serveur.

## Technologies et prérequis

- Java 21
- Spring Boot 4.1.x, Spring MVC, Spring Data JPA
- Thymeleaf, Jakarta Validation, Lombok
- MySQL 8.x
- Maven Wrapper inclus (`mvnw.cmd`)

## Configuration MySQL

Créez une base `stock_ims` et un compte ayant les droits sur cette base. Configurez les paramètres suivants dans `src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/stock_ims
spring.datasource.username=stock_app
spring.datasource.password=${STOCKIMS_DB_PASSWORD}
```

Définissez `STOCKIMS_DB_PASSWORD` dans l'environnement utilisé pour lancer l'application. Ne placez pas de mot de passe dans Git. Hibernate conserve `ddl-auto=update` pour synchroniser le schéma des entités.

## Lancement sous Windows

Depuis le dossier contenant `pom.xml` :

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd package
.\mvnw.cmd spring-boot:run
```

L'application est disponible sur <http://localhost:8080/>. Dans VS Code, ouvrez le dossier racine du projet, importez-le comme projet Maven, puis lancez `StockimsApplication`.

## Personnalisation de l'interface

Le layout Thymeleaf partagé est dans `src/main/resources/templates/fragments/layout.html`. Les styles communs sont dans `static/css/style.css`, et les interactions légères dans `static/js/app.js`. L'accueil présente les opérations disponibles sans chiffres de démonstration. La liste calcule le stock des articles via deux requêtes d'agrégation, et la fiche affiche les mouvements réels.

## Vérifications réalisées

- `.\mvnw.cmd -q test` : réussi, 4 tests d'intégration avec H2 et MockMvc, dont le scénario stock 0 → 20 → 13, refus d'une sortie de 15, quantités nulles/négatives, champs obligatoires et référence dupliquée.
- `.\mvnw.cmd -q package` : réussi.
- Le scénario a été exécuté sur H2 ; l'accès à MySQL local a répondu `Access denied` pour `stock_app`, donc le test de persistance après redémarrage sur MySQL reste à vérifier avec les identifiants locaux valides.

## Fichiers de remise

- `database/stockims_test_data.sql` : structure et jeu d'essai à exécuter une fois sur une base de démonstration.
- `PRESENTATION.md` : résumé des choix de réalisation.
- `REMISE_MOODLE.txt` : texte de remise à compléter sans inventer le lien du dépôt ou les noms.
- `docs/captures/README.md` : étapes pour réaliser les captures demandées depuis l'application réellement lancée.

## Publication GitHub et invitation du professeur

Ce dossier n'est pas encore un dépôt Git et aucun dépôt distant n'est configuré. Après création du dépôt GitHub et publication du code, ouvrez **Settings → Collaborators → Add people**, recherchez le compte `amozher`, puis confirmez l'invitation. L'invitation n'a pas été envoyée depuis cet environnement.
