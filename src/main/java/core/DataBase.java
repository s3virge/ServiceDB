package core;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class DataBase {
    private static final Logger logger = Logger.getLogger(DataBase.class);

    // JDBC URL, username and dbPassword of MySQL server
    private static final String dbUrl;
    private static final String dbUser;
    private static final String dbPassword;
    private static final String dbHost;
    private static final String dbPort;
    private static final String dbName;

    static {
        ResourceBundle properties = ResourceBundle.getBundle("dataBase");
        //String dbHost = properties.getString("dataBase.dbHost");
        dbHost = properties.getString("dataBase.host");
        dbPort = properties.getString("dataBase.port");
        //String dbName = properties.getString("dataBase.name");
        dbName = properties.getString("dataBase.name");

        dbUrl = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;

        dbUser = properties.getString("dataBase.login");
        dbPassword = properties.getString("dataBase.password");
    }

    /** существует ли база */
    public boolean isExist() {
        boolean result = true; //база существует
        String serverUrl = "jdbc:mysql://" + dbHost + ":" + dbPort;

        //когда базы нет вываливается исключение
        try (Connection con = DriverManager.getConnection(serverUrl, dbUser, dbPassword);
             Statement st = con.createStatement()) {
            st.execute("USE  " + dbName);
        }
        catch (Exception ex) {
            logger.error(ex);
            result = false;
        }

        return result;
    }

    /** возвращает пустого User если в базе нет пользователя с логином strLogin */
    public User getUser(String strLogin) {
        logger.debug("DataBase.getUser() is executed.\n     Try to connect to DB server");

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement()) {
            //stmt.execute("USE  " + dbName);
            ResultSet rs = stmt.executeQuery("select * from user where login='" + strLogin + "';");

            User user = new User();

            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));

                //System.out.printf("id: %d; login: %s; dbPassword: %s;\n", id, login, password);
            }

            return user;
        } catch (Exception exception) {
            logger.error(exception);
            throw new RuntimeException("error in DataBase.getUser()");
        }
    }

    /** создать базу и все таблицы которые будем использовать */
    public void createDB() {
        logger.debug("Launch createDB()");

        String serverUrl = "jdbc:mysql://" + dbHost + ":" + dbPort;
        // Параметры соединения с базой
        Properties connProp = new Properties();

        connProp.put("user", dbUser);
        connProp.put("password", dbPassword);

        connProp.put("useUnicode", "true");
        connProp.put("characterEncoding","Cp1251");

        try (Connection con = DriverManager.getConnection(serverUrl, connProp);
             Statement statement = con.createStatement()) {
            statement.execute("SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;");
            statement.execute("SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;");
            statement.execute("SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';");

            statement.execute("CREATE SCHEMA IF NOT EXISTS `" + dbName + "` DEFAULT CHARACTER SET utf8 ;");
            statement.execute("USE `" + dbName + "` ;");

            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`user_group` (" +
                    "`id` INT NOT NULL AUTO_INCREMENT, " +
                    "`name` VARCHAR(45) NOT NULL, " +
                    "PRIMARY KEY (`id`), " +
                    "UNIQUE INDEX `name_UNIQUE` (`name` ASC)) " +
                    "ENGINE = InnoDB " +
                    "DEFAULT CHARACTER SET = utf8;");

            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`user` (" +
                    "`id` INT NOT NULL AUTO_INCREMENT," +
                    "`login` VARCHAR(45) NOT NULL," +
                    "`password` VARCHAR(45) NOT NULL," +
                    "`user_group` INT NOT NULL," +
                    "PRIMARY KEY (`id`)," +
                    "INDEX `user_group_idx` (`user_group` ASC)," +
                    "UNIQUE INDEX `login_UNIQUE` (`login` ASC)," +
                    "CONSTRAINT `user_group` " +
                    "FOREIGN KEY (`user_group`) " +
                    "REFERENCES `" + dbName + "`.`user_group` (`id`)  " +
                    "ON DELETE NO ACTION " +
                    "ON UPDATE NO ACTION) " +
                    "ENGINE = InnoDB; ");

            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`name` (" +
                    "`id` INT NOT NULL AUTO_INCREMENT, " +
                    "`value` VARCHAR(45) NOT NULL, " +
                    "PRIMARY KEY (`id`), " +
                    "UNIQUE INDEX `name_UNIQUE` (`value` ASC), " +
                    "UNIQUE INDEX `id_UNIQUE` (`id` ASC)) " +
                    "ENGINE = InnoDB " +
                    "DEFAULT CHARACTER SET = utf8 " +
                    "COMMENT = 'имена пользователей';");

            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`patronymic` (" +
                    "`id` INT NOT NULL AUTO_INCREMENT, " +
                    "`value` VARCHAR(45) NOT NULL, " +
                    "PRIMARY KEY (`id`), " +
                    "UNIQUE INDEX `value_UNIQUE` (`value` ASC)) " +
                    "ENGINE = InnoDB " +
                    "DEFAULT CHARACTER SET = utf8 " +
                    "COMMENT = 'отчества'; ");

            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`surname` (" +
                    "`id` INT NOT NULL AUTO_INCREMENT," +
                    "`value` VARCHAR(45) NOT NULL," +
                    "PRIMARY KEY (`id`)," +
                    "UNIQUE INDEX `value_UNIQUE` (`value` ASC)) " +
                    "ENGINE = InnoDB " +
                    "DEFAULT CHARACTER SET = utf8 " +
                    "COMMENT = 'фамилии тут';");

            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`owner` (" +
                    "`id` INT NOT NULL AUTO_INCREMENT," +
                    "`surname_id` INT NOT NULL," +
                    "`name_id` INT NOT NULL," +
                    "`patronymic_id` INT NOT NULL," +
                    "`telephone_number` VARCHAR(15) NOT NULL," +
                    "PRIMARY KEY (`id`)," +
                    "INDEX `owner_name_idx` (`name_id` ASC)," +
                    "INDEX `owner_patronymic_idx` (`patronymic_id` ASC)," +
                    "INDEX `owner_surname_idx` (`surname_id` ASC)," +
                    "CONSTRAINT `owner_name` " +
                    "FOREIGN KEY (`name_id`) " +
                    "REFERENCES `" + dbName + "`.`name` (`id`) " +
                    "ON DELETE NO ACTION " +
                    "ON UPDATE NO ACTION," +
                    "CONSTRAINT `owner_patronymic` " +
                    "FOREIGN KEY (`patronymic_id`) " +
                    "REFERENCES `" + dbName + "`.`patronymic` (`id`) " +
                    "ON DELETE NO ACTION " +
                    "ON UPDATE NO ACTION," +
                    "CONSTRAINT `owner_surname` " +
                    "FOREIGN KEY (`surname_id`) " +
                    "REFERENCES `" + dbName + "`.`surname` (`id`) " +
                    "ON DELETE NO ACTION " +
                    "ON UPDATE NO ACTION) " +
                    "ENGINE = InnoDB " +
                    "DEFAULT CHARACTER SET = utf8 " +
                    "COMMENT = 'информация о пользователе';");

            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`brand` (" +
                    "`id` INT NOT NULL AUTO_INCREMENT," +
                    "`name` VARCHAR(45) NOT NULL," +
                    "PRIMARY KEY (`id`)," +
                    "UNIQUE INDEX `name_UNIQUE` (`name` ASC)) " +
                    "ENGINE = InnoDB " +
                    "DEFAULT CHARACTER SET = utf8;");

            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`type` (" +
                    "`id` INT NOT NULL AUTO_INCREMENT," +
                    "`name` VARCHAR(45) NOT NULL," +
                    "PRIMARY KEY (`id`)," +
                    "UNIQUE INDEX `name_UNIQUE` (`name` ASC)) " +
                    "ENGINE = InnoDB " +
                    "DEFAULT CHARACTER SET = utf8 " +
                    "COMMENT = 'тип устройства';");

            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`model` (" +
                    "`id` INT NOT NULL AUTO_INCREMENT," +
                    "`name` VARCHAR(45) NOT NULL," +
                    "PRIMARY KEY (`id`)," +
                    "UNIQUE INDEX `name_UNIQUE` (`name` ASC))" +
                    "ENGINE = InnoDB " +
                    "DEFAULT CHARACTER SET = utf8 " +
                    "COMMENT = 'таблица для хранения модели устройства';");

            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`defect` (" +
                    "`id` INT NOT NULL AUTO_INCREMENT," +
                    "`description` VARCHAR(500) NOT NULL," +
                    "PRIMARY KEY (`id`)," +
                    "UNIQUE INDEX `description_UNIQUE` (`description` ASC))" +
                    "ENGINE = InnoDB " +
                    "DEFAULT CHARACTER SET = utf8 " +
                    "COMMENT = 'неисправности';");

            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`status` (" +
                    "`id` INT NOT NULL AUTO_INCREMENT," +
                    "`value` VARCHAR(45) NOT NULL," +
                    "PRIMARY KEY (`id`)," +
                    "UNIQUE INDEX `value_UNIQUE` (`value` ASC))" +
                    "ENGINE = InnoDB " +
                    "DEFAULT CHARACTER SET = utf8 " +
                    "COMMENT = 'этап ремонта';");

            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`repair` (" +
                    "`id` INT NOT NULL AUTO_INCREMENT," +
                    "`master_comments` VARCHAR(1024) NULL," +
                    "`diagnostic_result` VARCHAR(1024) NULL," +
                    "`repair_result` VARCHAR(450) NULL," +
                    "`status_id` INT NOT NULL," +
                    "`date_of_accept` DATETIME NULL," +
                    "`date_of_issue` DATETIME NULL," +
                    "PRIMARY KEY (`id`)," +
                    "INDEX `repair_status_idx` (`status_id` ASC)," +
                    "CONSTRAINT `repair_status`" +
                    "FOREIGN KEY (`status_id`)" +
                    "REFERENCES `" + dbName + "`.`status` (`id`)" +
                    "ON DELETE NO ACTION " +
                    "ON UPDATE NO ACTION)" +
                    "ENGINE = InnoDB " +
                    "DEFAULT CHARACTER SET = utf8 " +
                    "COMMENT = 'таблица для результатов диагностики-ремонта';");

            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`device` (" +
                    "`id` INT NOT NULL AUTO_INCREMENT," +
                    "`type_id` INT NOT NULL," +
                    "`brand_id` INT NOT NULL," +
                    "`model_id` INT NOT NULL," +
                    "`serial_number` VARCHAR(45) NULL," +
                    "`defect_id` INT NOT NULL," +
                    "`owner_id` INT NOT NULL," +
                    "`repair_id` INT NOT NULL," +
                    "PRIMARY KEY (`id`)," +
                    "INDEX `owner_idx` (`owner_id` ASC)," +
                    "INDEX `brand_idx` (`brand_id` ASC)," +
                    "INDEX `type_idx` (`type_id` ASC)," +
                    "INDEX `model_idx` (`model_id` ASC)," +
                    "INDEX `defect_idx` (`defect_id` ASC)," +
                    "INDEX `device_repair_idx` (`repair_id` ASC)," +
                    "CONSTRAINT `device_owner` " +
                    "FOREIGN KEY (`owner_id`) " +
                    "REFERENCES `" + dbName + "`.`owner` (`id`) " +
                    "ON DELETE NO ACTION " +
                    "ON UPDATE NO ACTION," +
                    "CONSTRAINT `device_brand` " +
                    "FOREIGN KEY (`brand_id`) " +
                    "REFERENCES `" + dbName + "`.`brand` (`id`) " +
                    "ON DELETE NO ACTION " +
                    "ON UPDATE NO ACTION, " +
                    "CONSTRAINT `device_type` " +
                    "FOREIGN KEY (`type_id`) " +
                    "REFERENCES `" + dbName + "`.`type` (`id`) " +
                    "ON DELETE NO ACTION " +
                    "ON UPDATE NO ACTION," +
                    "CONSTRAINT `device_model` " +
                    "FOREIGN KEY (`model_id`) " +
                    "REFERENCES `" + dbName + "`.`model` (`id`) " +
                    "ON DELETE NO ACTION " +
                    "ON UPDATE NO ACTION, " +
                    "CONSTRAINT `device_defect` " +
                    "FOREIGN KEY (`defect_id`) " +
                    "REFERENCES `" + dbName + "`.`defect` (`id`) " +
                    "ON DELETE NO ACTION " +
                    "ON UPDATE NO ACTION," +
                    "CONSTRAINT `device_repair` " +
                    "FOREIGN KEY (`repair_id`) " +
                    "REFERENCES `" + dbName + "`.`repair` (`id`) " +
                    "ON DELETE NO ACTION " +
                    "ON UPDATE NO ACTION) " +
                    "ENGINE = InnoDB " +
                    "DEFAULT CHARACTER SET = utf8;");

            statement.execute("SET SQL_MODE=@OLD_SQL_MODE;");
            statement.execute("SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;");
            statement.execute("SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;");

            statement.execute("START TRANSACTION;");
            statement.execute("USE `" + dbName + "`;");
            statement.execute("INSERT INTO `" + dbName + "`.`user_group` (`id`, `name`) VALUES (1, 'administrator');");
            statement.execute("INSERT INTO `" + dbName + "`.`user_group` (`id`, `name`) VALUES (2, 'manager');");
            statement.execute("INSERT INTO `" + dbName + "`.`user_group` (`id`, `name`) VALUES (3, 'master');");
            statement.execute("INSERT INTO `" + dbName + "`.`user_group` (`id`, `name`) VALUES (4, 'acceptor');");

            statement.execute("COMMIT;");

            statement.execute("START TRANSACTION;");
            statement.execute("USE `" + dbName + "`;");
            statement.execute("INSERT INTO `" + dbName + "`.`user` (`id`, `login`, `password`, `user_group`) VALUES (1, 'admin', '21232f297a57a5a743894a0e4a801fc3', 1);");

            statement.execute("COMMIT;");

            statement.execute("START TRANSACTION;");
            statement.execute("USE `" + dbName + "`;");
            statement.execute("INSERT INTO `" + dbName + "`.`status` (`id`, `value`) VALUES (1, 'Оформлен');");
            statement.execute("INSERT INTO `" + dbName + "`.`status` (`id`, `value`) VALUES (2, 'Диагностика');");
            statement.execute("INSERT INTO `" + dbName + "`.`status` (`id`, `value`) VALUES (3, 'Ожидание комплектующих');");
            statement.execute("INSERT INTO `" + dbName + "`.`status` (`id`, `value`) VALUES (4, 'Ремонт');");
            statement.execute("INSERT INTO `" + dbName + "`.`status` (`id`, `value`) VALUES (5, 'Выдано');");

            statement.execute("COMMIT;");
        }
        catch (SQLException sqlExep) {
            //что-то пошло не так...
            removeDB();

            MsgBox.show(sqlExep.getMessage(), MsgBox.Type.MB_ERROR);
            //System.exit(0);
            throw new RuntimeException("Облом с созданием базы данных в createDB()");
        }

        logger.debug("createDB() successfully completed");
    }

    private void removeDB() {
        String servUrl = "jdbc:mysql://" + dbHost + ":" + dbPort;

        try (Connection con = DriverManager.getConnection(servUrl, dbUser, dbPassword);
             Statement st = con.createStatement()) {
            st.execute("drop schema `" + dbName + "`;");
        }
        catch (Exception ex) {
            logger.error(ex);
        }
    }
}