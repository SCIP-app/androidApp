package scip.app;


import android.view.View;
import android.widget.CheckBox;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

public class CalendarCustomAdapterFragment extends CaldroidFragment{

    public CustomizedCalendarCellAdapter instance;

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        // TODO Auto-generated method stub

        instance = new CustomizedCalendarCellAdapter(getActivity(), month, year,getCaldroidData(), extraData, ((CalendarViewActivity)getActivity()).getCouple());
        return instance;
    }

    @Override
    public void refreshView() {
        instance.calendarCellView.invalidate();
    }
    /**
     * Created by Sindu on 8/6/15.
     */

}