-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema servicedb
-- -----------------------------------------------------

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
  `value` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `value_UNIQUE` (`value` ASC))
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
  UNIQUE INDEX `telephone_number_UNIQUE` (`telephone_number` ASC),
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
  `value` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `value_UNIQUE` (`value` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `servicedb`.`devicetype`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `servicedb`.`devicetype` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `value_UNIQUE` (`value` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'тип устройства';


-- -----------------------------------------------------
-- Table `servicedb`.`devicemodel`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `servicedb`.`devicemodel` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `value_UNIQUE` (`value` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'таблица для хранения модели устройства';


-- -----------------------------------------------------
-- Table `servicedb`.`defect`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `servicedb`.`defect` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(500) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `description_UNIQUE` (`value` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'неисправности';


-- -----------------------------------------------------
-- Table `servicedb`.`status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `servicedb`.`status` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `value_UNIQUE` (`value` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'этап ремонта';


-- -----------------------------------------------------
-- Table `servicedb`.`repair`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `servicedb`.`repair` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `acceptor_id` INT NOT NULL,
  `master_id` INT NOT NULL,
  `master_comments` VARCHAR(1024) NULL,
  `diagnostic_result` VARCHAR(1024) NULL,
  `repair_result` VARCHAR(450) NULL,
  `status_id` INT NOT NULL,
  `date_of_accept` DATETIME NOT NULL,
  `date_of_give_out` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `repair_status_idx` (`status_id` ASC),
  INDEX `repair_acceptor_idx` (`acceptor_id` ASC),
  INDEX `repair_master_idx` (`master_id` ASC),
  CONSTRAINT `repair_status`
    FOREIGN KEY (`status_id`)
    REFERENCES `servicedb`.`status` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `repair_acceptor`
    FOREIGN KEY (`acceptor_id`)
    REFERENCES `servicedb`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `repair_master`
    FOREIGN KEY (`master_id`)
    REFERENCES `servicedb`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'таблица для результатов диагностики-ремонта ';


-- -----------------------------------------------------
-- Table `servicedb`.`completeness`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `servicedb`.`completeness` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `value_UNIQUE` (`value` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'комплектация устройства';


-- -----------------------------------------------------
-- Table `servicedb`.`appearance`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `servicedb`.`appearance` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(500) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `value_UNIQUE` (`value` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'внешний вид устройства';


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
  `repair_id` INT NOT NULL,
  `completeness_id` INT NOT NULL,
  `appearance_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `owner_idx` (`owner_id` ASC),
  INDEX `brand_idx` (`brand_id` ASC),
  INDEX `type_idx` (`type_id` ASC),
  INDEX `model_idx` (`model_id` ASC),
  INDEX `defect_idx` (`defect_id` ASC),
  INDEX `device_repair_idx` (`repair_id` ASC),
  INDEX `device_completeness_idx` (`completeness_id` ASC),
  INDEX `device_appearance_idx` (`appearance_id` ASC),
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
    REFERENCES `servicedb`.`devicetype` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `device_model`
    FOREIGN KEY (`model_id`)
    REFERENCES `servicedb`.`devicemodel` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `device_defect`
    FOREIGN KEY (`defect_id`)
    REFERENCES `servicedb`.`defect` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `device_repair`
    FOREIGN KEY (`repair_id`)
    REFERENCES `servicedb`.`repair` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `device_completeness`
    FOREIGN KEY (`completeness_id`)
    REFERENCES `servicedb`.`completeness` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `device_appearance`
    FOREIGN KEY (`appearance_id`)
    REFERENCES `servicedb`.`appearance` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `servicedb`.`user_group`
-- -----------------------------------------------------
START TRANSACTION;
USE `servicedb`;
INSERT INTO `servicedb`.`user_group` (`id`, `value`) VALUES (1, 'administrator');
INSERT INTO `servicedb`.`user_group` (`id`, `value`) VALUES (2, 'manager');
INSERT INTO `servicedb`.`user_group` (`id`, `value`) VALUES (3, 'master');
INSERT INTO `servicedb`.`user_group` (`id`, `value`) VALUES (4, 'acceptor');

COMMIT;


-- -----------------------------------------------------
-- Data for table `servicedb`.`user`
-- -----------------------------------------------------
START TRANSACTION;
USE `servicedb`;
INSERT INTO `servicedb`.`user` (`id`, `login`, `password`, `user_group`) VALUES (1, 'admin', md5('admin'), 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `servicedb`.`status`
-- -----------------------------------------------------
START TRANSACTION;
USE `servicedb`;
INSERT INTO `servicedb`.`status` (`id`, `value`) VALUES (1, 'Оформлен');
INSERT INTO `servicedb`.`status` (`id`, `value`) VALUES (2, 'Диагностика');
INSERT INTO `servicedb`.`status` (`id`, `value`) VALUES (3, 'Ожидание комплектующих');
INSERT INTO `servicedb`.`status` (`id`, `value`) VALUES (4, 'Ремонт');
INSERT INTO `servicedb`.`status` (`id`, `value`) VALUES (5, 'Выдано');

COMMIT;

