INSERT INTO USER (username, password, enabled)
VALUES ('wwirbelwind', '$2a$04$mQ0ybcO4eA7O/v.6/v4dpOejnTWsLOZhlRd5pd/ipJdcyC0bBpwFi', true);

INSERT INTO USER_AUTHORITY (user_id, authority)
VALUES ((SELECT id FROM USER WHERE username = 'wwirbelwind'), 'ROLE_USER');