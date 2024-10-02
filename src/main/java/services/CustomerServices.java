package services;

import java.util.*;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import models.Customer;
import DAO.CustomerCrud; // Import the CustomerCrud class

public class CustomerServices {

    // Store error message
    private static String errorMessage = "";

    // Method to get the error message (can be retrieved by front-end)
    public static String getErrorMessage() {
        return errorMessage;
    }

    // Method for Customer registration
    public static Customer registerProfile(String userName, String email, String password, String address,
            String contactNumber) {
        try {
            errorMessage = ""; // Reset error message

            // Validation for username
            Pattern usernamecheck = Pattern.compile("^[a-zA-Z\\s]{4,49}+$");
            Matcher matcher = usernamecheck.matcher(userName);
            if (!matcher.matches()) {
                errorMessage = "User Name cannot exceed 50 characters, must be more than 3 characters, and contain only alphabets.";
                return null;
            }

            // Validation for email
            Pattern emailcheck = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
            matcher = emailcheck.matcher(email);
            if (!matcher.matches()) {
                errorMessage = "Email must be in the correct format.";
                return null;
            }

            // Validation for password
            Pattern passcheck = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$");
            matcher = passcheck.matcher(password);
            if (!matcher.matches()) {
                errorMessage = "Password must be greater than 8 characters, include at least 1 special character, and 1 capital letter.";
                return null;
            }

            // Validation for contact number
            Pattern contactcheck = Pattern.compile("^[0-9]{10}$");
            matcher = contactcheck.matcher(contactNumber);
            if (!matcher.matches()) {
                errorMessage = "Contact Number must be exactly 10 digits.";
                return null;
            }

            // If all validations pass, register a new customer using CustomerCrud
            Customer newCustomer = CustomerCrud.registerUser(userName, email, password, address, contactNumber);
            if (newCustomer == null) {
                errorMessage = "Registration failed. Username may already exist.";
            }
            return newCustomer;

        } catch (Exception e) {
            errorMessage = e.getMessage(); // Store the error message
            return null;
        }
    }

    // Method to update profile information
    public static Customer updateProfile(int userId, String userName, String email, String address,
            String contactNumber, String currentPassword, String newPassword, String confirmPassword) {
        try {
      
            errorMessage = ""; // Reset error message

            if (!newPassword.equals(confirmPassword)) {
                errorMessage = "new password and confirm password is not matching.";
                return null;
            }

            // Validation for new password
            Pattern passcheck = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$");
            Matcher matcher = passcheck.matcher(newPassword);
            if (!matcher.matches()) {
                errorMessage = "New password must be greater than 8 characters and 1 Special Character and 1 Capital Character";
                return null;
            }

            // Validation for username
            Pattern usernamecheck = Pattern.compile("^[a-zA-Z\\s]{4,49}+$");
            matcher = usernamecheck.matcher(userName);
            if (!matcher.matches()) {
                errorMessage = "User Name cannot exceed 50 characters, must be more than 3 characters, and contain only alphabets.";
                return null;
            }

            // Validation for contact number
            Pattern contactcheck = Pattern.compile("^[0-9]{10}$");
            matcher = contactcheck.matcher(contactNumber);
            if (!matcher.matches()) {
                errorMessage = "Contact Number must be exactly 10 digits.";
                return null;
            }
            System.out.print("sdsds");
            // If all validations pass, update customer details using CustomerCrud
            Customer updatedCustomer = CustomerCrud.updateUser(userId, userName, email, currentPassword, newPassword,
                    confirmPassword, address, contactNumber);
            System.out.print("hi");
            return updatedCustomer;

        } catch (Exception e) {
            errorMessage = e.getMessage(); // Store the error message
            return null;
        }
    }

    // Method to login
    public static Customer login(String userName, String password) {
        // Use the DAO method for login
        Customer customer = CustomerCrud.userLogin(userName, password);
        if (customer != null) {
            customer.setIsLoggedIn("true"); // Set login status
        } else {
            errorMessage = "Invalid credentials! Try again.";
        }
        return customer;
    }

    // Method to logout
    public static boolean logout(int userID) {
        if (CustomerCrud.logout(userID)) {
            return true;
        }
        return false;
    }

    // Method to view ALL user
    public static ArrayList<Customer> viewAllCustomer(int userId) {
        return CustomerCrud.viewAllUsers(userId);
    }

    // Method to view Profile
    public static Customer viewCustomer(int userId) {
        return CustomerCrud.viewProfile(userId);
    }

    // Method to delete profile
    public static boolean deleteProfile(int userID) throws SQLException {
        if (CustomerCrud.deleteUser(userID)) {
            return true;
        }
        return false;
    }
}
