UPDATE PARTICIPATION
SET status = 'CLOSED'
WHERE name = 'main';

DELETE
FROM COMPETITOR;

ALTER TABLE COMPETITOR
    ALTER COLUMN startnumber RESTART WITH 1;

INSERT INTO COMPETITOR (FK_PARTICIPANT_id)
SELECT id
FROM PARTICIPANT
WHERE FK_SPORT_name = 'Athletics'
ORDER BY id;

DELETE
FROM RESULT;

ALTER TABLE RESULT
    ALTER COLUMN id RESTART WITH 1;

INSERT INTO RESULT (value, points, distance, FK_COMPETITOR_startnumber, FK_DISCIPLINE)
        (SELECT 1, 1, '60m', p.startnumber, 'Schnelllauf' FROM COMPETITOR AS p);

INSERT INTO RESULT (value, points, FK_COMPETITOR_startnumber, FK_DISCIPLINE)
        (SELECT 1, 1, p.startnumber, 'Weitsprung' FROM COMPETITOR AS p);

INSERT INTO RESULT (value, points, FK_COMPETITOR_startnumber, FK_DISCIPLINE)
        (SELECT 1, 1, p.startnumber, 'Ballwurf' FROM COMPETITOR AS p);

INSERT INTO RESULT (value, points, FK_COMPETITOR_startnumber, FK_DISCIPLINE)
        (SELECT 1, 1, p.startnumber, 'Ballzielwurf' FROM COMPETITOR AS p);

INSERT INTO RESULT (value, points, FK_COMPETITOR_startnumber, FK_DISCIPLINE)
        (SELECT 1, 1, p.startnumber, 'Seilspringen' FROM COMPETITOR AS p);

INSERT INTO RESULT (value, points, FK_COMPETITOR_startnumber, FK_DISCIPLINE)
        (SELECT 1, 1, p.startnumber, 'Korbeinwurf' FROM COMPETITOR AS p);
