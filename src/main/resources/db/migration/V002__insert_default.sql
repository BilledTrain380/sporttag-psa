INSERT INTO SPORT (name) VALUES 
('Schatzsuche'),
('Mehrkampf'),
('Brennball'),
('Velo- Rollerblades');

INSERT INTO UNIT (name, factor) VALUES
('Meter', 100),
('Sekunden', 100),
('Punkte', 1);

INSERT INTO DISCIPLINE (name, FK_UNIT_name) VALUES
  ('Schnelllauf', (SELECT name FROM UNIT WHERE name = 'Sekunden')),
  ('Weitsprung', (SELECT name FROM UNIT WHERE name = 'Meter')),
  ('Ballwurf', (SELECT name FROM UNIT WHERE name = 'Meter')),
  ('Ballzielwurf', (SELECT name FROM UNIT WHERE name = 'Punkte')),
  ('Seilspringen', (SELECT name FROM UNIT WHERE name = 'Punkte')),
  ('Korbeinwurf', (SELECT name FROM UNIT WHERE name = 'Punkte'));

INSERT INTO PARTICIPATION (name, status) VALUES
  ('main', 'OPEN');

INSERT INTO SETUP (name, initialized, jwt_secret) VALUES
  ('default', FALSE, '');

INSERT INTO AUTHORITY (role) VALUES
  ('ADMIN'),
  ('USER');

INSERT INTO USER (username, password, enabled) VALUES
  ('admin', '$2a$04$mQ0ybcO4eA7O/v.6/v4dpOejnTWsLOZhlRd5pd/ipJdcyC0bBpwFi', true);

INSERT INTO USER_AUTHORITY (user_id, authority) VALUES 
  ((SELECT id FROM USER WHERE username = 'admin'), 'ADMIN'),
  ((SELECT id FROM USER WHERE username = 'admin'), 'USER');