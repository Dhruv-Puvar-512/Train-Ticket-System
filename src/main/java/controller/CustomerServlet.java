package controller;

import services.CustomerServices;
import models.Customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/customer")
public class CustomerServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "login":
                handleLogin(request, response);
                break;
            case "register":
                handleRegister(request, response);
                break;
            case "update":
                handleUpdate(request, response);
                break;
            case "delete":
                handleDelete(request, response);
                break;
            case "viewProfile":
                handleViewProfile(request, response);
                break;
            case "viewAll":
                handleViewAllUsers(request, response);
                break;
            case "logout":
                handleLogout(request, response);
                break;
            default:
                response.sendRedirect("errorPage.jsp");
                break;
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        // Call the login method
        Customer customer = CustomerServices.login(userName, password);

        // Check if login was successful
        if (customer != null) {
            // Create a session and set customer object in session
            HttpSession session = request.getSession();
            session.setAttribute("customer", customer); // Store the entire customer object in session

            // Redirect or forward to a success page
            request.setAttribute("loginMessage", "Login successful!");
            response.sendRedirect("homePage.jsp");
        } else {
        	 // Set error message and forward to login page
            String errorMessage = CustomerServices.getErrorMessage();
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("loginPage.jsp").forward(request, response); // Forward back to login page
      
        }
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Extract registration details from request
        String userName = request.getParameter("userName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String address = request.getParameter("address");
        String contactNumber = request.getParameter("contactNumber");

        // Register the user
        Customer newCustomer = CustomerServices.registerProfile(userName, email, password, address, contactNumber);

        if (newCustomer != null) {
            request.setAttribute("registrationMessage", "Registration successful!");
            response.sendRedirect("loginPage.jsp"); // Redirect to login page
        } else {
            // Set error message and forward to registration page
            String errorMessage = CustomerServices.getErrorMessage();
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("registerPage.jsp").forward(request, response); // Forward back to registration page
        }
    }


    private void handleUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the customer object from session
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            response.sendRedirect("loginPage.jsp");
            return;
        }

        // Extract updated details from request
        String userName = request.getParameter("userName");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String contactNumber = request.getParameter("contactNumber");
        String currentPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Update the user profile
        Customer updatedCustomer = CustomerServices.updateProfile(customer.getUserId(), userName, email, address,
                contactNumber, currentPassword, newPassword, confirmPassword);

        if (updatedCustomer != null) {
            session.setAttribute("customer", updatedCustomer); // Update session with new customer details
            request.setAttribute("updateMessage", "Profile updated successfully!");
            request.getRequestDispatcher("viewProfile.jsp").forward(request, response);
        } else {
            // Set error message and forward to update profile page
            String errorMessage = CustomerServices.getErrorMessage();
            if(errorMessage.equals("")) {
            	errorMessage="Old Password Doesn't Match";
            }
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("updateProfile.jsp").forward(request, response);
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            response.sendRedirect("loginPage.jsp");
            return;
        }

        // Delete the user profile
        boolean isDeleted = false;
        try {
            isDeleted = CustomerServices.deleteProfile(customer.getUserId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (isDeleted) {
            request.setAttribute("deleteMessage", "Profile deleted successfully!");
            session.invalidate(); // Invalidate session after deletion
            response.sendRedirect("registerPage.jsp"); // Redirect to register page
        } else {
            request.setAttribute("deleteMessage", "Failed to delete profile.");
            request.getRequestDispatcher("loginPage.jsp").forward(request, response);
        }
    }

    private void handleViewProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            response.sendRedirect("loginPage.jsp");
            return;
        }

        // Fetch user profile
        request.setAttribute("customerProfile", customer);
        request.getRequestDispatcher("viewProfile.jsp").forward(request, response); // Forward to profile page
    }

    private void handleViewAllUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null || !customer.isAdmin().equals("true")) {
            response.sendRedirect("errorPage.jsp"); // Admin access only
            return;
        }

        // Fetch all users
        ArrayList<Customer> allUsers = CustomerServices.viewAllCustomer(customer.getUserId());

        request.setAttribute("customerList", allUsers); // Corrected line
        request.getRequestDispatcher("viewAllUsers.jsp").forward(request, response); // Forward to the "view all users" page
    }


    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Get existing session, do not create a new one
        if (session != null) {
            session.invalidate(); // Invalidate the session
        }
        response.sendRedirect("loginPage.jsp"); // Redirect to login page after logout
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle GET requests if needed
    }
}
