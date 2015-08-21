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
        if(((CalendarViewActivity)getActivity()).getCouple()!=null) {
            return new CustomizedCalendarCellAdapter(getActivity(), month, year,getCaldroidData(), extraData, ((CalendarViewActivity)getActivity()).getCouple(),null);
        }

        if(((CalendarViewActivity)getActivity()).getParticipant()!=null) {
            return new CustomizedCalendarCellAdapter(getActivity(), month, year,getCaldroidData(), extraData,null, ((CalendarViewActivity)getActivity()).getParticipant());
        }

        return null;
    }
}