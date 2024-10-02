package views;

import java.sql.Connection;

import utils.DatabaseConnection;

public class main {

    public static void main(String[] args) {
        // Your code goes here
        System.out.println("Hello, World!");
        Connection con =  DatabaseConnection.DBconnect_crud();
        System.out.print(con);
    }
}