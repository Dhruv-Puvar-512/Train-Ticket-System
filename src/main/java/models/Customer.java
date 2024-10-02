package models;

public class Customer {

    private String userName;
    private String email;
    private String password;
    private String address;
    private String contactNumber;
    private int userId;
    private String isAdmin; // New variable for Admin rights
    private String isLoggedIn; // Login Check
    private String softDelete; // Flag for soft delete

    // Constructor for Customer registration
    public Customer(int userId, String userName, String email, String password, String address, String contactNumber) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.address = address;
        this.contactNumber = contactNumber;
        this.isAdmin = "false";
        this.isLoggedIn = "false";
        this.softDelete = "false";

        System.out.println("You are registered successfully. ID: " + this.userId);
    }

    // Getters
    public String getUserName() {
        return userName;
    }

    public String getIsLoggedIn() {
        return isLoggedIn;
    }

    public String getSoftDelete() {
        return softDelete;
    }

    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String isAdmin() {
        return isAdmin;
    }

    // Setters
    public void setIsAdmin(String isAdmin) {
        this.isAdmin=isAdmin;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setSoftDelete(String softDelete) {
        this.softDelete = softDelete;
    }

    public void setIsLoggedIn(String isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

}
