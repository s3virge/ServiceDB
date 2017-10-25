package core;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class DataBase {
    private static final Logger logger = Logger.getLogger(DataBase.class);

    // JDBC URL, username and password of MySQL server
    private static final String url;
    private static final String user;
    private static final String password;

    static {
        ResourceBundle properties = ResourceBundle.getBundle("dataBase");
        String host = properties.getString("dataBase.host");
        String port = properties.getString("dataBase.port");
        String dbName = properties.getString("dataBase.name");

        //= "jdbc:mysql://localhost:3306/test";
        url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
        user = properties.getString("dataBase.login");
        password = properties.getString("dataBase.password");
    }

    // JDBC variables for opening and managing connection
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    //создать все таблицы которые будем использовать
    private static void fistRun() {

    }

    //существует ли база данных с указанным именем
    public static boolean isExist(/*String strDbName*/) {


        Connection conn = null;
        Statement stmt = null;
        return false;
    }
}
