DROP TABLE ABSENT_PARTICIPANT;

ALTER TABLE PARTICIPANT
  ADD absent BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE DISCIPLINE
  ADD (has_trials BOOLEAN NOT NULL DEFAULT FALSE ,
      has_distance BOOLEAN NOT NULL DEFAULT FALSE);

UPDATE DISCIPLINE
  SET has_trials = TRUE
  WHERE name = 'Ballwurf' OR name = 'Seilspringen' OR name = 'Weitsprung';

UPDATE DISCIPLINE
  SET has_distance = TRUE
  WHERE name = 'Ballzielwurf' OR name = 'Korbeinwurf';