INSERT INTO USER (username, password, enabled)
VALUES ('fhanseler', '$2a$04$mQ0ybcO4eA7O/v.6/v4dpOejnTWsLOZhlRd5pd/ipJdcyC0bBpwFi', true);

INSERT INTO USER_AUTHORITY (user_id, authority)
VALUES ((SELECT id FROM USER WHERE username = 'fhanseler'), 'ROLE_USER');
