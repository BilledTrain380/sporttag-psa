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
('Emily', 'Hill', 'FEMALE', 1298419200000, 'Hanover Street 45', (SELECT t.id FROM TOWN AS t WHERE t.zip = '3000'), (SELECT g.name FROM PARTICIPANT_GROUP AS g WHERE g.name = '2a'), 'Schatzsuche'),
('Betty', 'Robertson', 'MALE', 1305158400000, 'Lighthouse Drive 354', (SELECT t.id FROM TOWN AS t WHERE t.zip = '3000'), (SELECT g.name FROM PARTICIPANT_GROUP AS g WHERE g.name = '2a'), null),
('Howard', 'Jordan', 'MALE', 1313107200000, 'Neville Street 21', (SELECT t.id FROM TOWN AS t WHERE t.zip = '3000'), (SELECT g.name FROM PARTICIPANT_GROUP AS g WHERE g.name = '2a'), 'Brennball'),
('Dorothy C', 'Kirklin', 'FEMALE', 1327536000000, 'Arlington Avenue 430', (SELECT t.id FROM TOWN AS t WHERE t.zip = '3000'), (SELECT g.name FROM PARTICIPANT_GROUP AS g WHERE g.name = '2a'), null),
('Orville J', 'Granata', 'MALE', 1290038400000, 'Catherine Drive 1530', (SELECT t.id FROM TOWN AS t WHERE t.zip = '3000'), (SELECT g.name FROM PARTICIPANT_GROUP AS g WHERE g.name = '2a'), 'Brennball'),

('Marilynn', 'Decker', 'FEMALE', 1344816000000, 'Rosewood Lane 3550', (SELECT t.id FROM TOWN AS t WHERE t.zip = '3000'), (SELECT g.name FROM PARTICIPANT_GROUP AS g WHERE g.name = '2b'), 'Schatzsuche'),
('Eric A', 'Mason', 'MALE', 1329177600000, 'Peck Court 51', (SELECT t.id FROM TOWN AS t WHERE t.zip = '3000'), (SELECT g.name FROM PARTICIPANT_GROUP AS g WHERE g.name = '2b'), 'Athletics'),
('Gerald M', 'Sampson', 'MALE', 1314316800000, 'Tenmile 561', (SELECT t.id FROM TOWN AS t WHERE t.zip = '3000'), (SELECT g.name FROM PARTICIPANT_GROUP AS g WHERE g.name = '2b'), 'Brennball'),
('Harold', 'Boyette', 'FEMALE', 1334707200000, 'Duck Creek Road 464', (SELECT t.id FROM TOWN AS t WHERE t.zip = '3000'), (SELECT g.name FROM PARTICIPANT_GROUP AS g WHERE g.name = '2b'), 'Athletics'),
('Willie C', 'Jackson', 'MALE', 1319673600000, 'Red Bud Lane 61', (SELECT t.id FROM TOWN AS t WHERE t.zip = '3000'), (SELECT g.name FROM PARTICIPANT_GROUP AS g WHERE g.name = '2b'), 'Athletics');
