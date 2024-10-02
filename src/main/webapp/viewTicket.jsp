<%@ page session="true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="models.Customer" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"/>
</head>
<body>


<%
    // Check if the customer object is in the session
    Customer customer = (Customer) session.getAttribute("customer");
    if (customer == null) {
        // Redirect to login page if not logged in
        response.sendRedirect("loginPage.jsp");
        return; // Stop further processing
    }
%>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="homePage.jsp">Train Ticket System</a>
        
        <!-- Normal Nav Links -->
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="bookTicket.jsp">Book Tickets</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="viewTicket.jsp">View Tickets</a>
                </li>
                <% 
                    // Check if the customer is admin
                    if ("true".equals(customer.isAdmin())) {
                %>
                    <!-- Add Train and View All Train links for admins only -->
                    <li class="nav-item">
                        <a class="nav-link" href="trainRegister.jsp">Add Train</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="viewAllTrain.jsp">View All Trains</a>
                    </li>
                <% } %>
            </ul>
        </div>

        <!-- Right side profile dropdown -->
        <div class="dropdown ms-auto mx-3 my-1">
            <a class="dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                <i class="fa-regular fa-user"></i> <!-- Font Awesome profile icon -->
            </a>
            <ul class="dropdown-menu dropdown-menu-end">
                <li>
                    <!-- Redirecting to updateProfile.jsp directly -->
                    <a class="dropdown-item" href="updateProfile.jsp">Update Profile</a>
                </li>
                <li>
                    <form action="customer" method="POST" style="margin: 0;">
                        <input type="hidden" name="action" value="viewProfile">
                        <button type="submit" class="dropdown-item">View Profile</button>
                    </form>
                </li>
                <% 
                    // Check if the customer is admin
                    if ("true".equals(customer.isAdmin())) {
                %>
                    <li>
                        <form action="customer" method="POST" style="margin: 0;">
                            <input type="hidden" name="action" value="viewAll">
                            <button type="submit" class="dropdown-item">View All Profiles</button>
                        </form>
                    </li>
                <% } else { %>
                    <li>
                        <form action="customer" method="POST" style="margin: 0;">
                            <input type="hidden" name="action" value="delete">
                            <button type="submit" class="dropdown-item">Delete Profile</button>
                        </form>
                    </li>
                <% } %>
                <li>
                    <form action="customer" method="POST" style="margin: 0;">
                        <input type="hidden" name="action" value="logout">
                        <button type="submit" class="dropdown-item">Logout</button>
                    </form>
                </li>
            </ul>
        </div>
    </div>
</nav>

</body>
</html>