# Captures réelles pour la remise

Les fichiers PNG ne sont pas joints. L'application a pu démarrer sur une base H2 temporaire et le parcours web a été alimenté avec trois articles d'aperçu, mais la commande de capture Edge n'a pas produit de fichiers dans cet environnement. L'accès à MySQL local a répondu `Access denied` pour `stock_app`. Vérifiez le secret local avant de prendre les captures sur la base de démonstration. Ne placez pas de mot de passe dans les fichiers du projet.

Après avoir défini cette variable dans VS Code et démarré `StockimsApplication`, enregistrez ces trois captures dans ce dossier :

1. `01-articles.png` — ouvrir `http://localhost:8080/articles` et montrer la liste ainsi que les stocks calculés.
2. `02-fiche-stock.png` — ouvrir la fiche d'un article avec des mouvements et montrer la quantité calculée.
3. `03-stock-insuffisant.png` — sur cette fiche, tenter une sortie supérieure au stock et capturer le message rouge. Le stock doit rester inchangé.

Utilisez un outil de capture d'écran Windows (par exemple `Win` + `Shift` + `S`). Les tests automatisés valident les réponses HTML et le scénario métier avec une base H2 ; ils ne sont pas des captures de navigateur.
