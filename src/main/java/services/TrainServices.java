package services;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import models.Train;
import DAO.TrainCrud;

public class TrainServices {

    public static String errorMessage = ""; // Store error messages

    // Method to register a train (uses setters for input validation)
    public static Train registerTrain(String trainName, String trainDept, String trainArr,
            String trainDeptTime, String trainArrTime, String trainDate, int capacity,
            String trainRoute, String trainTier, int trainPrice) {
        
        try {
//            // Validation example using regex (adjust patterns as needed)
//            Pattern timePattern = Pattern.compile("^(?:[01]\\d|2[0-3]):[0-5]\\d$");
//            Matcher matchDeptTime = timePattern.matcher(trainDeptTime);
//            Matcher matchArrTime = timePattern.matcher(trainArrTime);
//            
//            if (!matchDeptTime.matches() || !matchArrTime.matches()) {
//                errorMessage = "Invalid time format. Please use HH:mm.";
//                return null;
//            }
            System.out.print("sxsx");
            Train t = TrainCrud.registerTrain(trainName, trainDept, trainArr, trainDeptTime,
                    trainArrTime, trainDate, capacity, trainRoute, trainTier, trainPrice);

            if (t != null) {
            	System.out.print("Train Registered Successfully");
                return t;
            } else {
                errorMessage = "Train registration failed.";
                return null;
            }
        } catch (Exception e) {
            errorMessage = "Error during train registration: " + e.getMessage();
            return null;
        }
    }

    // Method to update a train using setters (no direct input in method)
    public static Train updateTrain(int trainId, String trainName, String trainDept, String trainArr,
            String trainDeptTime, String trainArrTime, String trainDate, int capacity,
            String trainRoute, String trainTier, int trainPrice) {

        try {
//            Pattern timePattern = Pattern.compile("^(?:[01]\\d|2[0-3]):[0-5]\\d$");
//            Matcher matchDeptTime = timePattern.matcher(trainDeptTime);
//            Matcher matchArrTime = timePattern.matcher(trainArrTime);
//
//            if (!matchDeptTime.matches() || !matchArrTime.matches()) {
//                errorMessage = "Invalid time format. Please use HH:mm.";
//                return null;
//            }

            Train t = TrainCrud.updateTrain(trainId, trainName, trainDept, trainArr, trainDeptTime,
                    trainArrTime, trainDate, capacity, trainRoute, trainTier, trainPrice);

            if (t != null) {
                return t;
            } else {
                errorMessage = "Train update failed.";
                return null;
            }
        } catch (Exception e) {
            errorMessage = "Error during train update: " + e.getMessage();
            return null;
        }
    }

    // Method to delete a train (returns boolean)
    public static boolean deleteTrain(int trainId) {
        try {
            boolean isDeleted = TrainCrud.deleteTrain(trainId);

            if (!isDeleted) {
                errorMessage = "Failed to delete train.";
                return false;
            }
            return true;
        } catch (Exception e) {
            errorMessage = "Error during train deletion: " + e.getMessage();
            return false;
        }
    }

    // Method to display all trains
    public static ArrayList<Train> displayAllTrains() {
        try {
            ArrayList<Train> trains = TrainCrud.viewAllTrain();

            if (trains == null || trains.isEmpty()) {
                errorMessage = "No trains available.";
                return trains;
            }
            return trains;
        } catch (Exception e) {
            errorMessage = "Error fetching train data: " + e.getMessage();
            return null;
        }
    }

    // Method to search and display trains based on departure and arrival
    public static ArrayList<Train> searchAndDisplayTrains(String departureLocation, String arrivalLocation) {
        try {
            ArrayList<Train> trains = TrainCrud.viewSpecificTrain(departureLocation, arrivalLocation);

            if (trains == null || trains.isEmpty()) {
                errorMessage = "No trains found for the specified route.";
                return trains;
            }
            return trains;
        } catch (Exception e) {
            errorMessage = "Error fetching trains for the specified route: " + e.getMessage();
            return null;
        }
    }
}
