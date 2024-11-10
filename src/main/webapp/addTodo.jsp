<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Add Todo</title>
</head>
<body>
	<h1>Add New Todo</h1>
	<form action="TodoServlet" method="post">
		<label for="title">Title:</label> <input type="text" name="title"
			required><br> <label for="description">Description:</label>
		<input type="text" name="description" required><br> <label
			for="status">Status:</label> <input type="text" name="status"
			required><br>
		<button type="submit">Add Todo</button>
	</form>
	<a href="TodoServlet">Back to Todo List</a>
</body>
</html>
