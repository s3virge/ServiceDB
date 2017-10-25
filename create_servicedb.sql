-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema servicedb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `servicedb` DEFAULT CHARACTER SET utf8 ;
USE `servicedb` ;

-- -----------------------------------------------------
-- Table `servicedb`.`user_group`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `servicedb`.`user_group` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `servicedb`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `servicedb`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `user_group` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_group_idx` (`user_group` ASC),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC),
  CONSTRAINT `user_group`
    FOREIGN KEY (`user_group`)
    REFERENCES `servicedb`.`user_group` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `servicedb`.`name`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `servicedb`.`name` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`value` ASC),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'имена пользователей';


-- -----------------------------------------------------
-- Table `servicedb`.`patronymic`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `servicedb`.`patronymic` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `value_UNIQUE` (`value` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'отчества';


-- -----------------------------------------------------
-- Table `servicedb`.`surname`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `servicedb`.`surname` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `value_UNIQUE` (`value` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'фамилии тут';


-- -----------------------------------------------------
-- Table `servicedb`.`owner`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `servicedb`.`owner` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `surname_id` INT NOT NULL,
  `name_id` INT NOT NULL,
  `patronymic_id` INT NOT NULL,
  `telephone_number` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `owner_name_idx` (`name_id` ASC),
  INDEX `owner_patronymic_idx` (`patronymic_id` ASC),
  INDEX `owner_surname_idx` (`surname_id` ASC),
  CONSTRAINT `owner_name`
    FOREIGN KEY (`name_id`)
    REFERENCES `servicedb`.`name` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `owner_patronymic`
    FOREIGN KEY (`patronymic_id`)
    REFERENCES `servicedb`.`patronymic` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `owner_surname`
    FOREIGN KEY (`surname_id`)
    REFERENCES `servicedb`.`surname` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'информация о пользователе';


-- -----------------------------------------------------
-- Table `servicedb`.`brand`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `servicedb`.`brand` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `servicedb`.`type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `servicedb`.`type` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'тип устройства';


-- -----------------------------------------------------
-- Table `servicedb`.`model`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `servicedb`.`model` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'таблица для хранения модели устройства';


-- -----------------------------------------------------
-- Table `servicedb`.`defect`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `servicedb`.`defect` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(500) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `description_UNIQUE` (`description` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'неисправности';


-- -----------------------------------------------------
-- Table `servicedb`.`device`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `servicedb`.`device` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type_id` INT NOT NULL,
  `brand_id` INT NOT NULL,
  `model_id` INT NOT NULL,
  `serial_number` VARCHAR(45) NULL,
  `defect_id` INT NOT NULL,
  `owner_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `owner_idx` (`owner_id` ASC),
  INDEX `brand_idx` (`brand_id` ASC),
  INDEX `type_idx` (`type_id` ASC),
  INDEX `model_idx` (`model_id` ASC),
  INDEX `defect_idx` (`defect_id` ASC),
  CONSTRAINT `device_owner`
    FOREIGN KEY (`owner_id`)
    REFERENCES `servicedb`.`owner` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `device_brand`
    FOREIGN KEY (`brand_id`)
    REFERENCES `servicedb`.`brand` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `device_type`
    FOREIGN KEY (`type_id`)
    REFERENCES `servicedb`.`type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `device_model`
    FOREIGN KEY (`model_id`)
    REFERENCES `servicedb`.`model` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `device_defect`
    FOREIGN KEY (`defect_id`)
    REFERENCES `servicedb`.`defect` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
