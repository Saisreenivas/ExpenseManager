package com.saisreenivas.expensemanager.expensemanager;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.CustomExpensesAdapter;
import data.CustomMessageAdapter;
import data.DBHandler;
import model.ExpenseDone;
import model.MessageEach;

import static android.app.Activity.RESULT_OK;


public class MessagesFragment extends Fragment {

    private Date lastOne, lastOne1;
    private int lastCount, lastCount1;
    private Long dateStr;
    private TextView month;
    private Calendar cal;
    private String monthToday;
    private String monthPresent;
    private TextView totalCount;
    private TextView amount;
    private GridView listView1;
    ArrayList<ExpenseDone> totalData = new ArrayList<>();

    private View rootView;
    Context context;
    DBHandler dbHandler ;

    //sms data
    private ListView listView;//sms

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;


    //Permision code that will be checked in the method onRequestPermissionsResult
    private int STORAGE_PERMISSION_CODE = 23;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_messages, container, false);
        context = getContext();

        dbHandler = new DBHandler(getActivity());
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Context context = getContext();
        if(isReadStorageAllowed()){

            //dbHandler.deleteEarlierDates(dateStr);
            //Log.v("getLastCount!=0", lastOne + " " + lastOne1 + " " + lastCount + " " + lastCount1 + " " + dbHandler.getLastCount());


            //Existing the method with return
            //sms Data
            listView = (ListView) rootView.findViewById(R.id.list_sms);
            if(dbHandler.getCount() == 0){showSMS();}
            ////////showSMS();// for scanning if nothing is present in list view
            else{showBasicMessages();}
            ////////showBasicMessages(); // for already scanned messages
            //dbHandler.copyDbToExternal(getApplicationContext());
        }
        else {
            //If the app has not the permission then asking for the permission
            requestStoragePermission();
        }
    }

    //We are calling this method to check the permission status
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_SMS);
        int permissionLocation = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (result != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }

        //If permission is not granted returning false
        return true;
    }

    //Requesting permission
    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_SMS)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
            getActivity().finish();
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_SMS},STORAGE_PERMISSION_CODE);
    }






    public void showSMS(){

        //Inbox Uri
        Uri inboxUri = Uri.parse("content://sms/inbox");

        //List the required columns
        String[] reqColumns = new String[]{"_id", "address", "body", "date"};

        //Get content resolver object which will deal with content provider
        ContentResolver cr = getActivity().getContentResolver();

        //Fetch inbox sms message from built-in content provider
        Cursor c = cr.query(inboxUri, reqColumns, null, null, null);




        c.moveToLast();
        if (c.isLast()) {
            lastOne1 = new Date(c.getLong(3));
            lastCount1 = c.getCount();
            ExpenseDone expenseDone = new ExpenseDone();
            expenseDone.set_date(Long.toString(c.getLong(3)));
            expenseDone.set_count(Integer.toString(c.getCount()));
            dbHandler.addThePresentDate(expenseDone);
            Log.v("lastOne1 and lastCount1", lastOne + " " + lastOne1 + " " + lastCount + " " + lastCount1);
        }
        c.moveToFirst();

        //lastOne, lastCount, lastOne1 & lastCount1 relations.
        if(lastOne == null && lastCount == 0) {
            Log.v("lastOne||lastCount", "null 0");
            scanData(c);
            /*
            c.moveToLast();
            if(c.isLast()){
                Log.v("lastOne and lastCount", lastOne + " " + lastOne1 + " " + lastCount + " " + lastCount1);
            }*/
        }
        else if(lastOne != null && lastOne1 == lastOne && lastCount1 == lastCount){
            //do nothing.
            Log.v("if nothing is changed", lastOne + " " + lastOne1 + " " + lastCount + " " + lastCount1);
        }
        else if(lastCount != 0 && lastCount1 != lastCount){
            dbHandler.deleteAllMessages();
            Log.v("lastCount!=0", lastCount + "" + lastCount1);
            scanData(c);

        }
        c.close();
    }

    public void showBasicMessages(){
        List<MessageEach> messageEachList = dbHandler.ViewMessagesDeposit();

        totalCount = (TextView) rootView.findViewById(R.id.totalCount);
        totalCount.setText(messageEachList.size() + " " + "Messages");

        Log.v("ShowBasic", Integer.toString(messageEachList.size()));
        listView = (ListView) rootView.findViewById(R.id.list_sms);
        CustomMessageAdapter adapter = new CustomMessageAdapter(getActivity().getApplicationContext(), messageEachList);
        listView.setAdapter(adapter);
    }



    public void scanData(Cursor c){
        Log.v("inboxSMS", c.getColumnName(1) + " " + c.getColumnName(2) + " " + c.getColumnName(3));
        List<MessageEach> totalList = new ArrayList<MessageEach>();
        if (c.moveToFirst()) {
            do {
                MessageEach row = new MessageEach();
                row.setmAddress(c.getString(1));
                row.setmBody(c.getString(2));
                //Log.v("MessageEach" ,c.getString(1) + c.getString(2));
                String me = c.getString(2);
                String[] variables = getResources().getStringArray(R.array.message_check);

                if (me.contains("'")) {
                    String[] parts = me.split("'");
                    String concat = null;
                    for (int j = 0; j < parts.length; j++) {
                        concat = concat + parts[j];
                    }
                    row.setmBody(concat);
                } else {
                    row.setmBody(me);
                }

                for(int i=0;i<variables.length;i++) {
                    if (row.getmBody().contains(variables[i])) {
                        row.setmName(variables[i]);
                        Date date = new Date(c.getLong(3));
                        String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
                        row.setmDate(formattedDate);


                        moneyCheck(row);

                        //Log.v("Deposit", "contains " + me);

                            /*if(me.contains("Offers")){
                                dbHandler.DeleteMessage(row);
                            }*/
                    }
                }
                if(row.getmBody().contains("OTP"/*&RS*/) && row.getmBody().contains("Rs.")){
                    //row.setmBody(me);
                    row.setmName("OTP Amount");

                    //row.setmDate(c.getString(3));
                    Date date = new Date(c.getLong(3));
                    String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
                    row.setmDate(formattedDate);

                    Log.v("AddOTP", row.getmAddress());
                    moneyCheck(row);
                }
                if(row.getmBody().contains("Credit"/*&RS*/)){
                    if(row.getmBody().contains("expiry") || row.getmBody().contains("DATA")) {
                        //row.setmBody(me);
                        Log.v("DeleteExpiryData", row.getmAddress());
                        moneyCheck(row);
                    }
                }
                if(row.getmBody().contains("purchase"/*&RS*/) && row.getmBody().contains("@")){
                    Log.v("DeletePurchase", row.getmAddress());
                    dbHandler.DeleteMessage(row);
                }
                // Adding contact to list
                totalList.add(row);

            } while (c.moveToNext());
        }
        dbHandler.DeleteMessages();


        dbHandler.close();
    }


    public void moneyCheck(MessageEach row){
        int p;
        String[] moneycheck = getResources().getStringArray(R.array.money_check);
        for(int j=0;j<moneycheck.length;j++) {
            if (row.getmBody().contains(moneycheck[j])) {
                String[] num = row.getmBody().split(moneycheck[j]);
                try {
                    String[] nu = num[1].split(" ");
                    Log.v("Money", nu[0]);
                    row.setmMoney(nu[0]);
                    dbAdd(row);
                    p = 0;
                } catch (Exception e) {
                    p = 1;
                }
                if (p == 1) {
                    Log.v("Money", num[0]);
                    row.setmMoney(num[0]);
                    dbAdd(row);
                }
                //row.setmMoney("320");
                Log.v("MoneyCheck", row.getmAddress() + " " + moneycheck[j] + " " +row.getmMoney());
                //dbHandler.AddMessage(row);
            }
        }
    }


    public void dbAdd(MessageEach row) {
        if (dbHandler.PreviousMessage().getmDate().equals(row.getmDate())) {
            Log.v("scanData", dbHandler.PreviousMessage().getmBody() + " " + row.getmBody());
        } else {
            dbHandler.AddMessage(row);
        }
        dbHandler.AddMessage(row);
/*        if (dbHandler.PreviousMessage().getmBody().equals(row.getmBody())) {
            Log.v("scanData", dbHandler.PreviousMessage().getmBody() + " " + row.getmBody());
        } else {
            dbHandler.AddMessage(row);
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        /*
        if(requestCode == STORAGE_PERMISSION_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
                Toast.makeText(this,"Permission granted now you can read the SMS Tab",Toast.LENGTH_LONG).show();
                finish();
                /*
                //Setting Date
                cal = Calendar.getInstance();
                SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
                monthPresent = month_date.format(cal.getTime());
                monthToday = monthPresent;
                month.setText(monthPresent);
                monthAmount(monthPresent);


                //sms Data
                listView = (ListView) findViewById(R.id.list_sms);
                showSMS();// for scanning if nothing is present in list view
                showBasicMessages(); // for already scanned messages
                dbHandler.copyDbToExternal(getApplicationContext());/
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
    */
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_SMS, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                        //Log.d(TAG, "sms & location services permission granted");
                        // process the normal flow
                        getActivity().finish();
                        //else any one or both the permissions are not granted
                    } else {
                        //Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.RECORD_AUDIO)) {
                            showDialogOK("Service Permissions are required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    isReadStorageAllowed();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    getActivity().finish();
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            explain("You need to give some mandatory permissions to continue. Do you want to go to app settings?");
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }
    }


    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity().getApplicationContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private void explain(String msg){
        final android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(getActivity().getApplicationContext());
        dialog.setMessage(msg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        //  permissionsclass.requestPermission(type,code);
                        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.exampledemo.parsaniahardik.marshmallowpermission")));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        getActivity().finish();
                    }
                });
        dialog.show();
    }

}

