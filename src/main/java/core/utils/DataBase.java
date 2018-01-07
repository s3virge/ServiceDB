package core.utils;

import core.models.User;
import core.models.UserGroup;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;

public class DataBase {
    private static final Logger logger = Logger.getLogger(DataBase.class);

    // JDBC URL, username and dbPassword of MySQL server
    private static final String dbUrl;
    private static final String dbUser;
    private static final String dbPassword;
    private static final String dbHost;
    private static final String dbPort;
    private static final String dbName;

    //init static variables
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

    // init connection object
    private Connection connection;
    // init properties object
    private Properties properties;

    // create properties
    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", dbUser);
            properties.setProperty("password", dbPassword);

            properties.put("useUnicode", "true");
            properties.put("characterEncoding","Cp1251");
        }
        return properties;
    }

    /**
     * @return
     * возвращает connection
     */
    // connect database
    public Connection connect() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(dbUrl, getProperties());
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    // disconnect database
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /** @return
     * возвращает true если база данных уже создана, иначе false
     * */
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

    /**
     * @return
     * возвращает пустого User если в базе нет пользователя с логином strLogin
     */
    public User getUser(String strLogin) {
        logger.debug("DataBase.getUser() is executed.\n     Try to connect to DB server");

        User user = new User();

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement()) {
            /*
            выбрать id, login. password, value
            из user внутренне соединенной с user_group через user.user_group с user_group.id
            где login = admin
            * */
            ResultSet rs = stmt.executeQuery("SELECT user.id, user.login, user.password, user_group.value " +
                    "FROM user INNER JOIN user_group ON user.user_group = user_group.id " +
                    "where user.login='" + strLogin + "';");

            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                //получить значение столбца value из связанной таблицы
                user.setGroup(rs.getString("value"));

                //System.out.printf("id: %d; login: %s; dbPassword: %s;\n", id, login, password);
            }
        }
        catch (Exception exception) {
            logger.error(exception);
            System.exit(0);
        }

        return user;
    }

    /**
     * создать базу и все таблицы которые будем использовать
     */
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
             /*Statement statement = con.createStatement()*/) {

            ScriptRunner runner = new ScriptRunner(con, false, false);
            String file = "src/Resources/sql/createbd.sql";
            try {
                runner.runScript(new BufferedReader(new FileReader(file)));
            }
            catch (IOException fileException) {
                fileException.getMessage();
            }
//
//            statement.execute("SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;");
//            statement.execute("SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;");
//            statement.execute("SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';");
//
//            statement.execute("CREATE SCHEMA IF NOT EXISTS `" + dbName + "` DEFAULT CHARACTER SET utf8 ;");
//            statement.execute("USE `" + dbName + "` ;");
//
//            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`user_group` (" +
//                    "`id` INT NOT NULL AUTO_INCREMENT, " +
//                    "`value` VARCHAR(45) NOT NULL, " +
//                    "PRIMARY KEY (`id`), " +
//                    "UNIQUE INDEX `value_UNIQUE` (`value` ASC)) " +
//                    "ENGINE = InnoDB " +
//                    "DEFAULT CHARACTER SET = utf8;");
//
//            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`user` (" +
//                    "`id` INT NOT NULL AUTO_INCREMENT," +
//                    "`login` VARCHAR(45) NOT NULL," +
//                    "`password` VARCHAR(45) NOT NULL," +
//                    "`user_group` INT NOT NULL," +
//                    "PRIMARY KEY (`id`)," +
//                    "INDEX `user_group_idx` (`user_group` ASC)," +
//                    "UNIQUE INDEX `login_UNIQUE` (`login` ASC)," +
//                    "CONSTRAINT `user_group` " +
//                    "FOREIGN KEY (`user_group`) " +
//                    "REFERENCES `" + dbName + "`.`user_group` (`id`)  " +
//                    "ON DELETE NO ACTION " +
//                    "ON UPDATE NO ACTION) " +
//                    "ENGINE = InnoDB; ");
//
//            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`value` (" +
//                    "`id` INT NOT NULL AUTO_INCREMENT, " +
//                    "`value` VARCHAR(45) NOT NULL, " +
//                    "PRIMARY KEY (`id`), " +
//                    "UNIQUE INDEX `value_UNIQUE` (`value` ASC), " +
//                    "UNIQUE INDEX `id_UNIQUE` (`id` ASC)) " +
//                    "ENGINE = InnoDB " +
//                    "DEFAULT CHARACTER SET = utf8 " +
//                    "COMMENT = 'имена пользователей';");
//
//            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`patronymic` (" +
//                    "`id` INT NOT NULL AUTO_INCREMENT, " +
//                    "`value` VARCHAR(45) NOT NULL, " +
//                    "PRIMARY KEY (`id`), " +
//                    "UNIQUE INDEX `value_UNIQUE` (`value` ASC)) " +
//                    "ENGINE = InnoDB " +
//                    "DEFAULT CHARACTER SET = utf8 " +
//                    "COMMENT = 'отчества'; ");
//
//            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`surname` (" +
//                    "`id` INT NOT NULL AUTO_INCREMENT," +
//                    "`value` VARCHAR(45) NOT NULL," +
//                    "PRIMARY KEY (`id`)," +
//                    "UNIQUE INDEX `value_UNIQUE` (`value` ASC)) " +
//                    "ENGINE = InnoDB " +
//                    "DEFAULT CHARACTER SET = utf8 " +
//                    "COMMENT = 'фамилии тут';");
//
//            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`owner` (" +
//                    "`id` INT NOT NULL AUTO_INCREMENT," +
//                    "`surname_id` INT NOT NULL," +
//                    "`name_id` INT NOT NULL," +
//                    "`patronymic_id` INT NOT NULL," +
//                    "`telephone_number` VARCHAR(15) NOT NULL," +
//                    "PRIMARY KEY (`id`)," +
//                    "INDEX `owner_name_idx` (`name_id` ASC)," +
//                    "INDEX `owner_patronymic_idx` (`patronymic_id` ASC)," +
//                    "INDEX `owner_surname_idx` (`surname_id` ASC)," +
//                    "CONSTRAINT `owner_name` " +
//                    "FOREIGN KEY (`name_id`) " +
//                    "REFERENCES `" + dbName + "`.`value` (`id`) " +
//                    "ON DELETE NO ACTION " +
//                    "ON UPDATE NO ACTION," +
//                    "CONSTRAINT `owner_patronymic` " +
//                    "FOREIGN KEY (`patronymic_id`) " +
//                    "REFERENCES `" + dbName + "`.`patronymic` (`id`) " +
//                    "ON DELETE NO ACTION " +
//                    "ON UPDATE NO ACTION," +
//                    "CONSTRAINT `owner_surname` " +
//                    "FOREIGN KEY (`surname_id`) " +
//                    "REFERENCES `" + dbName + "`.`surname` (`id`) " +
//                    "ON DELETE NO ACTION " +
//                    "ON UPDATE NO ACTION) " +
//                    "ENGINE = InnoDB " +
//                    "DEFAULT CHARACTER SET = utf8 " +
//                    "COMMENT = 'информация о пользователе';");
//
//            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`brand` (" +
//                    "`id` INT NOT NULL AUTO_INCREMENT," +
//                    "`value` VARCHAR(45) NOT NULL," +
//                    "PRIMARY KEY (`id`)," +
//                    "UNIQUE INDEX `value_UNIQUE` (`value` ASC)) " +
//                    "ENGINE = InnoDB " +
//                    "DEFAULT CHARACTER SET = utf8;");
//
//            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`devicetype` (" +
//                    "`id` INT NOT NULL AUTO_INCREMENT," +
//                    "`value` VARCHAR(45) NOT NULL," +
//                    "PRIMARY KEY (`id`)," +
//                    "UNIQUE INDEX `value_UNIQUE` (`value` ASC)) " +
//                    "ENGINE = InnoDB " +
//                    "DEFAULT CHARACTER SET = utf8 " +
//                    "COMMENT = 'тип устройства';");
//
//            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`devicemodel` (" +
//                    "`id` INT NOT NULL AUTO_INCREMENT," +
//                    "`value` VARCHAR(45) NOT NULL," +
//                    "PRIMARY KEY (`id`)," +
//                    "UNIQUE INDEX `value_UNIQUE` (`value` ASC))" +
//                    "ENGINE = InnoDB " +
//                    "DEFAULT CHARACTER SET = utf8 " +
//                    "COMMENT = 'таблица для хранения модели устройства';");
//
//            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`defect` (" +
//                    "`id` INT NOT NULL AUTO_INCREMENT," +
//                    "`description` VARCHAR(500) NOT NULL," +
//                    "PRIMARY KEY (`id`)," +
//                    "UNIQUE INDEX `description_UNIQUE` (`description` ASC))" +
//                    "ENGINE = InnoDB " +
//                    "DEFAULT CHARACTER SET = utf8 " +
//                    "COMMENT = 'неисправности';");
//
//            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`status` (" +
//                    "`id` INT NOT NULL AUTO_INCREMENT," +
//                    "`value` VARCHAR(45) NOT NULL," +
//                    "PRIMARY KEY (`id`)," +
//                    "UNIQUE INDEX `value_UNIQUE` (`value` ASC))" +
//                    "ENGINE = InnoDB " +
//                    "DEFAULT CHARACTER SET = utf8 " +
//                    "COMMENT = 'этап ремонта';");
//
//            //-- -----------------------------------------------------
//            //-- Table `servicedb`.`repair`\n" +
//            //-- -----------------------------------------------------
//            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`repair` (\n" +
//                    "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
//                    "  `acceptor_id` INT NOT NULL,\n" +
//                    "  `master_id` INT NOT NULL,\n" +
//                    "  `master_comments` VARCHAR(1024) NULL,\n" +
//                    "  `diagnostic_result` VARCHAR(1024) NULL,\n" +
//                    "  `repair_result` VARCHAR(450) NULL,\n" +
//                    "  `status_id` INT NOT NULL,\n" +
//                    "  `date_of_accept` DATETIME NOT NULL,\n" +
//                    "  `date_of_give_out` DATETIME NOT NULL,\n" +
//                    "  PRIMARY KEY (`id`),\n" +
//                    "  INDEX `repair_status_idx` (`status_id` ASC),\n" +
//                    "  INDEX `repair_acceptor_idx` (`acceptor_id` ASC),\n" +
//                    "  INDEX `repair_master_idx` (`master_id` ASC),\n" +
//                    "  CONSTRAINT `repair_status`\n" +
//                    "    FOREIGN KEY (`status_id`)\n" +
//                    "    REFERENCES `" + dbName + "`.`status` (`id`)\n" +
//                    "    ON DELETE NO ACTION\n" +
//                    "    ON UPDATE NO ACTION,\n" +
//                    "  CONSTRAINT `repair_acceptor`\n" +
//                    "    FOREIGN KEY (`acceptor_id`)\n" +
//                    "    REFERENCES `" + dbName + "`.`user` (`id`)\n" +
//                    "    ON DELETE NO ACTION\n" +
//                    "    ON UPDATE NO ACTION,\n" +
//                    "  CONSTRAINT `repair_master`\n" +
//                    "    FOREIGN KEY (`master_id`)\n" +
//                    "    REFERENCES `" + dbName + "`.`user` (`id`)\n" +
//                    "    ON DELETE NO ACTION\n" +
//                    "    ON UPDATE NO ACTION)\n" +
//                    "ENGINE = InnoDB\n" +
//                    "DEFAULT CHARACTER SET = utf8\n" +
//                    "COMMENT = 'таблица для результатов диагностики-ремонта ';\n");
//
//            //-- -----------------------------------------------------
//            //        -- Table `servicedb`.`completeness`
//            //-- -----------------------------------------------------
//            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`completeness` (\n" +
//                "`id` INT NOT NULL AUTO_INCREMENT,\n" +
//                "`value` VARCHAR(255) NOT NULL,\n" +
//                "PRIMARY KEY (`id`),\n" +
//                "UNIQUE INDEX `id_UNIQUE` (`id` ASC),\n" +
//                "UNIQUE INDEX `value_UNIQUE` (`value` ASC))\n" +
//                "ENGINE = InnoDB\n" +
//                "DEFAULT CHARACTER SET = utf8\n" +
//                "COMMENT = 'комплектация устройства'");
//
//            statement.execute("CREATE TABLE IF NOT EXISTS `" + dbName + "`.`device` (" +
//                    "`id` INT NOT NULL AUTO_INCREMENT," +
//                    "`type_id` INT NOT NULL," +
//                    "`brand_id` INT NOT NULL," +
//                    "`model_id` INT NOT NULL," +
//                    "`serial_number` VARCHAR(45) NULL," +
//                    "`defect_id` INT NOT NULL," +
//                    "`owner_id` INT NOT NULL," +
//                    "`repair_id` INT NOT NULL," +
//                    "`completeness_id` INT NOT NULL," +
//                    "PRIMARY KEY (`id`)," +
//                    "INDEX `owner_idx` (`owner_id` ASC)," +
//                    "INDEX `brand_idx` (`brand_id` ASC)," +
//                    "INDEX `type_idx` (`type_id` ASC)," +
//                    "INDEX `model_idx` (`model_id` ASC)," +
//                    "INDEX `defect_idx` (`defect_id` ASC)," +
//                    "INDEX `device_repair_idx` (`repair_id` ASC)," +
//                    "INDEX `device_completeness_idx` (`completeness_id` ASC)," +
//                    "CONSTRAINT `device_owner` " +
//                    "FOREIGN KEY (`owner_id`) " +
//                    "REFERENCES `" + dbName + "`.`owner` (`id`) " +
//                    "ON DELETE NO ACTION " +
//                    "ON UPDATE NO ACTION," +
//                    "CONSTRAINT `device_brand` " +
//                    "FOREIGN KEY (`brand_id`) " +
//                    "REFERENCES `" + dbName + "`.`brand` (`id`) " +
//                    "ON DELETE NO ACTION " +
//                    "ON UPDATE NO ACTION, " +
//                    "CONSTRAINT `device_type` " +
//                    "FOREIGN KEY (`type_id`) " +
//                    "REFERENCES `" + dbName + "`.`devicetype` (`id`) " +
//                    "ON DELETE NO ACTION " +
//                    "ON UPDATE NO ACTION," +
//                    "CONSTRAINT `device_model` " +
//                    "FOREIGN KEY (`model_id`) " +
//                    "REFERENCES `" + dbName + "`.`devicemodel` (`id`) " +
//                    "ON DELETE NO ACTION " +
//                    "ON UPDATE NO ACTION, " +
//                    "CONSTRAINT `device_defect` " +
//                    "FOREIGN KEY (`defect_id`) " +
//                    "REFERENCES `" + dbName + "`.`defect` (`id`) " +
//                    "ON DELETE NO ACTION " +
//                    "ON UPDATE NO ACTION," +
//                    "CONSTRAINT `device_repair` " +
//                    "FOREIGN KEY (`repair_id`) " +
//                    "REFERENCES `" + dbName + "`.`repair` (`id`) " +
//                    "ON DELETE NO ACTION " +
//                    "ON UPDATE NO ACTION,\n" +
//                    "CONSTRAINT `device_completeness`\n" +
//                    "FOREIGN KEY (`completeness_id`)\n" +
//                    "REFERENCES `" + dbName + "`.`completeness` (`id`)\n" +
//                    "ON DELETE NO ACTION\n" +
//                    "ON UPDATE NO ACTION)" +
//                    "ENGINE = InnoDB " +
//                    "DEFAULT CHARACTER SET = utf8;");
//
//            statement.execute("SET SQL_MODE=@OLD_SQL_MODE;");
//            statement.execute("SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;");
//            statement.execute("SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;");
//
//            statement.execute("START TRANSACTION;");
//            statement.execute("USE `" + dbName + "`;");
//            statement.execute("INSERT INTO `" + dbName + "`.`user_group` (`id`, `value`) VALUES (1, '" + UserGroup.ADMIN + "');");
//            statement.execute("INSERT INTO `" + dbName + "`.`user_group` (`id`, `value`) VALUES (2, '" + UserGroup.MANAGER + "');");
//            statement.execute("INSERT INTO `" + dbName + "`.`user_group` (`id`, `value`) VALUES (3, '" + UserGroup.MASTER + "');");
//            statement.execute("INSERT INTO `" + dbName + "`.`user_group` (`id`, `value`) VALUES (4, '" + UserGroup.ACCEPTOR + "');");
//
//            statement.execute("COMMIT;");
//
//            statement.execute("START TRANSACTION;");
//            statement.execute("USE `" + dbName + "`;");
//            statement.execute("INSERT INTO `" + dbName + "`.`user` (`id`, `login`, `password`, `user_group`) VALUES (1, 'admin', '21232f297a57a5a743894a0e4a801fc3', 1);");
//
//            statement.execute("COMMIT;");
//
//            statement.execute("START TRANSACTION;");
//            statement.execute("USE `" + dbName + "`;");
//            statement.execute("INSERT INTO `" + dbName + "`.`status` (`id`, `value`) VALUES (1, 'Оформлен');");
//            statement.execute("INSERT INTO `" + dbName + "`.`status` (`id`, `value`) VALUES (2, 'Диагностика');");
//            statement.execute("INSERT INTO `" + dbName + "`.`status` (`id`, `value`) VALUES (3, 'Ожидание комплектующих');");
//            statement.execute("INSERT INTO `" + dbName + "`.`status` (`id`, `value`) VALUES (4, 'Ремонт');");
//            statement.execute("INSERT INTO `" + dbName + "`.`status` (`id`, `value`) VALUES (5, 'Выдано');");
//
//            statement.execute("COMMIT;");
        }
        catch (SQLException sqlExcep) {
            MsgBox.show("Облом в createDB() -> " + sqlExcep.getMessage(), MsgBox.Type.MB_ERROR);

            //что-то пошло не так...
            removeDB();

            System.exit(0);
            //throw new RuntimeException("Облом с созданием базы данных в createDB()");
        }

        logger.debug("createDB() successfully completed");
    }

    /**
     * удалить буза
     */
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