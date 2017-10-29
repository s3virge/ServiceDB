package core;

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ResourceBundle;

public class DataBase {
    private static final Logger logger = Logger.getLogger(DataBase.class);

    // JDBC URL, username and password of MySQL server
    private static final String url;
    private static final String user;
    private static final String password;
    private static final String host;
    private static final String port;
    private static final String dbName;

    static {
        ResourceBundle properties = ResourceBundle.getBundle("dataBase");
        //String host = properties.getString("dataBase.host");
        host = properties.getString("dataBase.host");
        port = properties.getString("dataBase.port");
        //String dbName = properties.getString("dataBase.name");
        dbName = properties.getString("dataBase.name");

        //url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
        url = "jdbc:mysql://" + host + ":" + port;
        user = properties.getString("dataBase.login");
        password = properties.getString("dataBase.password");
    }

    // JDBC variables for opening and managing connection
    private static Connection conn;
    private static Statement stmt;
    private static ResultSet rs;

    //а что если подключаться не к базе данных а к mysql
    //и проверять существует ли базе
    //нужно инициализировать статические поля класса
    public static void initialise() {
        logger.debug("DataBase.initialise() is executed.\n     Try to connect to DB server");

        try{
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.createStatement();
            stmt.execute("USE  " + dbName);

            rs = stmt.executeQuery("SELECT * FROM user;");

            while (rs.next()) {
                String count = rs.getString(2);
                System.out.println(count);
            }

            rs.close();
            stmt.close();
            conn.close();
        }
        catch (Exception exception){
            logger.error(exception);
            System.exit(0);
        }
    }

    //создать базу и все таблицы которые будем использовать
    public static void initialise1() {
        logger.debug("DataBase.initialise() is executed!");

        try {
            //пытаемся подключиться к базе данных
            conn = DriverManager.getConnection(url, user, password);
        }
        catch(Exception connEx) {
            logger.debug("Облом с подключением к " + dbName );
            logger.error(connEx);
            createDB();
        }

        try {
        	if (conn != null)
        	    conn.close();
        }
        catch (SQLException sqlEx) {
        	logger.error(sqlEx);
        	System.exit(0);
        }
    }

