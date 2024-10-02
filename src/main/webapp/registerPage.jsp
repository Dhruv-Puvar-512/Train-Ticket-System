<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script>
        function validateRegisterForm() {
            let userName = document.forms["registerForm"]["userName"].value;
            let email = document.forms["registerForm"]["email"].value;
            let password = document.forms["registerForm"]["password"].value;
            let address = document.forms["registerForm"]["address"].value;
            let contactNumber = document.forms["registerForm"]["contactNumber"].value;

            let emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
            let passwordPattern = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$/;
            let contactPattern = /^[0-9]{10}$/;

            if (userName === "") {
                alert("Username is required.");
                return false;
            }

            if (!emailPattern.test(email)) {
                alert("Please enter a valid email.");
                return false;
            }

            if (!passwordPattern.test(password)) {
                alert("Password must be at least 8 characters, with 1 uppercase letter and 1 number.");
                return false;
            }

            if (address === "") {
                alert("Address is required.");
                return false;
            }

            if (!contactPattern.test(contactNumber)) {
                alert("Contact number must be 10 digits.");
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
        <h1 class="text-center">Register</h1>

        <form name="registerForm" action="customer" method="post" onsubmit="return validateRegisterForm()">
            <input type="hidden" name="action" value="register">

            <div class="mb-3">
                <label for="userName" class="form-label">Username</label>
                <input type="text" class="form-control" id="userName" name="userName" placeholder="Enter your username" value="${param.userName}">
            </div>

            <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="email" class="form-control" id="email" name="email" placeholder="Enter your email" value="${param.email}">
            </div>

            <div class="mb-3">
                <label for="password" class="form-label">Password</label>
                <input type="password" class="form-control" id="password" name="password" placeholder="Enter your password">
            </div>

            <div class="mb-3">
                <label for="address" class="form-label">Address</label>
                <input type="text" class="form-control" id="address" name="address" placeholder="Enter your address" value="${param.address}">
            </div>

            <div class="mb-3">
                <label for="contactNumber" class="form-label">Contact Number</label>
                <input type="text" class="form-control" id="contactNumber" name="contactNumber" placeholder="Enter your contact number" value="${param.contactNumber}">
            </div>

            <button type="submit" class="btn btn-primary w-100">Register</button>
        </form>

        <div class="mt-3 text-center">
            <a href="loginPage.jsp">Already have an account? Login here</a>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
