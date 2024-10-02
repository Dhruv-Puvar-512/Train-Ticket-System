package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.Customer;
import utils.DatabaseConnection;
import java.util.*;

public class CustomerCrud {

	// Method to register a new user
	public static Customer registerUser(String userName, String email, String password, String address,
			String contactNumber) throws SQLException {

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			connection = DatabaseConnection.DBconnect_crud();

			// Check if the user with the same username already exists
			String checkUserQuery = "SELECT userId FROM CUSTOMER WHERE userName = ?";
			PreparedStatement checkStmt = connection.prepareStatement(checkUserQuery);
			checkStmt.setString(1, userName);
			rs = checkStmt.executeQuery();

		
			if (rs.next()) {
				// If a user is found with the same username, return null and set an error
				// message
				String errorMessage = "Username already exists. Please choose a different one.";
				System.out.println(errorMessage); // Or handle it as needed (e.g., return to the frontend)
				return null; // Or handle the error message to be returned
			} else {

				// If no user with the same username, proceed to insert the new user
				String insertUserQuery = "INSERT INTO CUSTOMER (userName, email, password, address, contactNumber, isAdmin, isLoggedIn, softDelete) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
				stmt = connection.prepareStatement(insertUserQuery);

				stmt.setString(1, userName);
				stmt.setString(2, email);
				stmt.setString(3, password);
				stmt.setString(4, address);
				stmt.setString(5, contactNumber);
				stmt.setString(6, "false"); // Default: not admin
				stmt.setString(7, "false"); // Default: not logged in
				stmt.setString(8, "false"); // Default: not soft deleted

				int count = stmt.executeUpdate();

				// If insertion is successful, fetch the new userId and return the customer
				// instance
				if (count == 1) {
					String fetchUserIdQuery = "SELECT userId FROM CUSTOMER WHERE userName = ?";
					PreparedStatement fetchStmt = connection.prepareStatement(fetchUserIdQuery);
					fetchStmt.setString(1, userName);
					rs = fetchStmt.executeQuery();

					if (rs.next()) {
						int userId = rs.getInt("userId");

						// Return the created customer instance
						return new Customer(userId, userName, email, password, address, contactNumber);
					} else {
						// Handle any issue in fetching the userId (though unlikely)
						System.out.println("Error fetching newly created user's ID.");
						return null;
					}
				} else {
					// Handle failure to insert the user
					System.out.println("Failed to register the user.");
					return null;
				}
			}

		} catch (Exception e) {
			// Handle SQL exceptions and print the error message
			System.out.println("An error occurred during user registration: " + e.getMessage());
			return null;
		} finally {
			// Close the ResultSet, PreparedStatement, and Connection resources
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (connection != null)
				connection.close();
		}
	}

	public static boolean registerSeats(int trainId, int capacity) throws SQLException {
		try {
			int count = 0;
			for (int i = 0; i < capacity; i++) {
				String query = "INSERT INTO SEATS (trainId, SeatNo, status) VALUES (?, ?, ?)";
				Connection connection = DatabaseConnection.DBconnect_crud();
				PreparedStatement stmt = connection.prepareStatement(query);
				stmt.setInt(1, trainId);
				stmt.setInt(2, i);
				stmt.setString(3, "false"); // Initially set status to "false"

				count += stmt.executeUpdate();
				connection.close();
			}
			return count == capacity;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	// Method to update an existing user's information based on old password and
	// login status
	public static Customer updateUser(int userId, String userName, String email, String oldPassword, String newPassword,
			String confirmPassword, String address, String contactNumber) throws SQLException {

		Connection connection = null;
		PreparedStatement stmt = null;

		try {
			connection = DatabaseConnection.DBconnect_crud();

			// Check if the newPassword matches the confirmPassword
			if (!newPassword.equals(confirmPassword)) {
				System.out.println("Internal Server Error: New password and confirm password do not match.");
				return null;
			}

			// Update user details if userId, oldPassword, and isLoggedIn=true
			String updateQuery = "UPDATE CUSTOMER SET email=?, password=?, address=?, contactNumber=? WHERE userId=? AND password=? AND isLoggedIn='true'";
			stmt = connection.prepareStatement(updateQuery);

			stmt.setString(1, email);
			stmt.setString(2, newPassword); // Update with new password
			stmt.setString(3, address);
			stmt.setString(4, contactNumber);
			stmt.setInt(5, userId); // Identify user with userId
			stmt.setString(6, oldPassword); // Ensure old password matches

			// Execute the update query
			int count = stmt.executeUpdate();

			// If update is successful, return a new instance of the updated Customer
			if (count == 1) {
				Customer Cust = new Customer(userId, userName, email, newPassword, address, contactNumber);
				Cust.setIsLoggedIn("true");
				if(userId==1) {
					Cust.setIsAdmin("true");					
				}
				return Cust;
			} else {
				// If update failed, return null and log error message
				System.out.println(
						"Internal Server Error: Failed to update user. Invalid credentials or user not logged in.");
				return null;
			}

		} catch (Exception e) {
			// Handle SQL or any other exceptions
			System.out.println("An error occurred during user update: " + e.getMessage());
			return null;
		} finally {
			// Close the PreparedStatement and Connection resources
			if (stmt != null)
				stmt.close();
			if (connection != null)
				connection.close();
		}
	}

	public static boolean deleteUser(int userId) throws SQLException {
		Connection connection = null;
		PreparedStatement stmt = null;

		try {
			connection = DatabaseConnection.DBconnect_crud();

			// Proceed to soft delete the user
			String softDeleteQuery = "UPDATE CUSTOMER SET softDelete = 'true', isLoggedIn = 'false' WHERE userId = ?";
			stmt = connection.prepareStatement(softDeleteQuery);
			stmt.setInt(1, userId);

			int count = stmt.executeUpdate();

			// If the soft delete was successful, return true
			return count == 1;

		} catch (Exception e) {
			// Handle SQL or any other exceptions
			System.out.println("An error occurred during user soft deletion: " + e.getMessage());
			return false;
		} finally {
			// Close resources
			if (stmt != null)
				stmt.close();
			if (connection != null)
				connection.close();
		}
	}

	// View all trains
	public void viewAllTrain() throws SQLException {
		try {
			String query = "SELECT * FROM TRAIN";
			Connection connection = DatabaseConnection.DBconnect_crud();
			PreparedStatement stmt = connection.prepareStatement(query);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				System.out.println("Train Name: " + rs.getString("trainName"));
				System.out.println("Departure: " + rs.getString("trainDept"));
				System.out.println("Arrival: " + rs.getString("trainArr"));
				System.out.println("Departure Time: " + rs.getString("trainDeptTime"));
				System.out.println("Arrival Time: " + rs.getString("trainArrTime"));
				System.out.println("Date: " + rs.getString("trainDate"));
				System.out.println("Capacity: " + rs.getInt("capacity"));
				System.out.println("Route: " + rs.getString("trainRoute")); // Display train route
				System.out.println("Tier: " + rs.getString("trainTier")); // Display train tier
				System.out.println("Price: " + rs.getInt("trainPrice")); // Display train price
				System.out.println("----------------------------------------");
			}
			connection.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// Method to view user profile by userId
	public static Customer viewProfile(int userId) {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			connection = DatabaseConnection.DBconnect_crud();

			// Query to fetch user details
			String query = "SELECT * FROM CUSTOMER WHERE userId = ? AND softDelete = 'false'";
			stmt = connection.prepareStatement(query);
			stmt.setInt(1, userId);

			rs = stmt.executeQuery();

			// If a user is found, create and return a Customer object
			if (rs.next()) {
				return new Customer(rs.getInt("userId"), rs.getString("userName"), rs.getString("email"),
						rs.getString("password"), rs.getString("address"), rs.getString("contactNumber"));
			} else {
				System.out.println("User not found or has been soft deleted.");
				return null;
			}

		} catch (SQLException e) {
			System.out.println("An error occurred while fetching user profile: " + e.getMessage());
			return null;
		} finally {
			// Close resources
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("Error closing resources: " + e.getMessage());
			}
		}
	}

	// Method to view all user profiles if the specified user is logged in and an
	// admin
	public static ArrayList<Customer> viewAllUsers(int userId) {
		ArrayList<Customer> allUsers = new ArrayList<Customer>();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
		
			connection = DatabaseConnection.DBconnect_crud();

			// Check if the user is logged in and is an admin
			String checkUserQuery = "SELECT isAdmin, isLoggedIn FROM CUSTOMER WHERE userId = ?";
			stmt = connection.prepareStatement(checkUserQuery);
			stmt.setInt(1, userId);
			rs = stmt.executeQuery();

			// If the user is found, check their admin and login status
			if (rs.next()) {
				String isAdmin = rs.getString("isAdmin");
				String isLoggedIn = rs.getString("isLoggedIn");

				if (isAdmin.equals("true") && isLoggedIn.equals("true")) {
					// User is an admin and logged in, proceed to fetch all users
					stmt.close(); // Close previous statement

					// Query to fetch all users
					String query = "SELECT * FROM CUSTOMER";
					stmt = connection.prepareStatement(query);
					rs = stmt.executeQuery();

					// Create Customer instances for each user found
					while (rs.next()) {
						Customer user = new Customer(rs.getInt("userId"), rs.getString("userName"),
								rs.getString("email"), rs.getString("password"), rs.getString("address"),
								rs.getString("contactNumber"));
						allUsers.add(user);
					}
				} else {
					System.out.println("Access denied. User must be logged in and an admin.");
				}
			} else {
				System.out.println("User not found with userId: " + userId);
			}

		} catch (SQLException e) {
			System.out.println("An error occurred while fetching user profiles: " + e.getMessage());
		} finally {
			// Close resources
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("Error closing resources: " + e.getMessage());
			}
		}

		return allUsers; // Return the list of users or an empty list if access is denied
	}

	public static Customer userLogin(String username, String password) {
		Connection connection = null;
		PreparedStatement updateStmt = null;
		PreparedStatement selectStmt = null;
		ResultSet rs = null;

		try {
			connection = DatabaseConnection.DBconnect_crud();

			// Step 1: Update isLoggedIn status
			String updateQuery = "UPDATE CUSTOMER SET isLoggedIn = 'true' WHERE userName = ? AND password = ?";
			updateStmt = connection.prepareStatement(updateQuery);
			updateStmt.setString(1, username);
			updateStmt.setString(2, password);

			int updateCount = updateStmt.executeUpdate();

			// Step 2: If update successful, fetch the customer details
			if (updateCount == 1) {
				// Prepare the select query to fetch the user details
				String selectQuery = "SELECT * FROM CUSTOMER WHERE userName = ? AND password = ?";
				selectStmt = connection.prepareStatement(selectQuery);
				selectStmt.setString(1, username);
				selectStmt.setString(2, password);
				rs = selectStmt.executeQuery();

				// Step 3: If user exists, create and return the Customer object
				if (rs.next()) {
					int userId = rs.getInt("userId");
					String email = rs.getString("email");
					String address = rs.getString("address");
					String contactNumber = rs.getString("contactNumber");
					String isAdmin =  rs.getString("isAdmin");
					Customer Cust = new Customer(userId, username, email, password, address, contactNumber);
					Cust.setIsAdmin(isAdmin);
					return Cust;
				}
			} else {
				System.out.println("Invalid username or password.");
				return null; // User not found or invalid credentials
			}

		} catch (SQLException e) {
			System.out.println("An error occurred during user login: " + e.getMessage());
			return null; // Internal error
		} finally {
			// Close resources
			try {
				if (rs != null)
					rs.close();
				if (selectStmt != null)
					selectStmt.close();
				if (updateStmt != null)
					updateStmt.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("Error closing resources: " + e.getMessage());
			}
		}

		return null; // Return null if no customer was found or an error occurred
	}

	public static boolean logout(int userId) {
		Connection connection = null;
		PreparedStatement stmt = null;

		try {
			connection = DatabaseConnection.DBconnect_crud();

			// Prepare the update query to set isLoggedIn to false
			String updateQuery = "UPDATE CUSTOMER SET isLoggedIn = 'false' WHERE userId = ?";
			stmt = connection.prepareStatement(updateQuery);
			stmt.setInt(1, userId);

			// Execute the update query
			int count = stmt.executeUpdate();

			// Return true if one row was updated, indicating a successful logout
			return count == 1;

		} catch (SQLException e) {
			System.out.println("An error occurred during logout: " + e.getMessage());
			return false; // Indicate failure in case of exception
		} finally {
			// Close resources
			try {
				if (stmt != null)
					stmt.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				System.out.println("Error closing resources: " + e.getMessage());
			}
		}
	}

	// Method to fetch current user details for pre-filling the update form
	public static Customer getUserById(int userId) throws SQLException {
	    Connection connection = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    try {
	        connection = DatabaseConnection.DBconnect_crud();

	        // Query to fetch the user details by userId
	        String query = "SELECT * FROM CUSTOMER WHERE userId = ?";
	        stmt = connection.prepareStatement(query);
	        stmt.setInt(1, userId);

	        rs = stmt.executeQuery();

	        // If user is found, return the customer object
	        if (rs.next()) {
	            return new Customer(rs.getInt("userId"), rs.getString("userName"),
	                    rs.getString("email"), rs.getString("password"),
	                    rs.getString("address"), rs.getString("contactNumber"));
	        } else {
	            System.out.println("User not found with userId: " + userId);
	            return null;
	        }

	    } catch (SQLException e) {
	        System.out.println("An error occurred while fetching user details: " + e.getMessage());
	        return null;
	    } finally {
	        if (rs != null)
	            rs.close();
	        if (stmt != null)
	            stmt.close();
	        if (connection != null)
	            connection.close();
	    }
	}

}