package todo.utils;

import java.sql.*;

public class TestJDBC {

	public static void main(String[] args) {
		// Derby DB connection details
		String url = "jdbc:derby:C:\\Users\\rv21\\TodoApp;create=true";

		// SQL query to create table if it doesn't exist
		String createTableSQL = "CREATE TABLE TODOS (" + "ID VARCHAR(6) PRIMARY KEY, " + "TITLE VARCHAR(100), "
				+ "DESCRIPTION VARCHAR(255), " + "STATUS VARCHAR(20))";

		// SQL query to insert sample data
		String insertSQL = "INSERT INTO TODOS (ID, TITLE, DESCRIPTION, STATUS) VALUES (?, ?, ?, ?)";

		// SQL query to fetch all todos
		String selectSQL = "SELECT * FROM TODOS";

		// SQL query to drop the table
		String dropTableSQL = "DROP TABLE TODOS";

		try (Connection conn = DriverManager.getConnection(url)) {
			// Check if the table already exists, if not, create it
			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet tables = metaData.getTables(null, null, "TODOS", null);

			if (!tables.next()) {
				try (Statement stmt = conn.createStatement()) {
					stmt.executeUpdate(createTableSQL);
					System.out.println("Table 'TODOS' created successfully.");
				}
			} else {
				System.out.println("Table 'TODOS' already exists.");
			}

			// Insert sample data into the TODOS table
			try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
				stmt.setString(1, "ABC123");
				stmt.setString(2, "First Todo");
				stmt.setString(3, "This is the first todo.");
				stmt.setString(4, "Pending");
				stmt.executeUpdate();

				stmt.setString(1, "DEF456");
				stmt.setString(2, "Second Todo");
				stmt.setString(3, "This is the second todo.");
				stmt.setString(4, "Completed");
				stmt.executeUpdate();

				stmt.setString(1, "GHI789");
				stmt.setString(2, "Third Todo");
				stmt.setString(3, "This is the third todo.");
				stmt.setString(4, "Pending");
				stmt.executeUpdate();
			}

			System.out.println("Sample data inserted.");

			// Display the records from the TODOS table
			try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(selectSQL)) {
				while (rs.next()) {
					System.out.println("ID: " + rs.getString("ID") + ", Title: " + rs.getString("TITLE")
							+ ", Description: " + rs.getString("DESCRIPTION") + ", Status: " + rs.getString("STATUS"));
				}
			}

			// Drop the table after operation
			try (Statement stmt = conn.createStatement()) {
				stmt.executeUpdate(dropTableSQL);
				System.out.println("Table 'TODOS' dropped successfully.");
			}

		} catch (SQLException e) {
			System.err.println("Error during database operations: " + e.getMessage());
		}
	}
}
