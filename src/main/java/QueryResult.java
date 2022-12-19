import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileReader;
import java.security.Provider;
import java.sql.*;
import java.util.ArrayList;

/**
 * This class is used to create a JDBC connection with MySql DB.
 * This class is used to apply select query from database.
 * @author Fatemeh Feizian
 */
public class QueryResult {
    //JDBC and database properties.
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/dbname";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "********";
    public static void main(String args[]){
        Connection conn = null;
        try{
            Class.forName(DB_DRIVER);
            conn = DriverManager.
                    getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            if(conn != null){
                System.out.println("Successfully connected.");
            }else{
                System.out.println("Failed to connect.");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void viewTable(Connection con) throws SQLException {
        String query = "select bc.CUSTOMER_ID,bc.CUSTOMER_NAME, ba.ACCOUNT_BALANCE from bank_account as ba INNER JOIN bank_customer as bc ON ba.ACCOUNT_CUSTOMER_ID = bc.CUSTOMER_ID WHERE ba.ACCOUNT_BALANCE > 1000";
        try (Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            ArrayList<ResultRecords> result_records = new ArrayList<>();
            while (rs.next()) {
                /**
                 * all columns in database tables stored as "string" (nvarchar(50)) include CUSTOMER_ID and ACCOUNT_BALANCE
                 */
                String ID = rs.getString("CUSTOMER_ID");
                String NAME = rs.getString("CUSTOMER_NAME");
                String BALANCE = rs.getString("ACCOUNT_BALANCE");

                result_records.add(new ResultRecords(ID,NAME,BALANCE));


            }

        } catch (SQLException e) {
            //JDBCTutorialUtilities.printSQLException(e);
        }
    }
}