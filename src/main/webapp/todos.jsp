<%@ page import="java.util.List"%>
<%@ page import="todo.bean.Todo"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Todo List</title>
</head>
<body>
	<h1>Todo List</h1>
	<a href="addTodo.jsp">Add New Todo</a>
	<table border="1">
		<thead>
			<tr>
				<th>ID</th>
				<th>Title</th>
				<th>Description</th>
				<th>Status</th>
				<th>Actions</th>
			</tr>
		</thead>
		<tbody>
			<%
			List<Todo> todos = (List<Todo>) request.getAttribute("todos");
			if (todos != null) {
				for (Todo todo : todos) {
			%>
			<tr>
				<td><%=todo.getId()%></td>
				<td><%=todo.getTitle()%></td>
				<td><%=todo.getDescription()%></td>
				<td><%=todo.getStatus()%></td>
				<td><a href="TodoServlet?action=edit&id=<%=todo.getId()%>">Edit</a>
					<a href="TodoServlet?action=delete&id=<%=todo.getId()%>">Delete</a>
				</td>
			</tr>
			<%
			}
			} else {
			%>
			<tr>
				<td colspan="5">No todos found.</td>
			</tr>
			<%
			}
			%>
		</tbody>
	</table>
</body>
</html>
