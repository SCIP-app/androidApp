package scip.app;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import scip.app.databasehelper.DatabaseHelper;
import scip.app.models.Participant;
import scip.app.models.ViralLoad;

/* LandingFragment: This class sets up the dashboard view for the calendar and viral load activities
 *
 *  Functions:
 *
 *   onCreateView (LayoutInflater, ViewGroup, Bundle): Creates and sets a View type layout for this
 *      fragment using a series of setter methods (is this how you'd say it?)
 *          input parameters: Standard LayoutInflator, ViewGroup, Bundle
 *          output parameters:
 *              view: A selectable list view of the couple ID
 *                  Type: View
 *
 *   onClick (View): Sets up the calendar and ART intents
 *      Input parameters: v
 *          Type: View
 *      Output parameters: view
 *
 *   Getters & setters
*/
public class LandingFragment extends Fragment {

    public LandingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_landing_main, container, false);
        ImageButton calendarButton = (ImageButton) view.findViewById(R.id.calendarButton);
        calendarButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calendarIntent = new Intent(getActivity(),CalendarViewActivity.class);
                calendarIntent.putExtra("couple_id", ((DashboardActivity)getActivity()).getCouple_id());
                startActivity(calendarIntent);
            }
        });

        ImageButton artButton = (ImageButton) view.findViewById(R.id.artButton);
        artButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent artIntent = new Intent(getActivity(), ViralLoadActivity.class);
                artIntent.putExtra("couple_id", ((DashboardActivity) getActivity()).getCouple_id());
                startActivity(artIntent);
            }
        });

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

        TextView peakFertilityText = (TextView) view.findViewById(R.id.Peak_Fertility);
        TextView nextCycleText = (TextView) view.findViewById(R.id.Next_Cycle);
        long coupleId = ((DashboardActivity)getActivity()).getCouple_id();
        DatabaseHelper db = new DatabaseHelper(getActivity().getApplicationContext());
        List<Participant> couple = db.getCoupleFromID(coupleId);
        db.closeDB();

        for(Participant participant: couple) {
            if(participant.isFemale()) {
                if(participant.getPeakFertility()!=null) {
                    List<Date> nextPeakFertilityValues = participant.getPeakFertility().getPeakFertilityWindow();
                    List<Date> nextCycleDates = participant.getPeakFertility().getNextCycleDates();

                    if(nextPeakFertilityValues!=null && nextPeakFertilityValues.size() == 4) {
                        String fertilityRange = formatter.format(nextPeakFertilityValues.get(0)) + " - " + formatter.format(nextPeakFertilityValues.get(nextPeakFertilityValues.size() - 1));
                        peakFertilityText.setText(fertilityRange);

                    }

                    if(nextCycleDates!=null && nextCycleDates.size() == 2) {
                        String nextFertilityRange = formatter.format(nextCycleDates.get(0)) + " - " +formatter.format(nextCycleDates.get(nextCycleDates.size()-1));
                        nextCycleText.setText(nextFertilityRange);
                    }

                }
            }
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
