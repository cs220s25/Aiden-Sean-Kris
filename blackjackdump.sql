-- Blackjack Database Dump
-- Generated: Mon Apr 07 14:57:28 EDT 2025

USE blackjack;

DROP TABLE IF EXISTS players;
CREATE TABLE players (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL UNIQUE,
  balance INT NOT NULL DEFAULT 40
);

INSERT INTO players (id, name, balance) VALUES (2, 'subarashi42', 340);
INSERT INTO players (id, name, balance) VALUES (7, 'FirstPlayer1743854874518', 40);
INSERT INTO players (id, name, balance) VALUES (8, 'SecondPlayer1743854874518', 40);
INSERT INTO players (id, name, balance) VALUES (9, 'TestUser1743854874582', 40);
INSERT INTO players (id, name, balance) VALUES (10, 'Player11743854874595', 40);
INSERT INTO players (id, name, balance) VALUES (11, 'Player21743854874596', 40);
INSERT INTO players (id, name, balance) VALUES (12, 'Player31743854874600', 40);
