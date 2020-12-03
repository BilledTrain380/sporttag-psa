-- Change setup to be initialized in order to use default login data
UPDATE SETUP as s
SET s.initialized = true
WHERE s.name = 'default';

INSERT INTO COACH (name) VALUES
('Peter Muster'),
('Willi Wirbelwind');

INSERT INTO PARTICIPANT_GROUP (name, FK_COACH_id) VALUES
('2a', (SELECT c.id FROM COACH AS c WHERE c.name = 'Peter Muster')),
('2b', (SELECT c.id FROM COACH AS c WHERE c.name = 'Willi Wirbelwind'));

INSERT INTO TOWN (zip, name) VALUES
('3000', 'Bern');

INSERT INTO PARTICIPANT (prename, surname, gender, birthday, address, FK_TOWN_id, FK_GROUP_name, FK_SPORT_name) VALUES
('Emily', 'Hill', 'FEMALE', '2011-02-23', 'Hanover Street 45', (SELECT t.id FROM TOWN AS t WHERE t.zip = '3000'), (SELECT g.name FROM PARTICIPANT_GROUP AS g WHERE g.name = '2a'), 'Schatzsuche'),
('Betty', 'Robertson', 'MALE', '2011-05-12', 'Lighthouse Drive 354', (SELECT t.id FROM TOWN AS t WHERE t.zip = '3000'), (SELECT g.name FROM PARTICIPANT_GROUP AS g WHERE g.name = '2a'), null),
('Howard', 'Jordan', 'MALE', '2011-08-12', 'Neville Street 21', (SELECT t.id FROM TOWN AS t WHERE t.zip = '3000'), (SELECT g.name FROM PARTICIPANT_GROUP AS g WHERE g.name = '2a'), 'Brennball'),
('Dorothy C', 'Kirklin', 'FEMALE', '2012-01-26', 'Arlington Avenue 430', (SELECT t.id FROM TOWN AS t WHERE t.zip = '3000'), (SELECT g.name FROM PARTICIPANT_GROUP AS g WHERE g.name = '2a'), null),
('Orville J', 'Granata', 'MALE', '2010-11-18', 'Catherine Drive 1530', (SELECT t.id FROM TOWN AS t WHERE t.zip = '3000'), (SELECT g.name FROM PARTICIPANT_GROUP AS g WHERE g.name = '2a'), 'Brennball'),

('Marilynn', 'Decker', 'FEMALE', '2012-08-13', 'Rosewood Lane 3550', (SELECT t.id FROM TOWN AS t WHERE t.zip = '3000'), (SELECT g.name FROM PARTICIPANT_GROUP AS g WHERE g.name = '2b'), 'Schatzsuche'),
('Eric A', 'Mason', 'MALE', '2012-02-14', 'Peck Court 51', (SELECT t.id FROM TOWN AS t WHERE t.zip = '3000'), (SELECT g.name FROM PARTICIPANT_GROUP AS g WHERE g.name = '2b'), 'Athletics'),
('Gerald M', 'Sampson', 'MALE', '2011-08-26', 'Tenmile 561', (SELECT t.id FROM TOWN AS t WHERE t.zip = '3000'), (SELECT g.name FROM PARTICIPANT_GROUP AS g WHERE g.name = '2b'), 'Brennball'),
('Harold', 'Boyette', 'FEMALE', '2012-04-18', 'Duck Creek Road 464', (SELECT t.id FROM TOWN AS t WHERE t.zip = '3000'), (SELECT g.name FROM PARTICIPANT_GROUP AS g WHERE g.name = '2b'), 'Athletics'),
('Willie C', 'Jackson', 'MALE', '2011-10-25', 'Red Bud Lane 61', (SELECT t.id FROM TOWN AS t WHERE t.zip = '3000'), (SELECT g.name FROM PARTICIPANT_GROUP AS g WHERE g.name = '2b'), 'Athletics');
