import org.apache.log4j.Logger;
import java.sql.*;
import java.util.ResourceBundle;

public class SQLiteJDBC {
    private static final Logger logger = Logger.getLogger(SQLiteJDBC.class);

    public static void main(String args[] ) {
        Connection connection = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + getDataBaseFileNameFromResources());
            connection.setAutoCommit(false); //что сдесь происходит...

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

    private static String getDataBaseFileNameFromResources() {
        ResourceBundle labels = ResourceBundle.getBundle("dataBase");
        String dataBaseFileName = labels.getString("dataBase.dataBaseFileName");

        logger.debug("execute getDataBaseFileNameFromResources()");

        return dataBaseFileName;
    }
}