UPDATE DISCIPLINE
    SET FK_UNIT_id = (SELECT id FROM UNIT WHERE unit = 'Meter')
    WHERE name = 'Ballwurf'
;