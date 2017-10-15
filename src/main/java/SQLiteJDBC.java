import org.apache.log4j.Logger;
import java.sql.*;
import java.util.ResourceBundle;

public class SQLiteJDBC {
    private static final Logger logger = Logger.getLogger(SQLiteJDBC.class);

    public static void main(String args[] ) {
        CreateTable();
        InsertRecords();
    }

    private static void CreateTable(){
        Connection connection = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + getDataBaseFileNameFromResources());

            logger.debug("Opened database successfully");

            stmt = connection.createStatement();
            String sql = "CREATE TABLE COMPANY " +
                    "(ID INT PRIMARY KEY     NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " AGE            INT     NOT NULL, " +
                    " ADDRESS        CHAR(50), " +
                    " SALARY         REAL)";

            stmt.executeUpdate(sql);
            stmt.close();
            connection.close();
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        logger.debug("Table created successfully");
    }

    private static void InsertRecords(){
        Connection connection = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + getDataBaseFileNameFromResources());

            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = connection.createStatement();
            String sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                    "VALUES (1, 'Paul', 32, 'California', 20000.00 );";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                    "VALUES (2, 'Allen', 25, 'Texas', 15000.00 );";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                    "VALUES (3, 'Teddy', 23, 'Norway', 20000.00 );";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                    "VALUES (4, 'Mark', 25, 'Rich-Mond ', 65000.00 );";
            stmt.executeUpdate(sql);

            stmt.close();
            connection.commit();
            connection.close();
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }

        System.out.println("Records created successfully");
    }


    private static String getDataBaseFileNameFromResources() {
        ResourceBundle labels = ResourceBundle.getBundle("dataBase");
        String dataBaseFileName = labels.getString("dataBase.dataBaseFileName");

        logger.debug("execute getDataBaseFileNameFromResources()");

        return dataBaseFileName;
    }
}