package todo.dao;

import todo.bean.Todo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TodoDAO {

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

	// Method to initialize the database (create table and insert funny todos if the
	// table doesn't exist)
	public void initializeDatabase() {
		// Check if the table exists
		String checkTableSQL = "SELECT COUNT(*) FROM SYS.SYSTABLES WHERE TABLENAME = 'TODOS'";
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
			String createTableSQL = "CREATE TABLE TODOS (" + "ID VARCHAR(6) PRIMARY KEY, " + "TITLE VARCHAR(100), "
					+ "DESCRIPTION VARCHAR(255), " + "STATUS VARCHAR(20))";

			try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
				stmt.executeUpdate(createTableSQL); // Create the table
				System.out.println("Table 'TODOS' created successfully.");

				// Insert two funny todos
				addTodo("Laugh at this Todo", "This Todo is so funny, it will make you laugh till you cry!", "Pending");
				addTodo("More laughs!", "This Todo has a joke that will keep you smiling all day long!", "Completed");
				System.out.println("Two funny test todos added successfully.");
			} catch (SQLException e) {
				System.err.println("Error during table creation or inserting test todos: " + e.getMessage());
			}
		} else {
			System.out.println("Table 'TODOS' already exists. No need to create it.");
		}
	}

	// 1. Create Todo
	public void addTodo(String title, String description, String status) {
		String id = generateId(); // Generate unique ID
		String sql = "INSERT INTO todos (id, title, description, status) VALUES (?, ?, ?, ?)";

		try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, id);
			stmt.setString(2, title);
			stmt.setString(3, description);
			stmt.setString(4, status);
			stmt.executeUpdate();
			System.out.println("Todo added successfully with ID: " + id);
		} catch (SQLException e) {
			System.err.println("Error while adding Todo: " + e.getMessage());
		}
	}

	// 2. Read all Todos
	public List<Todo> getTodos() {
		List<Todo> todos = new ArrayList<>();
		String sql = "SELECT * FROM todos";

		try (Connection conn = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				String id = rs.getString("id");
				String title = rs.getString("title");
				String description = rs.getString("description");
				String status = rs.getString("status");
				todos.add(new Todo(id, title, description, status)); // Add each Todo item to the list
			}
		} catch (SQLException e) {
			System.err.println("Error while fetching Todos: " + e.getMessage());
		}
		return todos; // Return the list of Todos
	}

	// 3. Read Todo by ID
	public Todo getTodoById(String id) {
		String sql = "SELECT * FROM todos WHERE id = ?";

		try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					String title = rs.getString("title");
					String description = rs.getString("description");
					String status = rs.getString("status");
					return new Todo(id, title, description, status);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error while fetching Todo by ID: " + e.getMessage());
		}
		return null; // Return null if no Todo is found with the given ID
	}

	// 4. Update Todo
	public boolean updateTodo(String id, String title, String description, String status) {
		String sql = "UPDATE todos SET title = ?, description = ?, status = ? WHERE id = ?";
		boolean isUpdated = false;

		try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, title);
			stmt.setString(2, description);
			stmt.setString(3, status);
			stmt.setString(4, id);
			int rowsUpdated = stmt.executeUpdate();
			isUpdated = rowsUpdated > 0; // Return true if the update was successful
			if (isUpdated) {
				System.out.println("Todo with ID " + id + " updated successfully.");
			}
		} catch (SQLException e) {
			System.err.println("Error while updating Todo: " + e.getMessage());
		}
		return isUpdated;
	}

	// 5. Delete Todo
	public boolean deleteTodo(String id) {
		String sql = "DELETE FROM todos WHERE id = ?";
		boolean isDeleted = false;

		try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, id);
			int rowsDeleted = stmt.executeUpdate();
			isDeleted = rowsDeleted > 0; // Return true if the deletion was successful
			if (isDeleted) {
				System.out.println("Todo with ID " + id + " deleted successfully.");
			}
		} catch (SQLException e) {
			System.err.println("Error while deleting Todo: " + e.getMessage());
		}
		return isDeleted;
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
			if (getTodoById(id.toString()) == null) {
				return id.toString(); // Return the unique ID
			} else {
				id.setLength(0); // Clear the StringBuilder and regenerate the ID
			}
		}
	}

//	public static void main(String[] args) {
//		TodoDAO dao = new TodoDAO();
//		dao.initializeDatabase(); // Initialize the database (create table and insert test todos if necessary)
//	}
}
