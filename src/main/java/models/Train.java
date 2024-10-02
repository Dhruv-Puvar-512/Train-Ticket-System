package models;

public class Train {
    private int trainId;
    private String trainName;
    private String trainDept;
    private String trainArr;
    private String trainDeptTime;
    private String trainArrTime;
    private String trainDate;
    private int capacity;
    private String[] seats;  // Array of Strings to represent seat booking status
    private String trainRoute;  // Single route string
    private String trainTier;   // Train tier (e.g., First class, Second class)
    private int trainPrice;     // Price of the train ticket

    // Constructor
    public Train(int trainId, String trainName, String trainDept, String trainArr, String trainDeptTime, String trainArrTime,
                 String trainDate, int capacity, String trainRoute, String trainTier, int trainPrice) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.trainDept = trainDept;
        this.trainArr = trainArr;
        this.trainDeptTime = trainDeptTime;
        this.trainArrTime = trainArrTime;
        this.trainDate = trainDate;
        this.capacity = capacity;
        this.seats = initializeSeats(capacity);  // Initialize seats as "false"
        this.trainRoute = trainRoute;
        this.trainTier = trainTier;
        this.trainPrice = trainPrice;
        System.out.println("Train Registered: ID = " + this.trainId);
    }

    // Method to initialize seats with "false" (unbooked)
    private String[] initializeSeats(int capacity) {
        String[] seats = new String[capacity];
        for (int i = 0; i < seats.length; i++) {
            seats[i] = "false";  // Initially mark all seats as "false" (unbooked)
        }
        return seats;
    }

    // Getters and Setters
    public int getTrainId() {
        return trainId;
    }
    
    public void setTrainId(int trainId) {
        this.trainId=trainId ;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getTrainDept() {
        return trainDept;
    }

    public void setTrainDept(String trainDept) {
        this.trainDept = trainDept;
    }

    public String getTrainArr() {
        return trainArr;
    }

    public void setTrainArr(String trainArr) {
        this.trainArr = trainArr;
    }

    public String getTrainDeptTime() {
        return trainDeptTime;
    }

    public void setTrainDeptTime(String trainDeptTime) {
        this.trainDeptTime = trainDeptTime;
    }

    public String getTrainArrTime() {
        return trainArrTime;
    }

    public void setTrainArrTime(String trainArrTime) {
        this.trainArrTime = trainArrTime;
    }

    public String getTrainDate() {
        return trainDate;
    }

    public void setTrainDate(String trainDate) {
        this.trainDate = trainDate;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
        this.seats = initializeSeats(capacity); // Reset seats array with new capacity
    }

    public String[] getSeats() {
        return seats;
    }

    public void setSeats(String[] seats) {
        this.seats = seats;
    }

    public String getTrainRoute() {
        return trainRoute;
    }

    public void setTrainRoute(String trainRoute) {
        this.trainRoute = trainRoute;
    }

    public String getTrainTier() {
        return trainTier;
    }

    public void setTrainTier(String trainTier) {
        this.trainTier = trainTier;
    }

    public int getTrainPrice() {
        return trainPrice;
    }

    public void setTrainPrice(int trainPrice) {
        this.trainPrice = trainPrice;
    }
}
