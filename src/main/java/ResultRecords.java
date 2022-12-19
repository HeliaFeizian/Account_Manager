public class ResultRecords {

    /**
     *
     *
     * this class is created for getting the result of select query("select customer_id , customer_name , customer_balance from tables where balance > 1000").
     * data types of all columns in DB defined as string to be easy to work with them.
     * class: QueryResult.java is working with this class.
     * @author Fatemeh Feizian
     * @since 11/5/2022
     */

    String customer_id;
    String customer_name;
    String account_balance;

    public ResultRecords(String customer_id, String customer_name, String account_balance) {
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.account_balance = account_balance;
    }
}
