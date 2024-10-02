<%@ page session="true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="models.Train" %>
<%@ page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Book Tickets</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- DataTables CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.11.5/css/jquery.dataTables.min.css">
</head>
<body>

<div class="container mt-5">
    <h2>Book Train Tickets</h2>

    <!-- Form to search trains by Departure and Arrival -->
    <form action="train" method="GET">
        <input type="hidden" name="action" value="viewSpecificTrain">
        <div class="mb-3">
            <label for="trainDept" class="form-label">Departure Location</label>
            <input type="text" class="form-control" id="trainDept" name="trainDept" placeholder="Enter departure location" required>
        </div>
        <div class="mb-3">
            <label for="trainArr" class="form-label">Arrival Location</label>
            <input type="text" class="form-control" id="trainArr" name="trainArr" placeholder="Enter arrival location" required>
        </div>
        <button type="submit" class="btn btn-primary">Search Trains</button>
    </form>

    <!-- Display Train Details (if any) after search -->
    <div class="mt-5">
        <h3>Available Trains</h3>
        <%
            ArrayList<Train> trains = (ArrayList<Train>) session.getAttribute("train");
            if (trains != null && !trains.isEmpty()) {
        %>
        <table id="trainTable" class="table table-bordered table-hover">
            <thead>
                <tr>
                    <th>Train Name</th>
                    <th>Departure</th>
                    <th>Arrival</th>
                    <th>Departure Time</th>
                    <th>Arrival Time</th>
                    <th>Date</th>
                    <th>Capacity</th>
                    <th>Route</th>
                    <th>Tier</th>
                    <th>Price</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <% for (Train train : trains) { %>
                <tr>
                    <td><%= train.getTrainName() %></td>
                    <td><%= train.getTrainDept() %></td>
                    <td><%= train.getTrainArr() %></td>
                    <td><%= train.getTrainDeptTime() %></td>
                    <td><%= train.getTrainArrTime() %></td>
                    <td><%= train.getTrainDate() %></td>
                    <td><%= train.getCapacity() %></td>
                    <td><%= train.getTrainRoute() %></td>
                    <td><%= train.getTrainTier() %></td>
                    <td><%= train.getTrainPrice() %></td>
                    <td>
					       <!-- Book button -->
					    <form action="train" method="POST">
					        <input type="hidden" name="action" value="bookTrain">
					        <input type="hidden" name="trainId" value="<%= train.getTrainId() %>">
					        <button type="submit" class="btn btn-success">Book</button>
					    </form>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
        <% } else if (session.getAttribute("error") != null) { %>
            <p class="text-danger"><%= session.getAttribute("error") %></p>
        <% } else { %>
            <p>No trains found. Please try a different search.</p>
        <% } %>
    </div>
</div>

<!-- jQuery and DataTables JS -->
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>

<!-- DataTables Initialization -->
<script>
    $(document).ready(function() {
        $('#trainTable').DataTable();
    });
</script>

</body>
</html>
