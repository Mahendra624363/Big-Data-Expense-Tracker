import java.sql.*;
import java.util.Scanner;

public class Admin {
    public static void adminMenu(Scanner scanner) {
        try {
            System.out.print("Enter admin password: ");
            String password = scanner.nextLine();

            if (!password.equals("admin123")) {
                System.out.println("Wrong password.");
                return;
            }
            boolean back = false;
            while (!back) {
                System.out.println("\n-- Admin Menu --");
                System.out.println("1. View All Users");
                System.out.println("2. View All Expenses");
                System.out.println("3. View User Expenses");
                System.out.println("4. Back");
                System.out.print("Choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                case 1: viewUsers(); break;
                case 2: viewAllExpenses(); break;
                case 3: viewUserExpenses(scanner); break;
                case 4: back = true; break;
                default: System.out.println("Invalid option.");
            }
        }
    } catch (Exception e) {
        System.out.println("Admin error.");
        e.printStackTrace();
    }
}
    public static void viewUsers() throws SQLException {
        Statement stmt = HiveConnection.conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users");
        System.out.println("\n-- All Users --");
        while (rs.next()) {
            System.out.println("Username: " + rs.getString("username")+"\t Password: " + rs.getString("password"));
        }
    }
    
    public static void viewAllExpenses() throws SQLException {
        Statement stmt = HiveConnection.conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM expenses");
        System.out.println("\n-- All Expenses --");
        while (rs.next()) {
            System.out.printf("User: %s | Amount: %.2f | Category: %s | Date: %s-%s-%s\n",
                rs.getString("username"),
                rs.getDouble("amount"),
                rs.getString("category"),
                rs.getString("year"),
                rs.getString("month"),
                rs.getString("day"));
        }
    }
    public static void viewUserExpenses(Scanner scanner) throws SQLException {
        System.out.print("Enter username to view expenses: ");
        String username = scanner.nextLine();

        Statement stmt = HiveConnection.conn.createStatement();

        ResultSet rsCheck = stmt.executeQuery(String.format("SELECT * FROM users WHERE username = '%s'", username));
        if (!rsCheck.next()) {
            System.out.println("User does not exist.");
            return;
        }
        String query = String.format("SELECT * FROM expenses WHERE username = '%s'", username);
        ResultSet rs = stmt.executeQuery(query);

        System.out.println("\n-- Expenses for user: " + username + " --");
        boolean hasExpenses = false;
        while (rs.next()) {
            hasExpenses = true;
            System.out.printf("Amount: %.2f | Category: %s | Date: %s-%s-%s\n",
                rs.getDouble("amount"),
                rs.getString("category"),
                rs.getString("year"),
                rs.getString("month"),
                rs.getString("day"));
        }
        if (!hasExpenses) {
            System.out.println("No expenses found for this user.");
        }
    }

}