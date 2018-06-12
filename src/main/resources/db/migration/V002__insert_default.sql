INSERT INTO SPORT (name) VALUES 
('Schatzsuche'),
('Mehrkampf'),
('Brennball'),
('Velo- Rollerblades');

INSERT INTO UNIT (unit) VALUES
('Meter'),
('Sekunden'),
('Punkte');

INSERT INTO DISCIPLINE (name, FK_UNIT_id) VALUES 
  ('Schnelllauf', (SELECT id FROM UNIT WHERE unit = 'Sekunden')),
  ('Weitsprung', (SELECT id FROM UNIT WHERE unit = 'Meter')),
  ('Ballwurf', (SELECT id FROM UNIT WHERE unit = 'Punkte')),
  ('Ballzielwurf', (SELECT id FROM UNIT WHERE unit = 'Punkte')),
  ('Seilspringen', (SELECT id FROM UNIT WHERE unit = 'Punkte')),
  ('Korbeinwurf', (SELECT id FROM UNIT WHERE unit = 'Punkte'));

INSERT INTO PARTICIPATION (is_finished) VALUES
  (FALSE);

INSERT INTO AUTHORITY (role) VALUES
  ('ADMIN'),
  ('USER');

INSERT INTO USER (username, password, enabled) VALUES
  ('admin', '$2a$04$mQ0ybcO4eA7O/v.6/v4dpOejnTWsLOZhlRd5pd/ipJdcyC0bBpwFi', true);

INSERT INTO USER_AUTHORITY (user_id, authority) VALUES 
  ((SELECT id FROM USER WHERE username = 'admin'), 'ADMIN'),
  ((SELECT id FROM USER WHERE username = 'admin'), 'USER');