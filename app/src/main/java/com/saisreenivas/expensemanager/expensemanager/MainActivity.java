package com.saisreenivas.expensemanager.expensemanager;


import data.CustomExpensesAdapter;
import data.DBHandler;
import info.tabsswipe.adapter.TabsPagerAdapter;
import model.ExpenseDone;

import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.R.attr.fragment;
import static android.view.View.GONE;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.saisreenivas.expensemanager.expensemanager.R.id.month;
import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;

    Calendar cal;
    DBHandler dbHandler;
    private GridView listView1;
    ArrayList<ExpenseDone> totalData = new ArrayList<>();
    // Tab titles
    private String[] tabs = { "Expenses", "Messages", "SMS Data" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getSupportActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        dbHandler = new DBHandler(this);




        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
                if(position == 0){

                    cal = Calendar.getInstance();
                    SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
                    String monthPresent = month_date.format(cal.getTime()).toString();
                    monthAmount(monthPresent);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }



/*
    public void showListMessage(){
        ArrayList<MessageEach> messageEachList = new ArrayList<>();
        messageEachList = dbHandler.ViewMessagesSeparated();

        ListView list = (ListView) findViewById(R.id.list_separated);
        CustomMessageTabAdapter adapter = new CustomMessageTabAdapter(getApplicationContext(), messageEachList);
        list.setAdapter(adapter);
        Log.v("getCount", Integer.toString(dbHandler.getCount()));
    }*/
    public void monthAmount(String month1){
        TextView month = (TextView) viewPager.findViewById(R.id.month);
        TextView amount = (TextView) viewPager.findViewById(R.id.money);
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

        listView1 = (GridView) viewPager.findViewById(R.id.list_expenses);
        CustomExpensesAdapter adapter1 = new CustomExpensesAdapter(MainActivity.this,R.layout.row_message_tab , totalData);
        listView1.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();

    }
}
