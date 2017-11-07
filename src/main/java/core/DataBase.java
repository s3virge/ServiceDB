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

    //а что если подключаться не к базе данных а к mysql
    //и проверять существует ли базе
    public boolean isExist(){
        boolean result = true; //база существует
		String serverUrl = "jdbc:mysql://" + dbHost + ":" + dbPort;

		//когда базы нет вываливается исключение
        try(Connection con = DriverManager.getConnection(serverUrl, dbUser, dbPassword);
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

    /**
    * возвращает пустого User если в базе нет пользователя с логином strLogin
    */
    public User getUser(String strLogin) {
        logger.debug("DataBase.getUser() is executed.\n     Try to connect to DB server");


        try(Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
			Statement stmt = conn.createStatement())
		{
            //stmt.execute("USE  " + dbName);
        	ResultSet rs = stmt.executeQuery("select * from user where login='"+ strLogin + "';");

        	User user = new User();

            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));

                //System.out.printf("id: %d; login: %s; dbPassword: %s;\n", id, login, password);
            }

            return user;
        }
        catch (Exception exception){
            logger.error(exception);
			throw new RuntimeException("error in DataBase.getUser()");
        }
    }

    //создать базу и все таблицы которые будем использовать
  /*  public void createDB() {
		logger.debug("Launch createDB()");
        String serverUrl = "jdbc:mysql://" + dbHost + ":" + dbPort;

        try (Connection con = DriverManager.getConnection(serverUrl, dbUser, dbPassword);
			 Statement statement = con.createStatement())
		{
         statement.executeUpdate("CREATE SCHEMA IF NOT EXISTS `servicedb` DEFAULT CHARACTER SET utf8; " +
                 "USE `servicedb`;" +
                 "-- -----------------------------------------------------" +
					-- Table `servicedb`.`user_group`
			-- -----------------------------------------------------
					"CREATE TABLE IF NOT EXISTS `servicedb`.`user_group` (
  "`id`   INT         NOT NULL AUTO_INCREMENT,
  "`name` VARCHAR(45) NOT NULL,
			"PRIMARY KEY (`id`),
            "UNIQUE INDEX `name_UNIQUE` (`name` ASC))
			"ENGINE = InnoDB
			"DEFAULT CHARACTER SET = utf8;

			-- -----------------------------------------------------
					-- Table `servicedb`.`user`
			-- -----------------------------------------------------
					CREATE TABLE IF NOT EXISTS `servicedb`.`user` (
  `id`         INT         NOT NULL AUTO_INCREMENT,
  `login`      VARCHAR(45) NOT NULL,
  `password`   VARCHAR(45) NOT NULL,
  `user_group` INT         NOT NULL,
					PRIMARY KEY (`id`),
					INDEX `user_group_idx` (`user_group` ASC),
			UNIQUE INDEX `login_UNIQUE` (`login` ASC),
			CONSTRAINT `user_group`
			FOREIGN KEY (`user_group`)
			REFERENCES `servicedb`.`user_group` (`id`)
			ON DELETE NO ACTION
			ON UPDATE NO ACTION
)
			ENGINE = InnoDB;

			-- -----------------------------------------------------
					-- Table `servicedb`.`name`
			-- -----------------------------------------------------
					CREATE TABLE IF NOT EXISTS `servicedb`.`name` (
  `id`    INT         NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(45) NOT NULL,
			PRIMARY KEY (`id`),
			UNIQUE INDEX `name_UNIQUE` (`value` ASC),
			UNIQUE INDEX `id_UNIQUE` (`id` ASC)
)
			ENGINE = InnoDB
			DEFAULT CHARACTER SET = utf8
			COMMENT = 'имена пользователей';

			-- -----------------------------------------------------
					-- Table `servicedb`.`patronymic`
			-- -----------------------------------------------------
					CREATE TABLE IF NOT EXISTS `servicedb`.`patronymic` (
  `id`    INT         NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(45) NOT NULL,
			PRIMARY KEY (`id`),
			UNIQUE INDEX `value_UNIQUE` (`value` ASC)
)
			ENGINE = InnoDB
			DEFAULT CHARACTER SET = utf8
			COMMENT = ' отчества';

			-- -----------------------------------------------------
					-- Table `servicedb`.`surname`
			-- -----------------------------------------------------
					CREATE TABLE IF NOT EXISTS `servicedb`.`surname` (
  `id`    INT         NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(45) NOT NULL,
			PRIMARY KEY (`id`),
			UNIQUE INDEX `value_UNIQUE` (`value` ASC)
)
			ENGINE = InnoDB
			DEFAULT CHARACTER SET = utf8
			COMMENT = ' фамилии тут';

			-- -----------------------------------------------------
					-- Table `servicedb`.`owner`
			-- -----------------------------------------------------
					CREATE TABLE IF NOT EXISTS `servicedb`.`owner` (
  `id`               INT         NOT NULL AUTO_INCREMENT,
  `surname_id`       INT         NOT NULL,
  `name_id`          INT         NOT NULL,
  `patronymic_id`    INT         NOT NULL,
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
			ON UPDATE NO ACTION
)
			ENGINE = InnoDB
			DEFAULT CHARACTER SET = utf8
			COMMENT = ' информация о пользователе';

			-- -----------------------------------------------------
					-- Table `servicedb`.`brand`
			-- -----------------------------------------------------
					CREATE TABLE IF NOT EXISTS `servicedb`.`brand` (
  `id`   INT         NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
			PRIMARY KEY (`id`),
			UNIQUE INDEX `name_UNIQUE` (`name` ASC)
)
			ENGINE = InnoDB
			DEFAULT CHARACTER SET = utf8;

			-- -----------------------------------------------------
					-- Table `servicedb`.`type`
			-- -----------------------------------------------------
					CREATE TABLE IF NOT EXISTS `servicedb`.`type` (
  `id`   INT         NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
			PRIMARY KEY (`id`),
			UNIQUE INDEX `name_UNIQUE` (`name` ASC)
)
			ENGINE = InnoDB
			DEFAULT CHARACTER SET = utf8
			COMMENT = ' тип устройства';

			-- -----------------------------------------------------
					-- Table `servicedb`.`model`
			-- -----------------------------------------------------
					CREATE TABLE IF NOT EXISTS `servicedb`.`model` (
  `id`   INT         NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
			PRIMARY KEY (`id`),
			UNIQUE INDEX `name_UNIQUE` (`name` ASC)
)
			ENGINE = InnoDB
			DEFAULT CHARACTER SET = utf8
			COMMENT = ' таблица для хранения модели устр' *//* comment truncated *//* *//*ойства*//*;

			-- -----------------------------------------------------
					-- Table `servicedb`.`defect`
			-- -----------------------------------------------------
					CREATE TABLE IF NOT EXISTS `servicedb`.`defect` (
  `id`          INT          NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(500) NOT NULL,
			PRIMARY KEY (`id`),
			UNIQUE INDEX `description_UNIQUE` (`description` ASC)
)
			ENGINE = InnoDB
			DEFAULT CHARACTER SET = utf8
			COMMENT = 'неисправности';

			-- -----------------------------------------------------
					-- Table `servicedb`.`status`
			-- -----------------------------------------------------
					CREATE TABLE IF NOT EXISTS `servicedb`.`status` (
  `id`    INT         NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(45) NOT NULL,
			PRIMARY KEY (`id`),
			UNIQUE INDEX `value_UNIQUE` (`value` ASC)
)
			ENGINE = InnoDB
			DEFAULT CHARACTER SET = utf8
			COMMENT = ' этап ремонта';

			-- -----------------------------------------------------
					-- Table `servicedb`.`repair`
			-- -----------------------------------------------------
					CREATE TABLE IF NOT EXISTS `servicedb`.`repair` (
  `id`                INT           NOT NULL AUTO_INCREMENT,
  `master_comments`   VARCHAR(1024) NULL,
  `diagnostic_result` VARCHAR(1024) NULL,
  `repair_result`     VARCHAR(450)  NULL,
  `status_id`         INT           NOT NULL,
					PRIMARY KEY (`id`),
					INDEX `repair_status_idx` (`status_id` ASC),
			CONSTRAINT `repair_status`
			FOREIGN KEY (`status_id`)
			REFERENCES `servicedb`.`status` (`id`)
			ON DELETE NO ACTION
			ON UPDATE NO ACTION
)
			ENGINE = InnoDB
			DEFAULT CHARACTER SET = utf8
			COMMENT = 'таблица для результата диагностики-ремонта';

			-- -----------------------------------------------------
					-- Table `servicedb`.`device`
			-- -----------------------------------------------------
					CREATE TABLE IF NOT EXISTS `servicedb`.`device` (
  `id`            INT         NOT NULL AUTO_INCREMENT,
  `type_id`       INT         NOT NULL,
  `brand_id`      INT         NOT NULL,
  `model_id`      INT         NOT NULL,
  `serial_number` VARCHAR(45) NULL,
  `defect_id`     INT         NOT NULL,
  `owner_id`      INT         NOT NULL,
  `repair_id`     INT         NOT NULL,
					PRIMARY KEY (`id`),
					INDEX `owner_idx` (`owner_id` ASC),
			INDEX `brand_idx` (`brand_id` ASC),
			INDEX `type_idx` (`type_id` ASC),
			INDEX `model_idx` (`model_id` ASC),
			INDEX `defect_idx` (`defect_id` ASC),
			INDEX `device_repair_idx` (`repair_id` ASC),
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
			ON UPDATE NO ACTION,
			CONSTRAINT `device_repair`
			FOREIGN KEY (`repair_id`)
			REFERENCES `servicedb`.`repair` (`id`)
			ON DELETE NO ACTION
			ON UPDATE NO ACTION
)
			ENGINE = InnoDB
			DEFAULT CHARACTER SET = utf8;


			SET SQL_MODE = @OLD_SQL_MODE;
			SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
			SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;

			-- -----------------------------------------------------
					-- Data for table `servicedb`.`user_group`
			-- -----------------------------------------------------
					START TRANSACTION;
			USE `servicedb`;
			INSERT INTO `servicedb`.`user_group` (`id`, `name`) VALUES (1, 'administrator');
			INSERT INTO `servicedb`.`user_group` (`id`, `name`) VALUES (2, 'manager');
			INSERT INTO `servicedb`.`user_group` (`id`, `name`) VALUES (3, 'MASTER');
			INSERT INTO `servicedb`.`user_group` (`id`, `name`) VALUES (4, 'acceptor');

			COMMIT;

			-- -----------------------------------------------------
					-- Data for table `servicedb`.`user`
			-- -----------------------------------------------------
					START TRANSACTION;
			USE `servicedb`;
			INSERT INTO `servicedb`.`user` (`id`, `login`, `password`, `user_group`) VALUES (1, 'admin', md5(' admin '), 1);

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

			COMMIT;");
        }
        catch (SQLException sqlExep) {
            logger.error(sqlExep);
            String msg = "SQLException в createDB() - > " + sqlExep.getMessage();
            MsgBox.show(msg, MsgBox.Type.MB_ERROR);
            System.exit(0);
        }
    }*/

    public void createDB(){
    	logger.debug("Launch createDB()");
        ClassLoader cl = this.getClass().getClassLoader();
        URL resFile = cl.getResource("sql/create_servicedb.sql");

        Path paths = null;
        try {
            paths = Paths.get(resFile.toURI());
        }
        catch (URISyntaxException uriExce){
            logger.error(uriExce);
        }

        try {
            final Stream<String> lines = Files.lines(paths);
            String s = "";
            for (String line : (Iterable<String>) () -> lines.iterator()) {
                s += line;
            }
            lines.close();
        }
        catch (IOException ioEx) {
            logger.error(ioEx);
        }


    	// получить текущий classloader
		URL file = getClass().getResource("sql/create_servicedb.sql");

        String sqlScript = null;
        String strFile = file.toString();
        System.out.println(strFile);

		try(FileInputStream fis = new FileInputStream(strFile)){
            System.out.println("РАзмер файла " + fis.available() + " байт");
            int i = -1;
            while ((i = fis.read()) != -1 ){
                System.out.println((char)i);
            }
		}
		catch (IOException e){
			MsgBox.show(e.getMessage(), MsgBox.Type.MB_ERROR);
			System.exit(0);
		}

		String serverUrl = "jdbc:mysql://" + dbHost + ":" + dbPort;

		try (Connection con = DriverManager.getConnection(serverUrl, dbUser, dbPassword);
			 Statement statement = con.createStatement())
		{
			statement.execute(sqlScript);
		}
		catch (SQLException sqlEx) {
			MsgBox.show(sqlEx.getMessage(), MsgBox.Type.MB_ERROR);
		}
	}
}
