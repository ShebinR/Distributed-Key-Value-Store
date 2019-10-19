package utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class DBConnection {

    public Connection conn = null;

    public DBConnection(String connectionUrl){
        this.conn = connect(connectionUrl);
    }

    /**
     * This method establishes a JDBC connection with the sqlite3 db mentioned
     * in the connection url
     * @param connectionUrl - The connection url
     * @return JDBC connection object
     */
    private Connection connect(String connectionUrl){
        System.out.println("Using DB : " + connectionUrl);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    /**
     * Method to insert new key-value sets into the table
     * @param key - Key to insert into the hashtable
     * @param value - Value to insert into the hash table
     */
    public int insert(String key, String value, long timeStamp) {
        String query = "INSERT INTO "+ Constants.TABLE_NAME + " VALUES(" + timeStamp + ",'" + key + "','" + value + "')";
        System.out.println("TS : " + timeStamp);
        int result;
        try{
            System.out.println("query : " + query);
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
            result = 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            result = Constants.OPERATION_FAILURE_STATUS_CODE;
        }
        return result;
    }

    /**
     * Method to fetch the value of a given key input
     * @param key - the input key to search for
     * @return The fetched value
     */
    public String select(String key){
        String query = "SELECT value FROM " + Constants.TABLE_NAME + " WHERE key = '" + key + "'";
        ResultSet rs;
        String value = null;
        try{
            System.out.println("query : " + query);
            PreparedStatement pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                value = rs.getString("value");
                break;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return value;
    }

    /**
     * This method selects all values from the table and creates an in-memory hash table
     * @return The hash table containing parsed key value pairs from db
     */
    public HashMap<String, String> selectAll(){
        String query = "SELECT * FROM " +  Constants.TABLE_NAME;
        ResultSet rs;
        HashMap<String, String> values = null;
        try{
            System.out.println("query : " + query);
            PreparedStatement pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            values = new HashMap<>();
            while (rs.next()) {
                values.put(rs.getString( "key"), rs.getString("value"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            values = null;
        }
        return values;
    }

    /**
     * Method that executes an update query to change the value of a given key
     * @param key - Key to update
     * @param value - new value to update in the given key
     */
    public int update(String key, String value, long timeStamp){

        String query = "UPDATE " + Constants.TABLE_NAME + " SET value = '" + value + "'" +
                ", timestamp = " + timeStamp
                + " WHERE key = '" + key + "'";
        System.out.println("TS : " + timeStamp);
        int result;
        try {
            System.out.println("query : " + query);
            PreparedStatement pstmt = conn.prepareStatement(query);
            // update
            pstmt.executeUpdate();
            result = 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            result = Constants.OPERATION_FAILURE_STATUS_CODE;
        }
        return result;
    }

    /**
     * Method that executes delete query to delete a record with the given key
     * @param key - The key to remove from in the DB
     * @return - The response code
     */
    public int delete(String key){
        String query = "DELETE FROM " + Constants.TABLE_NAME + " WHERE key = '" + key +"'";
        int result;
        try {
            System.out.println("query : " + query);
            PreparedStatement pstmt = conn.prepareStatement(query);
            // execute the delete statement
            pstmt.executeUpdate();
            result = 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            result =Constants.OPERATION_FAILURE_STATUS_CODE;
        }
        return result;
    }

    /**
     * Method to disconnect jdbc connection to sqlite3 db
     */
    public void disconnect(){
        if(conn == null){
            return;
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    public long getLastTimeStamp() {
        String query = "SELECT MAX(timestamp) FROM "+Constants.TABLE_NAME;
        ResultSet rs;
        long result = Constants.OPERATION_FAILURE_STATUS_CODE;
        try {
            System.out.println("query : " + query);
            PreparedStatement pstmt = conn.prepareStatement(query);
            // execute the delete statement
            rs = pstmt.executeQuery();
            if(rs.next()) {
                result = rs.getLong(1);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    /**
     *
     * @param lastKnownTS -  The last known timestamp of the data node
     * @return -
     */
    public List<String> getRecordsAfterSequenceNumber(long lastKnownTS) {
        String query = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE timestamp > " + lastKnownTS;
        List<String> records = new ArrayList<>();
        ResultSet rs;
        long result = Constants.OPERATION_FAILURE_STATUS_CODE;
        try {
            System.out.println("query : " + query);
            PreparedStatement pstmt = conn.prepareStatement(query);
            // execute the delete statement
            rs = pstmt.executeQuery();
            String record;
            while (rs.next()) {
                record = rs.getLong("timestamp") + "," + rs.getString("key")
                        + "," + rs.getString("value");
                System.out.println(record);
                records.add(record);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return records;
    }

    /**
     *
     * @param records - The list of records to put into DB
     * @return 0 - success and -1 - failure
     */
    public int applyUpdatesFromMaster(List<String> records) {
        /* For each record */
        /* Split & Process according to what you sent in getRecordsAfterSequenceNumber */
        String query = "INSERT OR REPLACE INTO "+ Constants.TABLE_NAME + " VALUES(?,?,?)";
        int result;
        try{
            PreparedStatement pstmt = conn.prepareStatement(query);
            System.out.println("query : " + query);
            for(String record : records){
                String[] fields = record.split(",");
                System.out.println(record);
                pstmt.setLong(1, Long.valueOf(fields[0]));
                pstmt.setString(2, fields[1]);
                pstmt.setString(3, fields[2]);

                pstmt.addBatch();
            }
            pstmt.executeBatch();
            result = Constants.OPERATION_SUCCESS_STATUS_CODE;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            result = Constants.OPERATION_FAILURE_STATUS_CODE;
        }
        return result;
    }

    public void closeConnection() {
        System.out.println("Closing DB Connection");
        try {
            this.conn.close();
        } catch (SQLException e) {
            System.out.println("Exception while closing connection");
            e.printStackTrace();
        }
    }
}
