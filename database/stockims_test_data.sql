-- Structure correspondant aux entités JPA du projet.
-- Jeu de démonstration non destructif : exécuter une seule fois dans stock_ims.
-- Ce fichier ne contient aucun identifiant de connexion ni instruction DROP.

USE stock_ims;

CREATE TABLE IF NOT EXISTS article (
    id BIGINT NOT NULL AUTO_INCREMENT,
    designation VARCHAR(80) NOT NULL,
    reference VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_article_reference (reference)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS entree (
    id BIGINT NOT NULL AUTO_INCREMENT,
    date_mouvement DATE NOT NULL,
    quantite INT NOT NULL,
    article_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    KEY idx_entree_article (article_id),
    CONSTRAINT fk_entree_article FOREIGN KEY (article_id) REFERENCES article (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sortie (
    id BIGINT NOT NULL AUTO_INCREMENT,
    date_mouvement DATE NOT NULL,
    quantite INT NOT NULL,
    article_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    KEY idx_sortie_article (article_id),
    CONSTRAINT fk_sortie_article FOREIGN KEY (article_id) REFERENCES article (id)
) ENGINE=InnoDB;

-- Données d'exemple : stock final de DEMO-001 = 20 - 7 = 13.
-- Les INSERT sont idempotents : le script peut être rejoué sans créer de doublons.
INSERT INTO article (reference, designation)
SELECT 'DEMO-001', 'Article de démonstration'
WHERE NOT EXISTS (SELECT 1 FROM article WHERE reference = 'DEMO-001');

INSERT INTO article (reference, designation)
SELECT 'DEMO-002', 'Article sans mouvement'
WHERE NOT EXISTS (SELECT 1 FROM article WHERE reference = 'DEMO-002');

INSERT INTO article (reference, designation)
SELECT 'DEMO-003', 'Article de démonstration complémentaire'
WHERE NOT EXISTS (SELECT 1 FROM article WHERE reference = 'DEMO-003');

INSERT INTO entree (article_id, quantite, date_mouvement)
SELECT id, 20, CURRENT_DATE
FROM article
WHERE reference = 'DEMO-001'
  AND NOT EXISTS (
      SELECT 1 FROM entree e
      WHERE e.article_id = article.id AND e.quantite = 20
  );

INSERT INTO sortie (article_id, quantite, date_mouvement)
SELECT id, 7, CURRENT_DATE
FROM article
WHERE reference = 'DEMO-001'
  AND NOT EXISTS (
      SELECT 1 FROM sortie s
      WHERE s.article_id = article.id AND s.quantite = 7
  );
