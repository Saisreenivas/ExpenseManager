package data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import model.MessageEach;
import com.saisreenivas.expensemanager.expensemanager.R;

import java.util.List;

/**
 * Created by Sai sreenivas on 1/2/2017.
 */

public class CustomMessageAdapter extends BaseAdapter{

    private Context mContext;
    private List<MessageEach> mMessageList;
    private static LayoutInflater mInflator = null;

    public CustomMessageAdapter(Context mContext, List<MessageEach> mMessageList) {
        this.mContext = mContext;
        this.mMessageList = mMessageList;
        mInflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mMessageList.size();
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
        view = mInflator.inflate(R.layout.row, parent, false);
        MessageEach mess = mMessageList.get(position);
        ((TextView)view.findViewById(R.id.lblMsg)).setText(mess.getmBody());
        ((TextView)view.findViewById(R.id.lblNumber)).setText(mess.getmAddress());
        ((TextView)view.findViewById(R.id.lblDate)).setText(mess.getmDate());
        ((TextView)view.findViewById(R.id.lblAmount)).setText(mess.getmMoney());
        return view;
    }
}
