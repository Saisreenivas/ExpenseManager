package info.tabsswipe.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.saisreenivas.expensemanager.expensemanager.MessageDataFragment;
import com.saisreenivas.expensemanager.expensemanager.MessagesFragment;
import com.saisreenivas.expensemanager.expensemanager.MonthlyFragment;

/**
 * Created by Sai sreenivas on 8/14/2017.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MonthlyFragment();
            case 1:
                return new MessagesFragment();
            case 2:
                return new MessageDataFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
