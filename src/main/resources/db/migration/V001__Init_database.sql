-- -----------------------------------------------------
-- Table TOWN
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS TOWN (
  id INT NOT NULL AUTO_INCREMENT UNIQUE,
  zip VARCHAR(4) NOT NULL,
  name VARCHAR(50) NOT NULL,
  PRIMARY KEY (id)
);



-- -----------------------------------------------------
-- Table COACH
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS COACH (
  id INT NOT NULL AUTO_INCREMENT UNIQUE,
  name VARCHAR(100) NOT NULL,
  PRIMARY KEY (id)
);



-- -----------------------------------------------------
-- Table ClazzEntity
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS CLAZZ (
  name VARCHAR(20) NOT NULL UNIQUE ,
  FK_coach_id INT NOT NULL,
  PRIMARY KEY (name),
  CONSTRAINT fk_CLAZZ_COACH
    FOREIGN KEY (FK_coach_id)
    REFERENCES COACH (id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
);


-- -----------------------------------------------------
-- Table SPORT
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS SPORT (
  name VARCHAR(45) NOT NULL UNIQUE ,
  PRIMARY KEY (name)
);

  
-- -----------------------------------------------------
-- Table COMPETITOR
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS COMPETITOR (
  id INT NOT NULL AUTO_INCREMENT UNIQUE,
  surname VARCHAR(30) NOT NULL,
  prename VARCHAR(30) NOT NULL,
  gender BOOLEAN NOT NULL,
  birthday BIGINT NOT NULL,
  address VARCHAR(80) NOT NULL,
  FK_TOWN_id INT NOT NULL,
  FK_CLAZZ VARCHAR(20) NOT NULL,
  FK_SPORT VARCHAR(45),
  PRIMARY KEY (id) ,
  CONSTRAINT fk_COMPETITOR_TOWN
    FOREIGN KEY (FK_TOWN_id)
    REFERENCES TOWN (id)
    ON DELETE RESTRICT 
    ON UPDATE RESTRICT,
  CONSTRAINT fk_COMPETITOR_CLAZZ
    FOREIGN KEY (FK_CLAZZ)
    REFERENCES CLAZZ (name)
    ON DELETE RESTRICT 
    ON UPDATE RESTRICT,
  CONSTRAINT fk_COMPETITOR_SPORT
    FOREIGN KEY (FK_SPORT)
    REFERENCES SPORT (name)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);


-- -----------------------------------------------------
-- Table ABSENT_COMPETITOR
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS ABSENT_COMPETITOR (
  id INT NOT NULL AUTO_INCREMENT UNIQUE ,
  FK_competitor INT NOT NULL UNIQUE ,
  PRIMARY KEY (id),
  CONSTRAINT fk_ABSENT_COMPETITOR
    FOREIGN KEY (FK_competitor)
    REFERENCES COMPETITOR (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


-- -----------------------------------------------------
-- Table UNIT
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS UNIT (
  unit VARCHAR(15) NOT NULL UNIQUE ,
  PRIMARY KEY (unit)
);


-- -----------------------------------------------------
-- Table DISCIPLINE
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS DISCIPLINE (
  name VARCHAR(45) NOT NULL UNIQUE ,
  FK_UNIT VARCHAR(15) NOT NULL ,
  PRIMARY KEY (name)  ,
  CONSTRAINT fk_DISCIPLINE_UNIT
  FOREIGN KEY (FK_UNIT)
  REFERENCES UNIT (unit)
  ON DELETE RESTRICT 
  ON UPDATE RESTRICT
);


-- -----------------------------------------------------
-- Table STARTER
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS STARTER (
  number INT NOT NULL AUTO_INCREMENT UNIQUE ,
  FK_COMPETITOR_id INT NOT NULL UNIQUE ,
  PRIMARY KEY (number)  ,
  CONSTRAINT fk_STARTER_COMPETITOR
  FOREIGN KEY (FK_COMPETITOR_id)
  REFERENCES COMPETITOR (id)
  ON DELETE CASCADE 
  ON UPDATE CASCADE
);


-- -----------------------------------------------------
-- Table RESULT
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS RESULT (
  id INT NOT NULL AUTO_INCREMENT UNIQUE ,
  distance VARCHAR(5) DEFAULT NULL ,
  result DOUBLE NOT NULL DEFAULT 0 ,
  points INT NOT NULL DEFAULT 0 ,
  FK_STARTER_number INT NOT NULL ,
  FK_DISCIPLINE VARCHAR(45) NOT NULL ,
  PRIMARY KEY (id)  ,
  CONSTRAINT fk_RESULT_STARTER
  FOREIGN KEY (FK_STARTER_number)
  REFERENCES STARTER (NUMBER)
  ON DELETE CASCADE 
  ON UPDATE CASCADE ,
  CONSTRAINT fk_RESULT_DISCIPLINE
  FOREIGN KEY (FK_DISCIPLINE)
  REFERENCES DISCIPLINE (name)
  ON DELETE CASCADE 
  ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Table PARTICIPATION
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS PARTICIPATION (
  id INT NOT NULL DEFAULT 1 UNIQUE ,
  is_finished BOOLEAN NOT NULL DEFAULT FALSE ,
  PRIMARY KEY (id)
);

-- -----------------------------------------------------
-- Table USER
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS USER (
  id INT NOT NULL AUTO_INCREMENT UNIQUE ,
  username VARCHAR(50) NOT NULL UNIQUE ,
  password VARCHAR(128) NOT NULL,
  enabled BOOLEAN NOT NULL DEFAULT false ,
  PRIMARY KEY (id)
);

-- -----------------------------------------------------
-- Table AUTHORITY
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS AUTHORITY (
  role VARCHAR(20) NOT NULL UNIQUE ,
  PRIMARY KEY (role)
);

CREATE TABLE IF NOT EXISTS USER_AUTHORITY(
  user_id INT NOT NULL ,
  authority VARCHAR(20) NOT NULL ,
  PRIMARY KEY (user_id, authority),
  CONSTRAINT fk_authority_user
    FOREIGN KEY (user_id)
    REFERENCES USER (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE ,
  CONSTRAINT fk_user_authority
    FOREIGN KEY (authority)
    REFERENCES AUTHORITY (role)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT 
);