-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema base_chapa
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `base_chapa` ;

-- -----------------------------------------------------
-- Schema base_chapa
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `base_chapa` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `base_chapa` ;

-- -----------------------------------------------------
-- Table `base_chapa`.`cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `base_chapa`.`cliente` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(50) NOT NULL,
  `logo` VARCHAR(50) NULL,
  `ancho` INT NULL,
  `alto` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `base_chapa`.`tipo_usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `base_chapa`.`tipo_usuario` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `tipo` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `base_chapa`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `base_chapa`.`usuario` (
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `enabled` TINYINT(1) NOT NULL,
  `id_cliente` INT NOT NULL,
  `id_tipousuario` INT NOT NULL,
  PRIMARY KEY (`username`),
  INDEX `fk_usuario_cliente_idx` (`id_cliente` ASC),
  INDEX `fk_usuario_tipo_usuario1_idx` (`id_tipousuario` ASC),
  CONSTRAINT `fk_usuario_cliente`
    FOREIGN KEY (`id_cliente`)
    REFERENCES `base_chapa`.`cliente` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_usuario_tipo_usuario1`
    FOREIGN KEY (`id_tipousuario`)
    REFERENCES `base_chapa`.`tipo_usuario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `base_chapa`.`historico`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `base_chapa`.`historico` (
  `id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `fecha` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `base_chapa`.`tipo_usuario`
-- -----------------------------------------------------
START TRANSACTION;
USE `base_chapa`;
INSERT INTO `base_chapa`.`tipo_usuario` (`id`, `tipo`) VALUES (1, 'ADMIN');
INSERT INTO `base_chapa`.`tipo_usuario` (`id`, `tipo`) VALUES (2, 'SUPERVISOR');
INSERT INTO `base_chapa`.`tipo_usuario` (`id`, `tipo`) VALUES (3, 'USUARIO');

COMMIT;

