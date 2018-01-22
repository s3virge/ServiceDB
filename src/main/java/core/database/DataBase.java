package core.database;

import core.models.User;
import core.utils.MsgBox;
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
    public void create() {
        logger.debug("Launch create()");

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
        }
        catch (SQLException sqlExcep) {
            MsgBox.show("Облом в create() -> " + sqlExcep.getMessage(), MsgBox.Type.MB_ERROR);

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
            System.out.println( "DataBase.java: Облом c getId() -> " + e.getMessage());
        }

        return valueId;
    }

    public static int getId(String sql) {

        int valueId = 0;

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);

            rs.next();

            valueId = rs.getInt("id");
        }
        catch (SQLException e) {
            System.out.println( "DataBase.java: Облом c getId() -> " + e.getMessage());
        }

        return valueId;
    }

    public static boolean insert(String strTable, String strColumn, String strValue) {

        String sql = "INSERT INTO " + strTable + " (" + strColumn + ") VALUE ('" + strValue + "')";

        boolean bResult = false;

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement()) {
            bResult = stmt.execute(sql);
        }
        catch (SQLException e) {
            System.out.println("Облом с insert() -> " + e.getMessage());
        }

        return bResult;
    }

    public static boolean insert(String strSql) {

        boolean result = true;

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement()) {
            stmt.execute(strSql);
        }
        catch (SQLException e) {
            System.out.println("Облом с insert() -> " + e.getMessage());
            result = false;
        }

        return result;
    }

}