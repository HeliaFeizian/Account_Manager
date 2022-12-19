import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomerValidation {
    static int counterValidRecords;
    static int counterInvalidRecors;


    static final String DB_URL = "jdbc:mysql://localhost:3306/dbname";
    static final String USER = "root";
    static final String PASS = "********";
    /**
     * @param args
     * @author: Fatemeh Feizian
     * @date   November, 4 , 2022
     *
     * This is a class that the validation of customers.csv records is being checked!
     *
     * param all_records : This is the arrayList to store all records from customerss.csv include valid and invalid records.
     * param valid_records : It contains valid records from customers.csv (Each record is considered as an object of class Customer)
     * param invalid_records : It contains invalid records from customers.csv which is not satisfy the condition.
     */

    public static void main(String[] args) {
        String line = "";
        String splitBy = ",";


        try {
            BufferedReader br = new BufferedReader(new FileReader("Customers.csv"));

            ArrayList<Customer> all_records = new ArrayList<>();
            ArrayList<Customer> valid_records = new ArrayList<>();
            ArrayList<Customer> invalid_records = new ArrayList<>();

            while ((line = br.readLine()) != null)
            {
                String[] arr = line.split(splitBy);

                all_records.add(new Customer(arr[0],arr[1],arr[2],arr[3],arr[4],arr[5],arr[6],arr[7]));

            }
            for (int counter = 1; counter < all_records.size(); counter++) {
                String record = all_records.get(counter).RECORD_NUMBER;
                String Id = all_records.get(counter).CUSTOMER_ID;
                String name = all_records.get(counter).CUSTOMER_NAME;
                String surname = all_records.get(counter).CUSTOMER_SURNAME;
                String address = all_records.get(counter).CUSTOMER_ADDRESS;
                String zipcode = all_records.get(counter).CUSTOMER_ZIP_CODE;
                String natinal_id = all_records.get(counter).CUSTOMER_NATIONAL_ID;
                String birth_date = all_records.get(counter).CUSTOMER_BIRTH_DATE;
                /**
                 *
                 * @param string is used to get the format of the birth_date. then get the year to check condition
                 */
                String string = birth_date;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy", Locale.ENGLISH);
                LocalDate date = LocalDate.parse(string, formatter);

                if((natinal_id.length()==10) && date.getYear()<1995){
                    valid_records.add(new Customer(record,Id,name,surname,address,zipcode,natinal_id,birth_date));
                    counterValidRecords = counterValidRecords + 1;
                    try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                        Statement stmt = conn.createStatement();) {
                        // Execute a query
                        System.out.println("Inserting records into the table...");
                        String sql = "INSERT INTO bank_customer VALUES (record,Id,name,surname,address,zipcode,natinal_id,birth_date)";
                        stmt.executeUpdate(sql);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    invalid_records.add(new Customer(record,Id,name,surname,address,zipcode,natinal_id,birth_date));
                    counterInvalidRecors = counterInvalidRecors + 1;
                    /*
                     *
                     * arraylist to json
                     */
                    //String json = new Gson().toJson(invalid_records);

                }


            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        System.out.println(counterValidRecords);
        System.out.println(counterInvalidRecors);
    }
}