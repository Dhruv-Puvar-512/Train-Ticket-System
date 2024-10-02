package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import models.Train;
import utils.DatabaseConnection;

public class TrainCrud {

    // Register a new train
    public static Train registerTrain(String trainName, String trainDept, String trainArr, String trainDeptTime,
            String trainArrTime, String trainDate, int capacity, String trainRoute, String trainTier, int trainPrice)
            throws SQLException {

        try {
            String query = "INSERT INTO TRAIN (trainName, trainDept, trainArr, trainDeptTime, trainArrTime, trainDate, capacity, trainRoute, trainTier, trainPrice) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            Connection connection = DatabaseConnection.DBconnect_crud();
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1, trainName);
            stmt.setString(2, trainDept);
            stmt.setString(3, trainArr);
            stmt.setString(4, trainDeptTime);
            stmt.setString(5, trainArrTime);
            stmt.setString(6, trainDate);
            stmt.setInt(7, capacity);
            stmt.setString(8, trainRoute);
            stmt.setString(9, trainTier);
            stmt.setInt(10, trainPrice);

            int count = stmt.executeUpdate();
            System.out.print("cevrvrvr");
            if (count == 1) {
                String fetchIdQuery = "SELECT trainId FROM TRAIN WHERE trainName = ? AND trainDeptTime = ?";
                PreparedStatement fetchStmt = connection.prepareStatement(fetchIdQuery);
                fetchStmt.setString(1, trainName);
                fetchStmt.setString(2, trainDeptTime);
                ResultSet rs = fetchStmt.executeQuery();

                if (rs.next()) {
                    int trainId = rs.getInt("trainId");
                    boolean seatBool = registerSeats(trainId, capacity);

                    if (seatBool) {
                        connection.close();
                        return new Train(trainId, trainName, trainDept, trainArr, trainDeptTime, trainArrTime, trainDate, capacity, trainRoute, trainTier, trainPrice);
                    }
                }
            }
            connection.close();
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
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
   


    public static Train getTrainById(int trainId) throws SQLException {
        Train train = null;
        try {
            System.out.println("Fetching train by ID: " + trainId);
            String query = "SELECT * FROM TRAIN WHERE trainId = ?";
            Connection connection = DatabaseConnection.DBconnect_crud();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, trainId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                train = new Train(
                    rs.getInt("trainId"),
                    rs.getString("trainName"),
                    rs.getString("trainDept"),
                    rs.getString("trainArr"),
                    rs.getString("trainDeptTime"),
                    rs.getString("trainArrTime"),
                    rs.getString("trainDate"),
                    rs.getInt("capacity"),
                    rs.getString("trainRoute"),
                    rs.getString("trainTier"),
                    rs.getInt("trainPrice")
                );
                System.out.println("Train found: " + train.getTrainName());
            } else {
                System.out.println("No train found with ID: " + trainId);
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error fetching train: " + e.getMessage());
            throw e; // Propagate the error to be caught later
        }
        return train;
    }

    
    // Update train after fetching details
    public static Train updateTrain(int trainId, String trainName, String trainDept, String trainArr,
            String trainDeptTime, String trainArrTime, String trainDate, int capacity, String trainRoute,
            String trainTier, int trainPrice) throws SQLException {

        try {
            String query = "UPDATE TRAIN SET trainName=?, trainDept=?, trainArr=?, trainDeptTime=?, trainArrTime=?, trainDate=?, capacity=?, trainRoute=?, trainTier=?, trainPrice=? WHERE trainId=?";
            Connection connection = DatabaseConnection.DBconnect_crud();
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1, trainName);
            stmt.setString(2, trainDept);
            stmt.setString(3, trainArr);
            stmt.setString(4, trainDeptTime);
            stmt.setString(5, trainArrTime);
            stmt.setString(6, trainDate);
            stmt.setInt(7, capacity);
            stmt.setString(8, trainRoute);
            stmt.setString(9, trainTier);
            stmt.setInt(10, trainPrice);
            stmt.setInt(11, trainId);

            boolean seatBool = updateSeats(trainId, capacity);
            int count = stmt.executeUpdate();

            if (seatBool && count > 0) {
                connection.close();
                return new Train(trainId, trainName, trainDept, trainArr, trainDeptTime, trainArrTime, trainDate, capacity, trainRoute, trainTier, trainPrice);
            }
            connection.close();
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    
       
    public static boolean updateSeats(int trainId, int capacity) throws SQLException {
        try {
            int count = 0;
            for (int i = 0; i < capacity; i++) {
                String query = "UPDATE SEATS SET status=? WHERE trainId=? AND SeatNo=?";
                Connection connection = DatabaseConnection.DBconnect_crud();
                PreparedStatement stmt = connection.prepareStatement(query);

                stmt.setString(1, "false"); // Set status to "false"
                stmt.setInt(2, trainId);
                stmt.setInt(3, i);

                count += stmt.executeUpdate();
                connection.close();
            }
            return count == capacity;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // View all trains and return an ArrayList of Train objects
    public static ArrayList<Train> viewAllTrain() throws SQLException {
        ArrayList<Train> trains = new ArrayList<>();
        try {
            String query = "SELECT * FROM TRAIN";
            Connection connection = DatabaseConnection.DBconnect_crud();
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Train train = new Train(rs.getInt("trainId"), rs.getString("trainName"), rs.getString("trainDept"), rs.getString("trainArr"),
                        rs.getString("trainDeptTime"), rs.getString("trainArrTime"), rs.getString("trainDate"), rs.getInt("capacity"),
                        rs.getString("trainRoute"), rs.getString("trainTier"), rs.getInt("trainPrice"));
                trains.add(train);
            }
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return trains;
    }

    // View specific trains by departure and arrival, return an ArrayList of Train objects
    public static ArrayList<Train> viewSpecificTrain(String trainDept, String trainArr) throws SQLException {
        ArrayList<Train> trains = new ArrayList<>();
        try {
            String query = "SELECT * FROM TRAIN WHERE trainDept=? AND trainArr=?";
            Connection connection = DatabaseConnection.DBconnect_crud();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, trainDept);
            stmt.setString(2, trainArr);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Train train = new Train(rs.getInt("trainId"), rs.getString("trainName"), rs.getString("trainDept"), rs.getString("trainArr"),
                        rs.getString("trainDeptTime"), rs.getString("trainArrTime"), rs.getString("trainDate"), rs.getInt("capacity"),
                        rs.getString("trainRoute"), rs.getString("trainTier"), rs.getInt("trainPrice"));
                trains.add(train);
            }
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return trains;
    }

    // Delete train, return boolean for success or failure
    public static boolean deleteTrain(int trainId) {
        try {
            String query = "DELETE FROM TRAIN WHERE trainId=?";
            Connection connection = DatabaseConnection.DBconnect_crud();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, trainId);
            int count = stmt.executeUpdate();
            connection.close();
            return count > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
