-- Change setup to be initialized in order to use default login data
UPDATE SETUP as s
SET s.initialized = true
WHERE s.name = 'default';