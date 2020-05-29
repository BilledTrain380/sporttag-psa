INSERT INTO USER (username, password, enabled)
VALUES ('user', '$2y$04$yg1jb.CmW8sDlBhSfEeU7uH2cnlkglilPMNDVCK1JMbJtDGww/gBC', true),
       ('mmuster', '$2y$04$yg1jb.CmW8sDlBhSfEeU7uH2cnlkglilPMNDVCK1JMbJtDGww/gBC', true);

INSERT INTO USER_AUTHORITY (user_id, authority)
VALUES ((SELECT id FROM USER WHERE username = 'user'), 'ROLE_USER'),
       ((SELECT id FROM USER WHERE username = 'mmuster'), 'ROLE_USER');
