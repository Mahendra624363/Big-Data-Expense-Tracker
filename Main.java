import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            HiveConnection.connect(); // connect to Hive


            boolean running = true;
            while (running) {
                System.out.println("\n-- Main Menu --");
                System.out.println("1. User");
                System.out.println("2. Admin");
                System.out.println("3. Exit");
                System.out.print("Choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        User.userMenu(scanner);
                        break;
                    case 2:
                        Admin.adminMenu(scanner);
                        break;
                    case 3:
                        running = false;
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid option");
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred:");
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}