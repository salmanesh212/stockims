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
INSERT INTO article (reference, designation) VALUES
    ('DEMO-001', 'Article de démonstration'),
    ('DEMO-002', 'Article sans mouvement'),
    ('DEMO-003', 'Article de démonstration complémentaire');

INSERT INTO entree (article_id, quantite, date_mouvement)
SELECT id, 20, CURRENT_DATE FROM article WHERE reference = 'DEMO-001';

INSERT INTO sortie (article_id, quantite, date_mouvement)
SELECT id, 7, CURRENT_DATE FROM article WHERE reference = 'DEMO-001';
