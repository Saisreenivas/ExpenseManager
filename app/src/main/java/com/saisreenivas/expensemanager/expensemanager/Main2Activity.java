package com.saisreenivas.expensemanager.expensemanager;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import data.DBHandler;
import model.ExpenseDone;

public class Main2Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private EditText addName;
    private EditText addAmount;
    private Button addExpense, goBack;
    private String month, date;
    private Spinner spinner;

    DBHandler dbHandler= new DBHandler(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //getActionBar().setDisplayHomeAsUpEnabled(true);
        addName = (EditText)findViewById(R.id.add_expense_name);
        addAmount = (EditText)findViewById(R.id.add_expense_amount);
        addExpense = (Button)findViewById(R.id.add_expense);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        month = month_date.format(cal.getTime());
        date = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
        goBack = (Button) findViewById(R.id.goBack);
        setSpinner(month);

        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(addName.getText().toString().equals("") || addAmount.getText().toString().equals("")) {
                    Toast.makeText(Main2Activity.this, "Fill the fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    ExpenseDone expense = new ExpenseDone(month,addName.getText().toString(),
                            Integer.parseInt(addAmount.getText().toString()), date);
                    dbHandler.AddExpense(expense);
                    Toast.makeText(getApplicationContext(), "Expense Added", Toast.LENGTH_SHORT).show();
                    addName.setText(null);
                    addAmount.setText(null);
                }

            }
        });


        goBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
                String monthPresent = month_date.format(cal.getTime()).toString();

                Intent returnIntent = getIntent();
                returnIntent.putExtra("returnData", monthPresent);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void setSpinner(String month1){
        //import Spinner.
        spinner = (Spinner) findViewById(R.id.spinner);

        //import Adapter.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.month_name,
                R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        //set a listener on Spinner.
        spinner.setOnItemSelectedListener(this);
        //set the present month to the spinner view;

        ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter

        int spinnerPosition = myAdap.getPosition(month1);

//set the default according to value
        spinner.setSelection(spinnerPosition);
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        month = selectedItem;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
