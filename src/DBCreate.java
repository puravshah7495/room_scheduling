/**
 * Created by Purav Shah.
 */
import java.sql.*;
public class DBCreate {
    public static void main(String [] args) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Data.db");

            stmt = c.createStatement();
            String sql = "CREATE TABLE ROOM " +
                    "(NAME      TEXT    NOT NULL, " +
                    " SPACE     INT     NOT NULL);";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE EMPLOYEE" +
                    "(NUMBER    INT    NOT NULL ," +
                    "NAME   TEXT    NOT NULL," +
                    "ROOM   TEXT    NOT NULL," +
                    "DATE   TEXT    NOT NULL);";
            stmt.executeUpdate(sql);

            stmt.close();
            c.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
