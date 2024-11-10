package todo.dao;

import todo.bean.User;
import java.sql.*;
import java.util.Random;

public class UserDAO {

	// Derby DB connection details
	private static final String DB_URL = "jdbc:derby:C:\\Users\\rv21\\TodoApp;create=true"; // Adjust the URL if needed

	// Method to establish a database connection
	private Connection connect() throws SQLException {
		try {
			// Load the Derby JDBC driver
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver"); // Load the Derby driver class

			// Establish the connection to the Derby database
			return DriverManager.getConnection(DB_URL);
		} catch (ClassNotFoundException e) {
			throw new SQLException("Derby JDBC driver not found: " + e.getMessage(), e);
		} catch (SQLException e) {
			throw new SQLException("Error while connecting to the database: " + e.getMessage(), e);
		}
	}

	// Method to initialize the database (create table if it doesn't exist)
	public void initializeDatabase() {
		// Check if the table exists
		String checkTableSQL = "SELECT COUNT(*) FROM SYS.SYSTABLES WHERE TABLENAME = 'USER'";
		boolean tableExists = false;

		try (Connection conn = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(checkTableSQL)) {
			if (rs.next() && rs.getInt(1) > 0) {
				tableExists = true; // Table exists
			}
		} catch (SQLException e) {
			System.err.println("Error checking table existence: " + e.getMessage());
		}

		if (!tableExists) {
			// Table doesn't exist, create it
			String createTableSQL = "CREATE TABLE USER (" + "ID VARCHAR(6) PRIMARY KEY, " + "NAME VARCHAR(100), "
					+ "EMAIL VARCHAR(100) UNIQUE, " + "PASSWORD VARCHAR(255))";

			try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
				stmt.executeUpdate(createTableSQL); // Create the table
				System.out.println("Table 'USER' created successfully.");
			} catch (SQLException e) {
				System.err.println("Error during table creation: " + e.getMessage());
			}
		} else {
			System.out.println("Table 'USER' already exists. No need to create it.");
		}
	}

	// Method to insert a new user into the database
	public void insertUser(String name, String email, String password) {
		String id = generateId(); // Generate unique ID
		String sql = "INSERT INTO user (id, name, email, password) VALUES (?, ?, ?, ?)";

		try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, id);
			stmt.setString(2, name);
			stmt.setString(3, email);
			stmt.setString(4, password); // For real apps, hash the password before storing
			stmt.executeUpdate();
			System.out.println("User added successfully with ID: " + id);
		} catch (SQLException e) {
			System.err.println("Error while adding User: " + e.getMessage());
		}
	}

	// Method to verify login credentials (email and password)
	public User verifyLogin(String email, String password) {
		String sql = "SELECT * FROM user WHERE email = ? AND password = ?";

		try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, email);
			stmt.setString(2, password); // Password should be hashed for security in production
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					// User found, return user details
					String id = rs.getString("id");
					String name = rs.getString("name");
					return new User(id, name, email, password);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error while verifying login: " + e.getMessage());
		}
		return null; // Return null if login failed
	}

	// Method to generate a six-digit alphanumeric ID
	private String generateId() {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuilder id = new StringBuilder();

		while (true) {
			// Generate a random 6-character ID
			for (int i = 0; i < 6; i++) {
				int index = random.nextInt(characters.length());
				id.append(characters.charAt(index));
			}

			// Check if the generated ID is unique in the database
			if (getUserById(id.toString()) == null) {
				return id.toString(); // Return the unique ID
			} else {
				id.setLength(0); // Clear the StringBuilder and regenerate the ID
			}
		}
	}

	// Helper method to get a User by ID (used in generateId to ensure uniqueness)
	private User getUserById(String id) {
		String sql = "SELECT * FROM user WHERE id = ?";

		try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					String name = rs.getString("name");
					String email = rs.getString("email");
					String password = rs.getString("password");
					return new User(id, name, email, password);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error while fetching User by ID: " + e.getMessage());
		}
		return null;
	}

//	Example main method for testing

	public static void main(String[] args) {
		UserDAO dao = new UserDAO();
		dao.initializeDatabase(); // Initialize the database (create table if necessary)
		dao.insertUser("John Doe", "john@example.com", "password123"); // Add a new user
		User user = dao.verifyLogin("john@example.com", "password123"); // Verify login
		if (user != null) {
			System.out.println("Login successful! User ID: " + user.getId());
		} else {							
			System.out.println("Invalid login credentials.");
		}
	}
}
