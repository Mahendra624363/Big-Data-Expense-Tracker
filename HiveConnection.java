import java.sql.*;

public class HiveConnection {
    public static Connection conn;

    public static void connect() {
        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            conn = DriverManager.getConnection(
                "jdbc:hive2://localhost:10000/expensetracker", "", "");
            System.out.println("Connected to Hive.");
        } catch (Exception e) {
            System.out.println("Connection failed.");
            e.printStackTrace();
        }
    }
}