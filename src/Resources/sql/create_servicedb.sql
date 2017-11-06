-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'TRADITIONAL, ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema servicedb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema servicedb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `servicedb`
  DEFAULT CHARACTER SET utf8;
USE `servicedb`;

-- -----------------------------------------------------
-- Table `servicedb`.`user_group`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `servicedb`.`user_group` (
  `id`   INT         NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;
