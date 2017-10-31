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
//    private Connection conn;
//    private Statement stmt;
//    private ResultSet rs;

    //а что если подключаться не к базе данных а к mysql
    //и проверять существует ли базе
    public boolean isExist(){
        boolean result = true; //база существует

        //когда базы нет вываливается исключение
        try(Connection con = DriverManager.getConnection(url, user, password);
        Statement st = con.createStatement())
        {
            st.execute("USE  " + dbName);
        }
        catch(Exception ex){
            logger.error(ex);
            result = false;
        }

        return result;
    }

    public void getUser() {
        logger.debug("DataBase.initialise() is executed.\n     Try to connect to DB server");

        try(Connection conn = DriverManager.getConnection(url, user, password);
			Statement stmt = conn.createStatement())
		{
            stmt.execute("USE  " + dbName);
        	ResultSet rs = stmt.executeQuery("SELECT * FROM user;");

            while (rs.next()) {
                int id = rs.getInt(1);
                String login = rs.getString("login");
                String password = rs.getString("password");
                System.out.printf("id: %d login: %s; password: %s\n", id, login, password);
            }
        }
        catch (Exception exception){
            logger.error(exception);
			throw new RuntimeException("error in initialise()");
        }
    }

    //создать базу и все таблицы которые будем использовать
    public void createDB() {
		logger.debug("Launch createDB()");
        String serverUrl = "jdbc:mysql://" + host + ":" + port;

        try {
            Connection con = DriverManager.getConnection(serverUrl, user, password);
            Statement statement = con.createStatement();

            statement.executeUpdate("CREATE SCHEMA IF NOT EXISTS " + dbName + " DEFAULT CHARACTER SET utf8");
            statement.executeUpdate( "USE " + dbName);

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
