import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
public class AccountValidation {

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
     * In this class the validation of accounts.csv records is being checked!
     *
     * param all_records : This is the arrayList to store all records from accounts.csv include valid and invalid records.
     * param valid_records : It contains valid records from accounts.csv (Each record is considered as an object of class Account)
     * param invalid_records : It contains invalid records from accounts.csv which is not satisfy the condition.
     */
    public static void main(String[] args) {
        String line = "";
        String splitBy = ",";

        try {
            BufferedReader br = new BufferedReader(new FileReader("Accounts.csv"));

            ArrayList<Account> all_records = new ArrayList<>();
            ArrayList<Account> valid_records = new ArrayList<>();
            ArrayList<Account> invalid_records = new ArrayList<>();

            while ((line = br.readLine()) != null)
            {
                String[] arr = line.split(splitBy);

                all_records.add(new Account(arr[0],arr[1],arr[2],arr[3],arr[4],arr[5],arr[6]));

            }
            for (int counter = 1; counter < all_records.size(); counter++) {
                String record = all_records.get(counter).RECORD_NUMBER;
                String account_number = all_records.get(counter).ACCOUNT_NUMBER;
                String acc_type = all_records.get(counter).ACCOUNT_TYPE;
                String customer_id = all_records.get(counter).ACCOUNT_CUSTOMER_ID;
                String limit = all_records.get(counter).ACCOUNT_LIMIT;
                String open_date = all_records.get(counter).ACCOUNT_OPEN_DATE;
                String balance = all_records.get(counter).ACCOUNT_BALANCE;

                String firstThreeChars = account_number.substring(0, 3);
                String expectedFirstThreeChars = "100";


                AccountType enum_type = AccountType.valueOf(acc_type);


                if( Integer.valueOf(balance)<=Integer.valueOf(limit) && account_number.length() == 6 && firstThreeChars.equals(expectedFirstThreeChars) && (enum_type.equals(AccountType.saving) || enum_type.equals(AccountType.fixed)||enum_type.equals(AccountType.recurring))){
                    valid_records.add(new Account(record,account_number,acc_type,customer_id,limit,open_date,balance));
                    counterValidRecords = counterValidRecords + 1;

                    try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                        Statement stmt = conn.createStatement();) {
                        // Execute a query
                        System.out.println("Inserting records into the table...");
                        String sql = "INSERT INTO bank_account VALUES (record,account_number,acc_type,customer_id,limit,open_date,balance)";
                        stmt.executeUpdate(sql);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    invalid_records.add(new Account(record,account_number,acc_type,customer_id,limit,open_date,balance));
                    counterInvalidRecors = counterInvalidRecors + 1;
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