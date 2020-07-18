package data;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import model.MessageEach;
import com.saisreenivas.expensemanager.expensemanager.R;

import java.util.ArrayList;

/**
 * Created by Sai sreenivas on 1/2/2017.
 */

public class CustomMessageTabAdapter extends BaseAdapter {

    private ArrayList<MessageEach> mMessageEachList;
    private Context mContext;
    private static LayoutInflater mInflater = null;

    public CustomMessageTabAdapter(Context Context, ArrayList<MessageEach> MessageEachList) {
        this.mMessageEachList = MessageEachList;
        this.mContext = Context;
        mInflater = (LayoutInflater) Context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mMessageEachList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(convertView == null){
            view = mInflater.inflate(R.layout.row_message_tab, null);
            MessageEach messageEach = mMessageEachList.get(position);
            ((TextView)view.findViewById(R.id.row_date)).setText(messageEach.getmDate());
            ((TextView)view.findViewById(R.id.row_name)).setText(messageEach.getmName());
            TextView money = (TextView)view.findViewById(R.id.row_money);
            money.setText(messageEach.getmMoney());
            Log.v("CustomMessageTabAdapter", messageEach.getmDate() + " " + messageEach.getmName());
        }

        return view;
    }
}