    private static void createDB() {
		logger.debug("Launch createDB()");
        String serverUrl = "jdbc:mysql://" + host + ":" + port;

        try {
            Connection con = DriverManager.getConnection(serverUrl, user, password);
            Statement statement = con.createStatement();

            statement.executeUpdate("CREATE SCHEMA IF NOT EXISTS " + dbName + " DEFAULT CHARACTER SET utf8");
            statement.executeUpdate( "USE " + dbName + "");

            /*-- -----------------------------------------------------
                    -- Table `servicedb`.`user_group`
            -- -----------------------------------------------------*/
            statement.executeUpdate(
            		"CREATE TABLE IF NOT EXISTS " + dbName + ".`user_group` " +
                        "(`id` INT NOT NULL AUTO_INCREMENT, " +
                        "`name` VARCHAR(45) NOT NULL, " +
                        "PRIMARY KEY (`id`), " +
                        "UNIQUE INDEX `name_UNIQUE` (`name` ASC)) " +
                        "ENGINE = InnoDB " +
                        "DEFAULT CHARACTER SET = utf8;");


            /*-- -----------------------------------------------------
                    -- Table `servicedb`.`user`
            -- -----------------------------------------------------*/
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS " + dbName + ".`user` " +
                            "(`id` INT NOT NULL AUTO_INCREMENT, " +
                            "`login` VARCHAR(45) NOT NULL, " +
                            "`password` VARCHAR(45) NOT NULL, " +
                            "`user_group` INT NOT NULL, " +
                            "PRIMARY KEY (`id`), " +
                            "INDEX `user_group_idx` (`user_group` ASC), " +
                            "UNIQUE INDEX `login_UNIQUE` (`login` ASC), " +
                            "CONSTRAINT `user_group` " +
                            "FOREIGN KEY (`user_group`) " +
                            "REFERENCES " + dbName + ".`user_group` (`id`)" +
                            "ON DELETE NO ACTION " +
                            "ON UPDATE NO ACTION) " +
                            "ENGINE = InnoDB;");


            /*-- -----------------------------------------------------
                    -- Table `servicedb`.`name`
            -- -----------------------------------------------------*/
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS " + dbName + ".`name` ( " +
                            "`id` INT NOT NULL AUTO_INCREMENT, " +
                            "`value` VARCHAR(45) NOT NULL, " +
                            "PRIMARY KEY (`id`), " +
                            "UNIQUE INDEX `name_UNIQUE` (`value` ASC), " +
                            "UNIQUE INDEX `id_UNIQUE` (`id` ASC)) " +
                            "ENGINE = InnoDB " +
                            "DEFAULT CHARACTER SET = utf8 " +
                            "COMMENT = 'имена пользователей';" );


            /*-- -----------------------------------------------------
                    -- Table `servicedb`.`patronymic`
            -- -----------------------------------------------------*/
                    statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS " + dbName + ".`patronymic` ( " +
					"`id` INT NOT NULL AUTO_INCREMENT, " +
					"`value` VARCHAR(45) NOT NULL, " +
            "PRIMARY KEY (`id`), " +
            "UNIQUE INDEX `value_UNIQUE` (`value` ASC)) " +
            "ENGINE = InnoDB " +
            "DEFAULT CHARACTER SET = utf8 " +
            "COMMENT = 'отчества'; ");


            /*-- -----------------------------------------------------
                    -- Table `servicedb`.`surname`
            -- -----------------------------------------------------*/
                    statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS " + dbName + ".`surname` ( " +
					"`id` INT NOT NULL AUTO_INCREMENT, " +
					"`value` VARCHAR(45) NOT NULL, " +
					"PRIMARY KEY (`id`), " +
					"UNIQUE INDEX `value_UNIQUE` (`value` ASC)) " +
					"ENGINE = InnoDB " +
					"DEFAULT CHARACTER SET = utf8 " +
					"COMMENT = 'фамилии тут';");


            /*-- -----------------------------------------------------
                    -- Table `servicedb`.`owner`
            -- -----------------------------------------------------*/
                    statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS " + dbName + ".`owner` ( " +
					"`id` INT NOT NULL AUTO_INCREMENT, " +
					"`surname_id` INT NOT NULL, " +
					"`name_id` INT NOT NULL, " +
					"`patronymic_id` INT NOT NULL, " +
					"`telephone_number` VARCHAR(15) NOT NULL, " +
					"PRIMARY KEY (`id`), " +
					"INDEX `owner_name_idx` (`name_id` ASC), " +
					"INDEX `owner_patronymic_idx` (`patronymic_id` ASC), " +
					"INDEX `owner_surname_idx` (`surname_id` ASC), " +
					"CONSTRAINT `owner_name` " +
					"FOREIGN KEY (`name_id`) " +
					"REFERENCES " + dbName + ".`name` (`id`) " +
					"ON DELETE NO ACTION " +
					"ON UPDATE NO ACTION, " +
					"CONSTRAINT `owner_patronymic` " +
					"FOREIGN KEY (`patronymic_id`) " +
					"REFERENCES " + dbName + ".`patronymic` (`id`) " +
					"ON DELETE NO ACTION " +
					"ON UPDATE NO ACTION, " +
					"CONSTRAINT `owner_surname` " +
					"FOREIGN KEY (`surname_id`) " +
					"REFERENCES " + dbName + ".`surname` (`id`) " +
					"ON DELETE NO ACTION " +
					"ON UPDATE NO ACTION) " +
					"ENGINE = InnoDB " +
					"DEFAULT CHARACTER SET = utf8 " +
					"COMMENT = 'информация о пользователе';" );


            /*-- -----------------------------------------------------
                    -- Table `servicedb`.`brand`
            -- -----------------------------------------------------*/
                    statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS " + dbName + ".`brand` ( " +
					"`id` INT NOT NULL AUTO_INCREMENT, " +
					"`name` VARCHAR(45) NOT NULL, " +
					"PRIMARY KEY (`id`), " +
					"UNIQUE INDEX `name_UNIQUE` (`name` ASC)) " +
					"ENGINE = InnoDB " +
					"DEFAULT CHARACTER SET = utf8;");


            /*-- -----------------------------------------------------
                    -- Table `servicedb`.`type`
            -- -----------------------------------------------------*/
                    statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS " + dbName + ".`type` ( " +
					"`id` INT NOT NULL AUTO_INCREMENT, " +
					"`name` VARCHAR(45) NOT NULL, " +
					"PRIMARY KEY (`id`), " +
					"UNIQUE INDEX `name_UNIQUE` (`name` ASC)) " +
					"ENGINE = InnoDB " +
					"DEFAULT CHARACTER SET = utf8 " +
					"COMMENT = 'тип устройства';");


            /*-- -----------------------------------------------------
                    -- Table `servicedb`.`model`
            -- -----------------------------------------------------*/
                    statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS " + dbName + ".`model` ( " +
					"`id` INT NOT NULL AUTO_INCREMENT, " +
					"`name` VARCHAR(45) NOT NULL, " +
					"PRIMARY KEY (`id`), " +
					"UNIQUE INDEX `name_UNIQUE` (`name` ASC)) " +
					"ENGINE = InnoDB " +
					"DEFAULT CHARACTER SET = utf8 " +
					"COMMENT = 'таблица для хранения модели устройства';");


            /*-- -----------------------------------------------------
                    -- Table `servicedb`.`defect`
            -- -----------------------------------------------------*/
                    statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS " + dbName + ".`defect` ( " +
					"`id` INT NOT NULL AUTO_INCREMENT, " +
					"`description` VARCHAR(500) NOT NULL, " +
					"PRIMARY KEY (`id`), " +
					"UNIQUE INDEX `description_UNIQUE` (`description` ASC)) " +
					"ENGINE = InnoDB " +
					"DEFAULT CHARACTER SET = utf8 " +
					"COMMENT = 'неисправности';");


            /*-- -----------------------------------------------------
                    -- Table `servicedb`.`device`
            -- -----------------------------------------------------*/
                    statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS " + dbName + ".`device` ( " +
					"`id` INT NOT NULL AUTO_INCREMENT, " +
					"`type_id` INT NOT NULL, " +
					"`brand_id` INT NOT NULL, " +
					"`model_id` INT NOT NULL, " +
					"`serial_number` VARCHAR(45) NULL, " +
					"`defect_id` INT NOT NULL, " +
					"`owner_id` INT NOT NULL, " +
                    "PRIMARY KEY (`id`), " +
                    "INDEX `owner_idx` (`owner_id` ASC), " +
					"INDEX `brand_idx` (`brand_id` ASC), " +
					"INDEX `type_idx` (`type_id` ASC), " +
					"INDEX `model_idx` (`model_id` ASC), " +
					"INDEX `defect_idx` (`defect_id` ASC), " +
					"CONSTRAINT `device_owner` " +
					"FOREIGN KEY (`owner_id`) " +
					"REFERENCES " + dbName + ".`owner` (`id`) " +
					"ON DELETE NO ACTION " +
					"ON UPDATE NO ACTION, " +
					"CONSTRAINT `device_brand` " +
					"FOREIGN KEY (`brand_id`) " +
					"REFERENCES " + dbName + ".`brand` (`id`) " +
					"ON DELETE NO ACTION " +
					"ON UPDATE NO ACTION, " +
					"CONSTRAINT `device_type` " +
					"FOREIGN KEY (`type_id`) " +
					"REFERENCES " + dbName + ".`type` (`id`) " +
					"ON DELETE NO ACTION " +
					"ON UPDATE NO ACTION, " +
					"CONSTRAINT `device_model` " +
					"FOREIGN KEY (`model_id`) " +
					"REFERENCES " + dbName + ".`model` (`id`) " +
					"ON DELETE NO ACTION " +
					"ON UPDATE NO ACTION, " +
					"CONSTRAINT `device_defect` " +
					"FOREIGN KEY (`defect_id`) " +
					"REFERENCES " + dbName + ".`defect` (`id`) " +
					"ON DELETE NO ACTION " +
					"ON UPDATE NO ACTION) " +
					"ENGINE = InnoDB " +
					"DEFAULT CHARACTER SET = utf8; ");

            statement.executeUpdate("INSERT INTO servicedb.user_group (`id`,`name`) VALUES (1, 'administrator');");
            statement.executeUpdate("INSERT INTO servicedb.user_group (`id`,`name`) VALUES (2, 'manager');");
            statement.executeUpdate("INSERT INTO servicedb.user_group (`id`,`name`) VALUES (3, 'user');");

            statement.executeUpdate("INSERT INTO " + dbName + ".`user` (`id`, `login`, `password`, `user_group`)" +
                            " VALUES ('1', 'admin', 'admin', '1')");

            statement.close();
            con.close();
        }
        catch (SQLException sqlExep) {
            logger.error(sqlExep);
            System.exit(0);
        }
    }
}
