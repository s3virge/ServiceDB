package core.database;

import core.models.User;
import core.utils.ScriptRunner;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
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

    private static String lastError;

    //init static variables
    static {
        ResourceBundle properties = ResourceBundle.getBundle("dataBase");
        //String dbHost = properties.getString("dataBase.dbHost");
        dbHost = properties.getString("dataBase.host");
        dbPort = properties.getString("dataBase.port");
        //String dbName = properties.getString("dataBase.name");
        dbName = properties.getString("dataBase.name");

        dbUrl = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?useSSL=false&characterEncoding=UTF-8";

        dbUser = properties.getString("dataBase.login");
        dbPassword = properties.getString("dataBase.password");

        lastError = "no errors";
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

//            properties.put("useUnicode", "true");
//            properties.put("characterEncoding","Cp1251");
            properties.put("characterEncoding","UTF-8");

            properties.setProperty("useSSL", "false");
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
        String serverUrl = "jdbc:mysql://" + dbHost + ":" + dbPort ;

        //когда базы нет вываливается исключение
        try (Connection con = DriverManager.getConnection(serverUrl, getProperties());
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
    public static User getUser(String strLogin) {
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
    public void create() {
        logger.debug("Launch DataBase create()");

        String serverUrl = "jdbc:mysql://" + dbHost + ":" + dbPort;

        try (Connection con = DriverManager.getConnection(serverUrl, getProperties());
             /*Statement statement = con.createStatement()*/) {

            ScriptRunner runner = new ScriptRunner(con, false, false);
            String file = "src/Resources/sql/createbd.sql";
            try {
                runner.runScript(new BufferedReader(new FileReader(file)));
            }
            catch (IOException fileException) {
                fileException.getMessage();
            }
        }
        catch (SQLException sqlExcep) {
            lastError = sqlExcep.getMessage();
            logger.error(lastError);
            //что-то пошло не так...
            remove();

            System.exit(0);
            //throw new RuntimeException("Облом с созданием базы данных в create()");
        }

        logger.debug("create() successfully completed");
    }

    /**
     * удалить буза
     */
    private void remove() {
        String servUrl = "jdbc:mysql://" + dbHost + ":" + dbPort;

        try (Connection con = DriverManager.getConnection(servUrl, dbUser, dbPassword);
             Statement st = con.createStatement()) {
            st.execute("drop schema `" + dbName + "`;");
        }
        catch (Exception ex) {
            logger.error(ex);
        }
    }

    //возвращает id значения strValue в таблице strTable
    public static int getId(String strTable, String strColumn, String strValue) {

        String sql = "select id FROM " + strTable + " WHERE " + strColumn + "='" + strValue + "'";

        int valueId = 0;

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);

            rs.next();

            valueId = rs.getInt("id");
        }
        catch (SQLException e) {
            lastError = e.getMessage();
            logger.debug("DataBase.java: Облом c getId() -> " + lastError);
        }

        return valueId;
    }

    public static int getMaxId(String strTable) {
        String sql = "Select max(id) as id from " + strTable;
        int valueId = 0;

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);

            rs.next();

            valueId = rs.getInt("id");
        }
        catch (SQLException e) {
            lastError = e.getMessage();
            System.out.println( "DataBase.java: Облом c getMaxId() -> " + lastError);
        }

        return valueId;
    }

    public static int insert(String strTable, String strColumn, String strValue) {

        String sql = "INSERT INTO " + strTable + " (" + strColumn + ") VALUES (" + strValue + ")";

        int iLastId = 0;

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);

            //
            // Use the MySQL LAST_INSERT_ID()
            // function to do the same thing as getGeneratedKeys()
            //
            ResultSet rs;

            rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");

            if (rs.next()) {
                iLastId = rs.getInt(1);
            }
        }
        catch (SQLException e) {
            lastError = e.getMessage();
            logger.error("Облом с insert() -> " + lastError);
        }

        return iLastId;
    }

    public static String getLastError() {
        return lastError;
    }

}