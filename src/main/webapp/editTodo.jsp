<%@ page import="todo.bean.Todo"%>
<%@ page import="todo.dao.TodoDAO"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Edit Todo</title>
</head>
<body>
	<h1>Edit Todo</h1>
	<form action="TodoServlet" method="post">
		<input type="hidden" name="id" value="${todo.id}"> <label
			for="title">Title:</label> <input type="text" name="title"
			value="${todo.title}" required><br> <label
			for="description">Description:</label> <input type="text"
			name="description" value="${todo.description}" required><br>
		<label for="status">Status:</label> <input type="text" name="status"
			value="${todo.status}" required><br>
		<button type="submit" name="action" value="update">Update
			Todo</button>
	</form>
	<a href="TodoServlet">Back to Todo List</a>
</body>
</html>
