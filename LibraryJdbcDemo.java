import java.sql.*;
import java.util.Scanner;

public class LibraryJdbcDemo {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/librarydb"; // your DB name
        String user = "root"; // MySQL username
        String pass = "ruchika@123"; // MySQL password

        Scanner sc = new Scanner(System.in);

        try {
            // 1️⃣ Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2️⃣ Establish connection
            Connection con = DriverManager.getConnection(url, user, pass);
            System.out.println("✅ Connected to library database successfully!");

            while (true) {
                System.out.println("\n=== 📚 Library Management Menu ===");
                System.out.println("1. Add New Book");
                System.out.println("2. Display All Books");
                System.out.println("3. Update Book Details");
                System.out.println("4. Delete Book");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        // ➕ INSERT Operation
                        System.out.print("Enter Book Title: ");
                        String title = sc.nextLine();
                        System.out.print("Enter Author Name: ");
                        String author = sc.nextLine();

                        String insertQuery = "INSERT INTO books (title, author, available) VALUES (?, ?, ?)";
                        PreparedStatement psInsert = con.prepareStatement(insertQuery);
                        psInsert.setString(1, title);
                        psInsert.setString(2, author);
                        psInsert.setBoolean(3, true);

                        int rowsInserted = psInsert.executeUpdate();
                        System.out.println(rowsInserted + " book added successfully!");
                        psInsert.close();
                        break;

                    case 2:
                        // 📋 DISPLAY Operation
                        String selectQuery = "SELECT * FROM books";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(selectQuery);

                        System.out.println("\nID\tTitle\t\tAuthor\t\tAvailable");
                        System.out.println("------------------------------------------------");
                        while (rs.next()) {
                            System.out.println(
                                rs.getInt("id") + "\t" +
                                rs.getString("title") + "\t\t" +
                                rs.getString("author") + "\t\t" +
                                (rs.getBoolean("available") ? "Yes" : "No")
                            );
                        }
                        rs.close();
                        stmt.close();
                        break;

                    case 3:
                        // ✏ UPDATE Operation
                        System.out.print("Enter Book ID to update: ");
                        int uid = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter new Title: ");
                        String newTitle = sc.nextLine();
                        System.out.print("Enter new Author: ");
                        String newAuthor = sc.nextLine();
                        System.out.print("Is book available? (true/false): ");
                        boolean isAvailable = sc.nextBoolean();

                        String updateQuery = "UPDATE books SET title=?, author=?, available=? WHERE id=?";
                        PreparedStatement psUpdate = con.prepareStatement(updateQuery);
                        psUpdate.setString(1, newTitle);
                        psUpdate.setString(2, newAuthor);
                        psUpdate.setBoolean(3, isAvailable);
                        psUpdate.setInt(4, uid);

                        int rowsUpdated = psUpdate.executeUpdate();
                        System.out.println(rowsUpdated + " book record updated successfully!");
                        psUpdate.close();
                        break;

                    case 4:
                        // ❌ DELETE Operation
                        System.out.print("Enter Book ID to delete: ");
                        int did = sc.nextInt();

                        String deleteQuery = "DELETE FROM books WHERE id=?";
                        PreparedStatement psDelete = con.prepareStatement(deleteQuery);
                        psDelete.setInt(1, did);

                        int rowsDeleted = psDelete.executeUpdate();
                        System.out.println(rowsDeleted + " book deleted successfully!");
                        psDelete.close();
                        break;

                    case 5:
                        // 🚪 EXIT
                        System.out.println("Exiting Library Management System... Goodbye!");
                        con.close();
                        sc.close();
                        System.exit(0);

                    default:
                        System.out.println("❌ Invalid choice! Try again.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
