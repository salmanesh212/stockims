# Présentation de StockIMS

## Choix visuels

L'interface utilise un bleu marine pour la navigation, un bleu franc pour les actions principales, des surfaces blanches et un fond gris clair. Le vert marque les entrées et les confirmations ; le rouge identifie les sorties et les erreurs. Les tailles, couleurs, espacements et arrondis sont regroupés en variables CSS.

## Navigation et pages

Le layout commun regroupe la navigation latérale, l'en-tête de page et le pied de page. Sur petit écran, le menu latéral devient un tiroir ouvrable au clavier ou au toucher. L'accueil mène à la liste des articles. Depuis la liste, chaque ligne ouvre la fiche de stock de l'article.

## Articles et stocks

La liste affiche les données fournies par Thymeleaf et le stock calculé depuis les entrées et sorties présentes en base. Deux requêtes d'agrégation calculent les stocks de tous les articles. La fiche utilise les routes POST existantes. `StockService` vérifie le stock sous verrou transactionnel avant d'enregistrer une sortie.

## Formulaires et JavaScript

Les formulaires restent des formulaires HTML reliés aux objets Spring MVC et leurs validations serveur sont conservées. Le JavaScript filtre la liste sans modifier les données de la base et confirme les sorties avant l'envoi. Si JavaScript est désactivé, la recherche et la confirmation disparaissent, mais les formulaires métier continuent de fonctionner.

## Thymeleaf

Thymeleaf fournit le contenu réel au layout et aux pages. Aucun tableau statique de statistiques ou stock inventé n'est présenté comme une donnée réelle.
