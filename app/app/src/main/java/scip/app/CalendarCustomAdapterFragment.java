package scip.app;


import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.List;

import scip.app.models.Participant;

public class CalendarCustomAdapterFragment extends CaldroidFragment {


    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        // TODO Auto-generated method stub

        return new CustomizedCalendarCellAdapter(getActivity(), getActivity().getIntent(), month, year,
                getCaldroidData(), extraData, ((CalendarViewActivity)getActivity()).getCouple());

    }

}