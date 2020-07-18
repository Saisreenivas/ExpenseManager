package com.saisreenivas.expensemanager.expensemanager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import data.CustomExpensesAdapter;
import data.DBHandler;
import model.ExpenseDone;

import static android.app.Activity.RESULT_OK;
import static com.saisreenivas.expensemanager.expensemanager.MessagesFragment.REQUEST_ID_MULTIPLE_PERMISSIONS;


public class MonthlyFragment extends Fragment implements View.OnClickListener{


    private Button addExpense1;
    private ImageView previousMonth;
    private ImageView nextMonth;
    private TextView month;
    private Calendar cal;
    private String monthToday;
    private TextView amount;
    private int lastCount;
    private Date lastOne;
    private Long dateStr;
    DBHandler dbHandler;

    private GridView listView1;
    ArrayList<ExpenseDone> totalData = new ArrayList<>();

    private View rootView;
    private static final int REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_monthly, container, false);

        addExpense1 = (Button) rootView.findViewById(R.id.add_expense_button);
        amount = (TextView) rootView.findViewById(R.id.money);
        month = (TextView) rootView.findViewById(R.id.month);
        previousMonth = (ImageView) rootView.findViewById(R.id.arrow_left);
        nextMonth = (ImageView) rootView.findViewById(R.id.arrow_right);

        //Setting onClick Listener's
        addExpense1.setOnClickListener(this);
        previousMonth.setOnClickListener(this);
        nextMonth.setOnClickListener(this);

        dbHandler = new DBHandler(getActivity());
        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (dbHandler.getLastCount() != 0) {
            ExpenseDone expenseDone = new ExpenseDone();
            expenseDone = showLastDate();

            Long me = Long.parseLong(expenseDone.get_date());
            lastOne = new Date(me);
            lastCount = Integer.parseInt(expenseDone.get_count());

            cal = Calendar.getInstance();
            SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
            String monthPresent = month_date.format(cal.getTime()).toString();
            monthAmount(monthPresent);
        }
    }

    @Override
    public void onClick(View v) {
        Log.v("onClickId", v.getId() + " " + R.id.add_expense_button + " " + R.id.arrow_left  + " " + R.id.arrow_right);
        switch (v.getId()) {
            case R.id.add_expense_button:
                Intent intent = new Intent(getContext(), Main2Activity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.arrow_left:
                cal.add(Calendar.MONTH, -1);
                Log.v("arrowLeft", String.valueOf(cal));
                month.setText(new SimpleDateFormat("MMMM").format(cal.getTime()));
                monthAmount(month.getText().toString());
                break;
            case R.id.arrow_right:
                if (month.getText() != monthToday){
                    cal.add(Calendar.MONTH, +1);
                    month.setText(new SimpleDateFormat("MMMM").format(cal.getTime()));
                    monthAmount(month.getText().toString());
                    Log.v("MainActivity", monthToday + " " + month.getText());
                }else if(month.getText() == monthToday){//not working
                    Toast.makeText(getActivity().getApplicationContext(), "Sorry, you cannot spend on your future", Toast.LENGTH_SHORT).show();
                    Log.v("MainActivity", monthToday + " " + month.getText());
                }
        }
    }

    public void monthAmount(String month1){
        month = (TextView) getActivity().findViewById(R.id.month);
        amount = (TextView) getActivity().findViewById(R.id.money);
        month.setText(month1);
        ExpenseDone expenseDone = new ExpenseDone(month1);
        int expenses = dbHandler.ShowTotalExpense(expenseDone);
        amount.setText( "\n" + expenses);
        showData(month1);
    }


    public void showData(String month){
        if(month == "me"){
            month = new SimpleDateFormat("MMMM").format(cal.getTime());
        }
        ExpenseDone expense = new ExpenseDone(month);
        totalData.clear();
        totalData = dbHandler.databaseToArry(expense);
/*
        nameD = (TextView) findViewById(R.id.nameDisplay);
        amountD = (TextView) findViewById(R.id.amountDisplay);
        serial = (TextView) findViewById(R.id.serial);
        String name1 = "Name";
        String amount1 = "Amount";
        String sr1 = "Serial";
        int i=1;

        for(ExpenseDone ed: totalData){
            sr1 = sr1 + "\n" + (i);
            name1 = name1 + "\n" +ed.get_name();
            amount1 = amount1 + "\n" + ed.get_amount();
            i++;
        }

        nameD.setText(name1);
        amountD.setText(amount1);
        serial.setText(sr1);*/


        listView1 = (GridView) rootView.findViewById(R.id.list_expenses);
        final CustomExpensesAdapter adapter1 = new CustomExpensesAdapter(getActivity(),R.layout.row_message_tab , totalData);
        listView1.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();


    }

    public ExpenseDone showLastDate(){
        ExpenseDone expense = new ExpenseDone();
        List<ExpenseDone> totalData = dbHandler.viewLastDate();
//        Log.v("showLastDate", Integer.toString(totalData.size()));
        for(ExpenseDone ed: totalData) {
            dateStr = Long.parseLong(ed.get_date());
            lastOne = new Date(dateStr);
            lastCount = Integer.parseInt(ed.get_count());
            expense.set_count(ed.get_count());
            expense.set_date(ed.get_date());
            //Log.v("showLastDate", lastOne + " " + lastOne1 + " " + lastCount + " " + lastCount1);
        }
        return expense;

/*
        nameD = (TextView) findViewById(R.id.nameDisplay);
        amountD = (TextView) findViewById(R.id.amountDisplay);
        serial = (TextView) findViewById(R.id.serial);
        String name1 = "Name";
        String amount1 = "Amount";
        String sr1 = "Serial";
        int i=1;

        for(ExpenseDone ed: totalData){
            sr1 = sr1 + "\n" + (i);
            name1 = name1 + "\n" +ed.get_name();
            amount1 = amount1 + "\n" + ed.get_amount();
            i++;
        }

        nameD.setText(name1);
        amountD.setText(amount1);
        serial.setText(sr1);*/

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){
                String result = data.getStringExtra("returnData");
                cal = Calendar.getInstance();
                monthAmount(result);

                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        }
    }
}
