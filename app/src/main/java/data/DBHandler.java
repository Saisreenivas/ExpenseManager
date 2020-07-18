package data;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.saisreenivas.expensemanager.expensemanager.MainActivity;
import com.saisreenivas.expensemanager.expensemanager.MonthlyFragment;

import model.ExpenseDone;
import model.MessageEach;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sai sreenivas on 12/30/2016.
 */

public class DBHandler extends SQLiteOpenHelper {

    public DBHandler(Context context) {
        super(context,Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.CREATE_EXPENSE_TABLE);
        db.execSQL(Constants.CREATE_MESSAGE_TABLE);
        db.execSQL(Constants.CREATE_LASTDATECOUNT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_MONTHLY_EXPENSE);
// Creating tables again
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_MESSAGE);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_LASTDATECOUNT);
        onCreate(db);

    }

    public void AddExpense(ExpenseDone expense){
        /*ContentValues value = new ContentValues();
        value.put(COLUMN_MONTH_NAME, expense.get_month());
        value.put(COLUMN_NAME, expense.get_name());
        value.put(COLUMN_AMOUNT, expense.get_amount());*/
        if(expense.get_month() == "" || expense.get_name() == "" || expense.get_amount() == 0) {

        }
        else {
            String addQuery = "INSERT INTO " + Constants.TABLE_MONTHLY_EXPENSE + "(" + Constants.COLUMN_MONTH_NAME + "," +
                    Constants.COLUMN_NAME + "," + Constants.COLUMN_AMOUNT + "," + Constants.COLUMN_EXPENSE_DATE + ")" +
                    " VALUES ('" + expense.get_month() + "','" + expense.get_name() + "','" + expense.get_amount() + "','"+
                    expense.get_date() +"')";
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(addQuery);
            db.close();
        }
    }

    public int ShowMonthlyExpense(ExpenseDone expense){
        String monthlyExpenseQuery = "SELECT * FROM " + Constants.TABLE_MONTHLY_EXPENSE + " WHERE" + (Constants.COLUMN_MONTH_NAME == expense.get_month());
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(monthlyExpenseQuery, null);

        int monthlyExpense = 0;
        if(cursor.getCount() >=0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                monthlyExpense = monthlyExpense + Integer.parseInt(cursor.getString(3));
            }
        }
        cursor.close();

        return monthlyExpense;
    }

    public int ShowTotalExpense(ExpenseDone expense){
        //String fullExpenseQuery = "SELECT * FROM " + TABLE_MONTHLY_EXPENSE + " WHERE " + COLUMN_MONTH_NAME + " = '" +
        //        expense.get_month() + "';" ;
        String fullExpenseQuery = "SELECT "+ Constants.COLUMN_AMOUNT+" FROM " + Constants.TABLE_MONTHLY_EXPENSE + " WHERE " + Constants.COLUMN_MONTH_NAME +
                " LIKE '" + expense.get_month() +"'";

        int amount = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(fullExpenseQuery, null);
        if(cursor.moveToFirst()){
            do {
                ExpenseDone row = new ExpenseDone();
                row.set_amount(Integer.parseInt(cursor.getString(0)));
                // Adding contact to list
                amount = amount + row.get_amount();
            }while (cursor.moveToNext());
        }
        db.close();
        cursor.close();

        return amount;
    }

    public ArrayList<ExpenseDone> databaseToArry(ExpenseDone expense){
        String fullQuery = "SELECT * FROM " + Constants.TABLE_MONTHLY_EXPENSE + " WHERE " + Constants.COLUMN_MONTH_NAME + " LIKE '" +
                expense.get_month() + "';";
        ArrayList<ExpenseDone> totalList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(fullQuery, null);
        if(cursor.moveToFirst()){
            do {
                ExpenseDone row = new ExpenseDone();
                row.set_id(Integer.parseInt(cursor.getString(0)));
                Log.v("databaseToArray", cursor.getString(0));
                row.set_month(cursor.getString(1));
                row.set_name(cursor.getString(2));
                row.set_amount(Integer.parseInt(cursor.getString(3)));
                row.set_date(cursor.getString(4));
                // Adding contact to list
                totalList.add(row);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return  totalList;
    }

    public void deleteExpense(ExpenseDone expenseDone){
        String deleteQuery = "DELETE FROM " + Constants.TABLE_MONTHLY_EXPENSE + " WHERE " + Constants.COLUMN_ID  + " LIKE " +
                expenseDone.get_id() ;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(deleteQuery);
        db.close();
    }

    public int getCount(){
        String countQuery = "SELECT * FROM " + Constants.TABLE_MESSAGE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
/*
    public Cursor cursorEdit(Cursor cursor){
        String editQuery = "SELECT MESSAGE_ID,MESSAGE_NUMBER,MESSAGE_FULL FROM TABLE_MESSAGES WHERE MESSAGE_DATA IN(DEBITTED, " +
                "CREDITTED)";
        Cursor c1 = cursor.    }
*/
    public void AddMessage(MessageEach message){
        String addMessageQuery = "INSERT INTO " + Constants.TABLE_MESSAGE + " (" + Constants.COLUMN_NUMBER + ", " + Constants.COLUMN_MESSAGE + ", " + Constants.COLUMN_DATE +
                ", " + Constants.COLUMN_MY_NAME + ", " + Constants.COLUMN_MONEY + ") " + " VALUES('"+message.getmAddress() + "','"+ message.getmBody()+ "','"+ message.getmDate()+
                "','" + message.getmName() + "','" + message.getmMoney() +"')";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(addMessageQuery);
        db.close();
    }

    public MessageEach PreviousMessage(){
        String previousQuery = "SELECT * FROM " + Constants.TABLE_MESSAGE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(previousQuery, null);

        cursor.moveToLast();
        MessageEach messageEach = new MessageEach();
        if(cursor.getCount() != 0) {
            messageEach.setmBody(cursor.getString(2));
            messageEach.setmDate(cursor.getString(3));
            Log.v("PreviousMessage", cursor.getString(2));
        }
        else {
            messageEach.setmBody("01");
            messageEach.setmDate("02");
        }
        return messageEach;
    }

    public void DeleteMessages(){
        String deleteMesssageQuery = "DELETE FROM " + Constants.TABLE_MESSAGE + " WHERE " + Constants.COLUMN_MESSAGE + " LIKE ('%Offers%')";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(deleteMesssageQuery);
        db.close();
    }


    public void DeleteMessage(MessageEach message){
        String deleteMesssageQuery = "DELETE FROM " + Constants.TABLE_MESSAGE + " WHERE " + Constants.COLUMN_MESSAGE + " = '"+ message.getmBody() + "';";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(deleteMesssageQuery);
        db.close();
    }

    public ArrayList<MessageEach> ViewMessagesDeposit(){
        String viewMessageQuery = "SELECT * FROM " + Constants.TABLE_MESSAGE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(viewMessageQuery,null);
        ArrayList<MessageEach> messageEachList = new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
                    MessageEach messageEach = new MessageEach();
                messageEach.setmAddress(cursor.getString(1));
                messageEach.setmBody(cursor.getString(2));
                messageEach.setmDate(cursor.getString(3));
                //Log.v("DBHandler", cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2));
                messageEachList.add(messageEach);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return messageEachList;
    }

    public ArrayList<MessageEach> ViewMessagesSeparated(){
        String viewMessageQuery = "SELECT * FROM " + Constants.TABLE_MESSAGE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(viewMessageQuery,null);
        ArrayList<MessageEach> messageEachList = new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
                MessageEach messageEach = new MessageEach();
                messageEach.setmAddress(cursor.getString(1));
                messageEach.setmBody(cursor.getString(2));
                messageEach.setmDate(cursor.getString(3));
                messageEach.setmName(cursor.getString(4));
                messageEachList.add(messageEach);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return messageEachList;
    }

    public void deleteAllMessages(){
        String deleteQuery = "DELETE FROM " + Constants.TABLE_MESSAGE;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(deleteQuery);
        db.close();
    }

   /* public void copyDbToExternal(Context context) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//data//" + context.getApplicationContext().getPackageName() + "//databases//"
                        + DATABASE_NAME;
                String backupDBPath = DATABASE_NAME;
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public int getLastCount() {
        String countQuery = "SELECT * FROM " + Constants.TABLE_LASTDATECOUNT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public List<ExpenseDone> viewLastDate(){
        String viewAllQuery= "SELECT * FROM " + Constants.TABLE_LASTDATECOUNT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(viewAllQuery, null);
        List<ExpenseDone> expenseDones = new ArrayList<>();
        if(cursor.moveToLast()){
            ExpenseDone expenseDone1 = new ExpenseDone();
            expenseDone1.set_count(cursor.getString(2));
            expenseDone1.set_date(cursor.getString(1));
            expenseDones.add(expenseDone1);
        }
        return expenseDones;
    }

    public void deleteEarlierDates(Long DateStr){
        String deleteEarlierQuery = "SELECT * FROM " + Constants.TABLE_LASTDATECOUNT + " WHERE " + Constants.COLUMN_DATE + " = " + DateStr;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c1= db.rawQuery(deleteEarlierQuery, null);
        String deleteAllQuery = "DELETE FROM " + Constants.TABLE_LASTDATECOUNT;
        db.execSQL(deleteAllQuery);
        if(c1.moveToFirst()){
            do {
                String addData = "INSERT INTO " + Constants.TABLE_LASTDATECOUNT + "('" + Constants.COLUMN_DATE + "','" + Constants.COLUMN_COUNT +"') VALUES('"
                        + c1.getString(1) + "','" + c1.getString(2) + "')";
                db.execSQL(addData);
                db.close();
                Log.v("deleteEarlierDates", c1.getString(1) + " " + c1.getString(2) );
            }
            while(c1.moveToNext());
        }
    }

    public void addThePresentDate(ExpenseDone expenseDone){
        String addData = "INSERT INTO " + Constants.TABLE_LASTDATECOUNT + "('" + Constants.COLUMN_DATE + "','" + Constants.COLUMN_COUNT +"') VALUES('"
                + expenseDone.get_date() + "','" + expenseDone.get_count() + "')";
        Log.v("addThePresentDate", expenseDone.get_date() + "','" + expenseDone.get_count());
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(addData);
        db.close();
    }

    public ExpenseDone showLast(){
        ExpenseDone expense = new ExpenseDone();
        String selectLastQuery = "SELECT * FROM " + Constants.TABLE_LASTDATECOUNT;
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor c1 = db.rawQuery(selectLastQuery, null);
        c1.moveToLast();
        if(c1.isLast()){
            expense.set_date(c1.getString(1));
            expense.set_count(c1.getString(2));
        }
        c1.close();
        return expense;
    }

}

