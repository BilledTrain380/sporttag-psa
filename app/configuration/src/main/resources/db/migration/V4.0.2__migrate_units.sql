INSERT INTO UNIT (name, factor)
VALUES ('Anzahl', 1),
       ('Treffer', 1);

UPDATE DISCIPLINE
SET FK_UNIT_name = (SELECT name FROM UNIT WHERE name = 'Anzahl')
WHERE name = 'Seilspringen';

UPDATE DISCIPLINE
SET FK_UNIT_name = (SELECT name FROM UNIT WHERE name = 'Treffer')
WHERE name = 'Korbeinwurf';
