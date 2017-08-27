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
  (' Weitsprung', (SELECT id FROM UNIT WHERE unit = 'Meter')),
  ('Weitwurf', (SELECT id FROM UNIT WHERE unit = 'Punkte')),
  ('Zielwurf', (SELECT id FROM UNIT WHERE unit = 'Punkte')),
  ('Seilspringen', (SELECT id FROM UNIT WHERE unit = 'Punkte'));

INSERT INTO PARTICIPATION (is_finished) VALUES
  (FALSE);