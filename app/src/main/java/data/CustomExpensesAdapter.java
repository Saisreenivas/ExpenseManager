package data;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import model.ExpenseDone;

import com.saisreenivas.expensemanager.expensemanager.Main2Activity;
import com.saisreenivas.expensemanager.expensemanager.MainActivity;
import com.saisreenivas.expensemanager.expensemanager.MonthlyFragment;
import com.saisreenivas.expensemanager.expensemanager.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by Sai sreenivas on 1/5/2017.
 */

public class CustomExpensesAdapter extends ArrayAdapter<ExpenseDone> {

    private Activity activity;
    private Context mContext;
    private int layoutresource;
    private List<ExpenseDone> expenseDoneList;
    public int selectedRow=-1;

    public CustomExpensesAdapter(Activity act, int resource, ArrayList<ExpenseDone> expenseDones) {
        super(act, resource, expenseDones);
        mContext = act.getApplicationContext();
        activity = act;
        layoutresource = resource;
        expenseDoneList = expenseDones;
        notifyDataSetChanged();
    }



    @Override
    public int getCount() {
        return expenseDoneList.size();
    }

    @Override
    public ExpenseDone getItem(int position) {
        return expenseDoneList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

//        View view = convertView;
//
//        view = mInflator.inflate(R.layout.expenses_row, parent, false);
//        ExpenseDone expense = expenseDoneList.get(position);
//        //Log.v("CustomMessageAdapter", expense.getmAddress() + mMessageList.get(position));
//        ((TextView)view.findViewById(R.id.numB)).setText(expense.get_id() + ".");
//        ((TextView)view.findViewById(R.id.name)).setText(expense.get_name());
//        ((TextView)view.findViewById(R.id.cost)).setText("Rs." + " " + expense.get_amount());
//
//
//        return view;

        View customRow = convertView;
        ViewHolder holder = null;
        if (customRow == null) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            customRow = inflater.inflate(layoutresource, null);
        }else {
            //holder = (ViewHolder) customRow.getTag();
        }
            holder = new ViewHolder();

            //holder.alphabet = (TextView) customRow.findViewById(R.id.alphabet_text);
            holder.recordDate = (TextView) customRow.findViewById(R.id.row_date);
            holder.name = (TextView) customRow.findViewById(R.id.row_name);
            holder.amount = (TextView) customRow.findViewById(R.id.row_money);
            holder.rowEach = (RelativeLayout) customRow.findViewById(R.id.row_each);
            holder.delButton = (Button) customRow.findViewById(R.id.delButton);
            holder.rowDetails = (RelativeLayout) customRow.findViewById(R.id.row_details);
            holder.rowEdit = (RelativeLayout) customRow.findViewById(R.id.row_edit);

            customRow.setTag(holder);

        holder.expenseDone = getItem(position);
        //if(!holder.expenseDone.get_name().isEmpty()) {
            String nameMe = holder.expenseDone.get_name();
            if(nameMe.length() >= 19){
                String first = nameMe.substring(0, 17);
                nameMe = first + "...";
            }
            holder.name.setText(nameMe);
            holder.amount.setText("Rs." + " " +  Integer.toString(holder.expenseDone.get_amount()));
            holder.recordDate.setText(holder.expenseDone.get_date());

        final ViewHolder finalHolder = holder;
        final View finalCustomRow = customRow;
//        holder.delButton.setTag(position);
/*        holder.delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseDoneList.remove(position);
                Toast.makeText(getContext(), "expense deleted", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });*/
        final ViewHolder finalHolder1 = holder;
        holder.rowEach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalHolder.rowEdit.getVisibility() == VISIBLE) {
                    finalHolder.rowEdit.setVisibility(GONE);
                }
                else if(finalHolder.rowEdit.getVisibility() == GONE){
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                    layoutParams.addRule(RelativeLayout.BELOW, finalHolder.rowDetails.getId());
                    finalHolder.rowEdit.setLayoutParams(layoutParams);
                    finalHolder.rowEdit.setVisibility(View.VISIBLE);
                }
                finalHolder.delButton.setTag(position);
                finalHolder.delButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        expenseDoneList.remove(position);
                        Toast.makeText(getContext(), "expense deleted", Toast.LENGTH_SHORT).show();
                        finalHolder.rowEdit.setVisibility(GONE);
                        notifyDataSetChanged();
                    }
                });
            }
        });

        return customRow;
    }

    public class ViewHolder{

        ExpenseDone expenseDone;
        TextView alphabet, recordDate, name, amount;
        Button delButton;
        RelativeLayout rowEach;
        RelativeLayout rowEdit, rowDetails;
    }



}

