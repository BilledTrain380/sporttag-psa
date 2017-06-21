-- MySQL Workbench Forward Engineering

-- -----------------------------------------------------
-- Table TOWN
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS TOWN (
  id INT NOT NULL AUTO_INCREMENT UNIQUE,
  zip VARCHAR(4) NOT NULL,
  name VARCHAR(50) NOT NULL,
  PRIMARY KEY (id));



-- -----------------------------------------------------
-- Table TEACHER
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS TEACHER (
  id INT NOT NULL AUTO_INCREMENT UNIQUE,
  name VARCHAR(100) NOT NULL,
  PRIMARY KEY (id));



-- -----------------------------------------------------
-- Table ClazzEntity
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS CLAZZ (
  id INT NOT NULL AUTO_INCREMENT UNIQUE,
  name VARCHAR(6) NOT NULL,
  FK_TEACHER_id INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_CLAZZ_TEACHER
    FOREIGN KEY (FK_TEACHER_id)
    REFERENCES TEACHER (id)
    ON DELETE RESTRICT 
    ON UPDATE RESTRICT );


-- -----------------------------------------------------
-- Table SPORT
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS SPORT (
  id INT NOT NULL AUTO_INCREMENT UNIQUE ,
  name VARCHAR(45) NOT NULL,
  PRIMARY KEY (id));

  
-- -----------------------------------------------------
-- Table COMPETITOR
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS COMPETITOR (
  id INT NOT NULL AUTO_INCREMENT UNIQUE,
  surname VARCHAR(30) NOT NULL,
  prename VARCHAR(30) NOT NULL,
  gender BOOLEAN NOT NULL,
  birthday DATE NOT NULL,
  address VARCHAR(80) NOT NULL,
  FK_TOWN_id INT NOT NULL,
  FK_CLAZZ_id INT NOT NULL,
  FK_SPORT_id INT,
  PRIMARY KEY (id) ,
  CONSTRAINT fk_COMPETITOR_TOWN
    FOREIGN KEY (FK_TOWN_id)
    REFERENCES TOWN (id)
    ON DELETE RESTRICT 
    ON UPDATE RESTRICT,
  CONSTRAINT fk_COMPETITOR_CLAZZ
    FOREIGN KEY (FK_CLAZZ_id)
    REFERENCES CLAZZ (id)
    ON DELETE RESTRICT 
    ON UPDATE RESTRICT,
  CONSTRAINT fk_COMPETITOR_SPORT
    FOREIGN KEY (FK_SPORT_id)
    REFERENCES SPORT (id)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT );


-- -----------------------------------------------------
-- Table UNIT
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS UNIT (
  id INT NOT NULL AUTO_INCREMENT UNIQUE ,
  unit VARCHAR(15) NOT NULL UNIQUE ,
  PRIMARY KEY (id));


-- -----------------------------------------------------
-- Table DISCIPLINE
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS DISCIPLINE (
  id INT NOT NULL AUTO_INCREMENT UNIQUE ,
  name VARCHAR(45) NOT NULL UNIQUE ,
  UNIT_id INT NOT NULL ,
  PRIMARY KEY (id)  ,
  CONSTRAINT fk_DISCIPLINE_UNIT
  FOREIGN KEY (UNIT_id)
  REFERENCES UNIT (id)
  ON DELETE RESTRICT 
  ON UPDATE RESTRICT );


-- -----------------------------------------------------
-- Table STARTNUMBER
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS STARTNUMBER (
  startnumber INT NOT NULL AUTO_INCREMENT UNIQUE ,
  COMPETITOR_id INT NOT NULL UNIQUE ,
  PRIMARY KEY (startnumber)  ,
  CONSTRAINT fk_STARTNUMBER_COMPETITOR
  FOREIGN KEY (COMPETITOR_id)
  REFERENCES COMPETITOR (id)
  ON DELETE CASCADE 
  ON UPDATE CASCADE );


-- -----------------------------------------------------
-- Table RESULT
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS RESULT (
  id INT NOT NULL AUTO_INCREMENT UNIQUE ,
  performance BIGINT NOT NULL DEFAULT 0 ,
  points INT NOT NULL DEFAULT 0 ,
  STARTNUMBER_startnumber INT NOT NULL ,
  DISCIPLIN_id INT NOT NULL ,
  PRIMARY KEY (id)  ,
  CONSTRAINT fk_RESULT_STARTNUMBER
  FOREIGN KEY (STARTNUMBER_startnumber)
  REFERENCES STARTNUMBER (startnumber)
  ON DELETE CASCADE 
  ON UPDATE CASCADE ,
  CONSTRAINT fk_RESULT_DISCIPLIN
  FOREIGN KEY (DISCIPLIN_id)
  REFERENCES DISCIPLINE (id)
  ON DELETE CASCADE 
  ON UPDATE CASCADE )