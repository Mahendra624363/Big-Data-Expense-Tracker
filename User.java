import java.sql.*;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;


public class User {

    public static void userMenu(Scanner scanner) {
        System.out.println("\n-- User Menu --");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.print("Choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 : register(scanner);
            break;
            case 2 : login(scanner);
            break;
            default : System.out.println("Invalid choice.");
        }
    }
    public static void register(Scanner scanner) {
        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            Statement stmt = HiveConnection.conn.createStatement();
            String checkSql = String.format(
                    "SELECT COUNT(*) as count FROM users WHERE username = '%s'", username);
                ResultSet rs = stmt.executeQuery(checkSql);
                rs.next();
                int count = rs.getInt("count");

                if (count > 0) {
                    System.out.println(" Username already exists. Please choose another.");
                    return; 
                }
                String insertSql = String.format(
                        "INSERT INTO users VALUES ('%s', '%s')", username, password);
                    stmt.execute(insertSql);
                    System.out.println("User registered successfully.");

                } catch (SQLException e) {
                    System.out.println(" Registration failed.");
                    e.printStackTrace();
                }
            }
    
    public static void login(Scanner scanner) {
        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            Statement stmt = HiveConnection.conn.createStatement();
            String query = String.format(
                    "SELECT * FROM users WHERE username='%s' AND password='%s'",
                    username, password);
                ResultSet rs = stmt.executeQuery(query);

                if (rs.next()) {
                    System.out.println("Login successful.");
                    dashboard(scanner, username);
                } else {
                    System.out.println("Invalid credentials.");
                }
        } catch (SQLException e) {
            System.out.println("Login error.");
            e.printStackTrace();
        }
    }
    
    public static void dashboard(Scanner scanner, String username)throws SQLException {
        boolean back = false;
        while (!back) {
            System.out.println("\n-- Dashboard --");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. View Expenses yearly");
            System.out.println("4. View Expenses monthly");
            System.out.println("5. View Expenses last 30 days");
            System.out.println("6. Logout");
            System.out.print("Choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
            case 1 : addExpense(scanner, username);
            break;
            case 2 : viewExpenses(username);
            break;
            case 3: getYearlySummary(username); break;
            case 4: getMonthlySummary(username); break;
            case 5: getDayWiseSummary(username); break;
            case 6 : back = true;
            break;
            default : System.out.println("Invalid option");
        }
    }
}
    public static void addExpense(Scanner scanner, String username) throws SQLException {
        Statement stmt = HiveConnection.conn.createStatement();

        Date now = new Date();
        SimpleDateFormat yearFmt = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFmt = new SimpleDateFormat("MM");
        SimpleDateFormat dayFmt = new SimpleDateFormat("dd");

        String year = yearFmt.format(now);
        String month = monthFmt.format(now);
        String day = dayFmt.format(now);
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();  

        String insert = String.format(
            "INSERT INTO TABLE expenses PARTITION (year='%s', month='%s', day='%s') VALUES ('%s', %.2f, '%s')",
            year, month, day, username, amount, category
        );
        stmt.execute(insert);
        System.out.println("Expense added.");
    }
        
        
    public static void getYearlySummary(String username) throws SQLException {
        Statement stmt = HiveConnection.conn.createStatement();
        String query = String.format(
            "SELECT year, SUM(amount) AS total FROM expenses WHERE username = '%s' GROUP BY year ORDER BY year",
            username
        );
        ResultSet rs = stmt.executeQuery(query);
        System.out.println("\n-- Yearly Summary --");
        while (rs.next()) {
            System.out.printf("Year: %s | Total: %.2f\n", rs.getString("year"), rs.getDouble("total"));
        }
    }
    
    public static void getMonthlySummary(String username) throws SQLException {
        Statement stmt = HiveConnection.conn.createStatement();
        String query = String.format(
            "SELECT year, month, SUM(amount) AS total FROM expenses WHERE username = '%s' " +
            "GROUP BY year, month ORDER BY year, month",
            username
        );
        ResultSet rs = stmt.executeQuery(query);
        System.out.println("\n-- Monthly Summary --");
        while (rs.next()) {
            System.out.printf("Year: %s | Month: %s | Total: %.2f\n",
                rs.getString("year"), rs.getString("month"), rs.getDouble("total"));
        }
    }
    
    public static void getDayWiseSummary(String username) throws SQLException {
        Statement stmt = HiveConnection.conn.createStatement();
        String query = String.format(
            "SELECT CONCAT(year, '-', month, '-', day) AS date, SUM(amount) AS total " +
            "FROM expenses WHERE username = '%s' AND " +
            "TO_DATE(CONCAT(year, '-', month, '-', day)) >= DATE_SUB(CURRENT_DATE, 30) " +
            "GROUP BY year, month, day ORDER BY date DESC",
            username
        );
        ResultSet rs = stmt.executeQuery(query);
        System.out.println("\n-- Day-wise (Last 30 Days) Summary --");
        while (rs.next()) {
            System.out.printf("Date: %s | Total: %.2f\n",
                rs.getString("date"), rs.getDouble("total"));
        }
    }

    
    public static void viewExpenses(String username) {
        try {
            Statement stmt = HiveConnection.conn.createStatement();
            String sql = String.format("SELECT * FROM expenses WHERE username='%s'", username);
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("\n-- Your Expenses --");
            while (rs.next()) {
                System.out.printf("Amount: %.2f | Category: %s | Date: %s\n",
                        rs.getDouble("amount"),
                        rs.getString("category"),
                        rs.getDate("expense_date"));
                }
            } catch (SQLException e) {
                System.out.println("Failed to retrieve expenses.");
                e.printStackTrace();
            }
        }
    }

