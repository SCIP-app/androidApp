package scip.app;


import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.List;

import scip.app.models.Participant;

public class CalendarCustomAdapterFragment extends CaldroidFragment{

    public CustomizedCalendarCellAdapter instance;

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        // TODO Auto-generated method stub


        instance = new CustomizedCalendarCellAdapter(getActivity(), month, year,getCaldroidData(), extraData, ((CalendarViewActivity)getActivity()).getCouple());
        return instance;
    }

    public CustomizedCalendarCellAdapter getInstance() {
        return instance;
    }


}