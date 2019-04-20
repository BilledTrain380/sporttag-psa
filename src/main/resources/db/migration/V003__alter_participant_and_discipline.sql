DROP TABLE ABSENT_PARTICIPANT;

ALTER TABLE PARTICIPANT
  ADD absent BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE DISCIPLINE
  ADD (hasTrials BOOLEAN NOT NULL DEFAULT FALSE ,
      hasDistance BOOLEAN NOT NULL DEFAULT FALSE);

UPDATE DISCIPLINE
  SET hasTrials = TRUE
  WHERE name = 'Ballwurf' OR name = 'Seilspringen' OR name = 'Weitsprung';

UPDATE DISCIPLINE
  SET hasDistance = TRUE
  WHERE name = 'Ballzielwurf' OR name = 'Korbeinwurf';