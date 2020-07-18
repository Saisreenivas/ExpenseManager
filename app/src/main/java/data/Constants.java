package data;

/**
 * Created by Sai sreenivas on 1/27/2017.
 */

public class Constants {
    // Database Version
    static final int DATABASE_VERSION = 11;
    // Database Name
    static final String DATABASE_NAME = "expensemanagerdb";
    // Contacts table name
    static final String TABLE_MONTHLY_EXPENSE = "monthly_expense";
    // Shops Table Columns names
    static final String COLUMN_ID = "id";
    static final String COLUMN_MONTH_NAME = "month_name";
    static final String COLUMN_NAME = "expense_name";
    static final String COLUMN_AMOUNT = "expense_amount";
    static final String COLUMN_EXPENSE_DATE = "expense_date";
    static final String CREATE_EXPENSE_TABLE = "CREATE TABLE " + TABLE_MONTHLY_EXPENSE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_MONTH_NAME + " TEXT,"
            + COLUMN_NAME + " TEXT," + COLUMN_AMOUNT + " INTEGER, " + COLUMN_EXPENSE_DATE + " TEXT " + ")";
    static final String COLUMN_NUMBER = "message_number";
    static final String COLUMN_MESSAGE = "message_data";
    static final String COLUMN_DATE= "message_date";
    static final String COLUMN_MY_NAME = "message_my_name";
    static final String COLUMN_MONEY = "message_my_money";
    static final String TABLE_MESSAGE = "messages_required";
    static final String CREATE_MESSAGE_TABLE = "CREATE TABLE "+ TABLE_MESSAGE + "(" + COLUMN_ID + "INTEGER PRIMARY KEY,"
                    + COLUMN_NUMBER + " TEXT," + COLUMN_MESSAGE + " TEXT," + COLUMN_DATE + " DATE," + COLUMN_MY_NAME + " TEXT, " +
                    COLUMN_MONEY + " TEXT)";
    static final String COLUMN_COUNT = "message_total_count";
    static final String TABLE_LASTDATECOUNT = "message_last_date_count";
    static final String CREATE_LASTDATECOUNT_TABLE = "CREATE TABLE " + TABLE_LASTDATECOUNT + " (" + COLUMN_ID +
                            " INTEGER PRIMARY KEY, " + COLUMN_DATE + " TEXT, " + COLUMN_COUNT + " TEXT" + ")";
}
