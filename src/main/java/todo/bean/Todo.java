package todo.bean;

public class Todo {
	private String id; // Changed to String for alphanumeric ID
	private String title;
	private String description;
	private String status;

	// Parameterized constructor
	public Todo(String id, String title, String description, String status) {
		this.id = id; // Generate a unique ID when a Todo is created
		this.title = title;
		this.description = description;
		this.status = status;
	}

	// Getter and Setter for id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	// Getter and Setter for title
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	// Getter and Setter for description
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// Getter and Setter for status
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Todo{id='" + id + "', title='" + title + "', description='" + description + "', status='" + status
				+ "'}";
	}
}
