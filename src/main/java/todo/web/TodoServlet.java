package todo.web;

import todo.bean.Todo;
import todo.dao.TodoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/TodoServlet")
public class TodoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TodoDAO todoDAO;

	public TodoServlet() {
		super();
		todoDAO = new TodoDAO(); // Initialize the TodoDAO object
	}

	// Initialize the database and table when the servlet is loaded
	@Override
	public void init() throws ServletException {
		super.init();
		todoDAO.initializeDatabase(); // Initialize the table if it doesn't exist and insert test todos
	}

	// Handle GET request (Display list of todos or show a single todo for update)
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		if (action == null) {
			// If no action specified, show all todos
			List<Todo> todos = todoDAO.getTodos();
			System.out.println(todos);
			request.setAttribute("todos", todos);
			request.getRequestDispatcher("/todos.jsp").forward(request, response);
		} else if (action.equals("edit")) {
			// Edit action: Show todo details for editing
			String id = request.getParameter("id");
			Todo todo = todoDAO.getTodoById(id);
			request.setAttribute("todo", todo);
			request.getRequestDispatcher("/editTodo.jsp").forward(request, response);
		} else if (action.equals("delete")) {
			// Delete action: Delete the todo by ID
			String id = request.getParameter("id");
			todoDAO.deleteTodo(id);
			response.sendRedirect("TodoServlet"); // Redirect to list after deletion
		}
	}

	// Handle POST request (Add or Update a todo)
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		if (action == null) {
			// Create new Todo
			String title = request.getParameter("title");
			String description = request.getParameter("description");
			String status = request.getParameter("status");
			todoDAO.addTodo(title, description, status);
			response.sendRedirect("TodoServlet"); // Redirect to list after adding
		} else if (action.equals("update")) {
			// Update an existing Todo
			String id = request.getParameter("id");
			String title = request.getParameter("title");
			String description = request.getParameter("description");
			String status = request.getParameter("status");
			todoDAO.updateTodo(id, title, description, status);
			response.sendRedirect("TodoServlet"); // Redirect to list after updating
		}
	}
}
