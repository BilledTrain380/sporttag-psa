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
-- Table CLAZZ
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS CLAZZ (
  id INT NOT NULL AUTO_INCREMENT UNIQUE,
  name VARCHAR(6) NOT NULL,
  TEACHER_id INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_CLAZZ_TEACHER
    FOREIGN KEY (TEACHER_id)
    REFERENCES TEACHER (id)
    ON DELETE RESTRICT 
    ON UPDATE RESTRICT );



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
  TOWN_id INT NOT NULL,
  CLAZZ_id INT NOT NULL,
  PRIMARY KEY (id) ,
  CONSTRAINT fk_COMPETITOR_TOWN
    FOREIGN KEY (TOWN_id)
    REFERENCES TOWN (id)
    ON DELETE RESTRICT 
    ON UPDATE RESTRICT,
  CONSTRAINT fk_COMPETITOR_CLAZZ
    FOREIGN KEY (CLAZZ_id)
    REFERENCES CLAZZ (id)
    ON DELETE RESTRICT 
    ON UPDATE RESTRICT);


