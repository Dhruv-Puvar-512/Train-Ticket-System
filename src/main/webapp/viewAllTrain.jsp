<%@ page session="true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="models.Customer" %>
<%@ page import="java.util.*" %>
<%@ page import="models.Train" %>
<%@ page import="services.TrainServices" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Train Ticket System - View All Trains</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"/>
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.5/css/jquery.dataTables.min.css">
</head>
<body>
<%
    // Check if the customer object is in the session
    Customer customer = (Customer) session.getAttribute("customer");
    if (customer == null) {
        // Redirect to login page if not logged in
        response.sendRedirect("loginPage.jsp");
        return;
    } else if (!"true".equals(customer.isAdmin())) {
        // Redirect if the user is not an admin
        session.setAttribute("error", "Unauthorized access!");
        response.sendRedirect("errorPage.jsp");
        return;
    }

    // Get the list of trains
    ArrayList<Train> trainList = TrainServices.displayAllTrains();
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


<!-- Train List Table -->
<div class="container mt-5">
    <h2 class="mb-4">All Trains</h2>
    <table id="trainTable" class="table table-striped table-bordered">
        <thead>
            <tr>
                <th>Train ID</th>
                <th>Train Name</th>
                <th>Departure Location</th>
                <th>Arrival Location</th>
                <th>Departure Time</th>
                <th>Arrival Time</th>
                <th>Date</th>
                <th>Capacity</th>
                <th>Price</th>
                <th>Tier</th>
                <th>Route</th>
                <th>Actions</th> <!-- Actions column for Update and Delete -->
            </tr>
        </thead>
        <tbody>
            <%
                if (trainList != null && !trainList.isEmpty()) {
                    for (Train train : trainList) {
            %>
                        <tr>
                            <td><%= train.getTrainId() %></td>
                            <td><%= train.getTrainName() %></td>
                            <td><%= train.getTrainDept() %></td>
                            <td><%= train.getTrainArr() %></td>
                            <td><%= train.getTrainDeptTime() %></td>
                            <td><%= train.getTrainArrTime() %></td>
                            <td><%= train.getTrainDate() %></td>
                            <td><%= train.getCapacity() %></td>
                            <td><%= train.getTrainPrice() %></td>
                            <td><%= train.getTrainTier() %></td>
                            <td><%= train.getTrainRoute() %></td>
                            <td>
                                <div class="d-inline-block">     
                                    <!-- Assuming you have a list of trains displayed in a table -->
									<a href="train?action=update&trainId=<%= train.getTrainId() %>" class="btn btn-warning">Edit</a>
                                </div>
                                <div class="d-inline-block m-2">
                                    <!-- Delete Form -->
                                    <form action="train" method="POST" style="display:inline;">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="trainId" value="<%= train.getTrainId() %>">
                                        <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
            <%
                    }
                } else {
            %>
                    <tr>
                        <td colspan="12" class="text-center">No trains available.</td>
                    </tr>
            <%
                }
            %>
        </tbody>
    </table>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.datatables.net/1.13.5/js/jquery.dataTables.min.js"></script>
<script>
    $(document).ready(function() {
        $('#trainTable').DataTable();
    });
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
