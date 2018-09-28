-- -----------------------------------------------------
-- Table COACH
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS COACH (
  id INT NOT NULL AUTO_INCREMENT UNIQUE,
  name VARCHAR(100) NOT NULL,
  PRIMARY KEY (id)
);


-- -----------------------------------------------------
-- Table GROUP
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS PARTICIPANT_GROUP (
  name VARCHAR(20) NOT NULL UNIQUE ,
  FK_COACH_id INT NOT NULL,
  PRIMARY KEY (name),
  CONSTRAINT fk_GROUP_COACH
    FOREIGN KEY (FK_COACH_id)
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
-- Table TOWN
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS TOWN (
  id INT NOT NULL AUTO_INCREMENT UNIQUE,
  zip VARCHAR(4) NOT NULL,
  name VARCHAR(50) NOT NULL,
  PRIMARY KEY (id)
);

-- -----------------------------------------------------
-- Table PARTICIPANT
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS PARTICIPANT (
  id INT NOT NULL AUTO_INCREMENT UNIQUE,
  surname VARCHAR(30) NOT NULL,
  prename VARCHAR(30) NOT NULL,
  gender VARCHAR(6) NOT NULL,
  birthday BIGINT NOT NULL,
  address VARCHAR(80) NOT NULL,
  FK_TOWN_id INT NOT NULL,
  FK_GROUP_name VARCHAR(20) NOT NULL,
  FK_SPORT_name VARCHAR(45),
  PRIMARY KEY (id) ,
  CONSTRAINT fk_PARTICIPANT_TOWN
    FOREIGN KEY (FK_TOWN_id)
    REFERENCES TOWN (id)
    ON DELETE RESTRICT 
    ON UPDATE RESTRICT,
  CONSTRAINT fk_PARTICIPANT_GROUP
    FOREIGN KEY (FK_GROUP_name)
    REFERENCES PARTICIPANT_GROUP (name)
    ON DELETE RESTRICT 
    ON UPDATE RESTRICT,
  CONSTRAINT fk_PARTICIPANT_SPORT
    FOREIGN KEY (FK_SPORT_name)
    REFERENCES SPORT (name)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);


-- -----------------------------------------------------
-- Table ABSENT_PARTICIPANT
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS ABSENT_PARTICIPANT (
  id INT NOT NULL AUTO_INCREMENT UNIQUE ,
  FK_PARTICIPANT_id INT NOT NULL UNIQUE ,
  PRIMARY KEY (id),
  CONSTRAINT fk_ABSENT_PARTICIPANT
    FOREIGN KEY (FK_PARTICIPANT_id)
    REFERENCES PARTICIPANT (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Table COMPETITOR
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS COMPETITOR (
  startnumber INT NOT NULL AUTO_INCREMENT UNIQUE ,
  FK_PARTICIPANT_id INT NOT NULL UNIQUE ,
  PRIMARY KEY (startnumber)  ,
  CONSTRAINT fk_COMPETITOR_PARTICIPANT
  FOREIGN KEY (FK_PARTICIPANT_id)
  REFERENCES PARTICIPANT (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Table UNIT
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS UNIT (
  name VARCHAR(45) NOT NULL UNIQUE ,
  factor INT NOT NULL DEFAULT 1,
  PRIMARY KEY (name)
);


-- -----------------------------------------------------
-- Table DISCIPLINE
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS DISCIPLINE (
  name VARCHAR(45) NOT NULL UNIQUE ,
  FK_UNIT_name VARCHAR(45) NOT NULL ,
  PRIMARY KEY (name)  ,
  CONSTRAINT fk_DISCIPLINE_UNIT
  FOREIGN KEY (FK_UNIT_name)
  REFERENCES UNIT (name)
  ON DELETE RESTRICT 
  ON UPDATE RESTRICT
);

-- -----------------------------------------------------
-- Table RESULT
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS RESULT (
  id INT NOT NULL AUTO_INCREMENT UNIQUE ,
  distance VARCHAR(5) DEFAULT NULL ,
  value BIGINT NOT NULL DEFAULT 0 ,
  points INT NOT NULL DEFAULT 0 ,
  FK_COMPETITOR_startnumber INT NOT NULL ,
  FK_DISCIPLINE VARCHAR(45) NOT NULL ,
  PRIMARY KEY (id)  ,
  CONSTRAINT fk_RESULT_COMPETITOR
  FOREIGN KEY (FK_COMPETITOR_startnumber)
  REFERENCES COMPETITOR (startnumber)
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
  name VARCHAR(10) NOT NULL DEFAULT 'main',
  status VARCHAR(10) NOT NULL DEFAULT 'OPEN',
  PRIMARY KEY (name)
);

CREATE TABLE IF NOT EXISTS SETUP (
  name VARCHAR(10) NOT NULL DEFAULT 'default',
  initialized BOOLEAN NOT NULL DEFAULT false,
  jwt_secret VARCHAR(32) NOT NULL DEFAULT ''
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