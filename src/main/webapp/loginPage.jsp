<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script>
        function validateLoginForm() {
            let username = document.forms["loginForm"]["userName"].value;
            let password = document.forms["loginForm"]["password"].value;

            if (username === "") {
                alert("Username must be filled out");
                return false;
            }

            if (password === "") {
                alert("Password must be filled out");
                return false;
            }

            return true;
        }

        // Function to show error message if it exists
        function showErrorMessage() {
            const errorMessage = "${errorMessage}";
            if (errorMessage) {
                alert(errorMessage);
            }
        }
    </script>
</head>
<body onload="showErrorMessage()">

    <div class="container mt-5">
        <h1 class="text-center">Login</h1>

        <form name="loginForm" action="customer" method="post" onsubmit="return validateLoginForm()">
            <input type="hidden" name="action" value="login">

            <div class="mb-3">
                <label for="userName" class="form-label">Username</label>
                <input type="text" class="form-control" id="userName" name="userName" 
                       placeholder="Enter your username" value="${param.userName != null ? param.userName : ''}">
            </div>

            <div class="mb-3">
                <label for="password" class="form-label">Password</label>
                <input type="password" class="form-control" id="password" name="password" placeholder="Enter your password">
            </div>

            <button type="submit" class="btn btn-primary w-100">Login</button>
        </form>

        <div class="mt-3 text-center">
            <a href="registerPage.jsp">Don't have an account? Register here</a>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
