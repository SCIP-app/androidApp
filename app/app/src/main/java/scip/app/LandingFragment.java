package scip.app;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
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


public class LandingFragment extends Fragment {
    TextView peakFertilityText;
    TextView nextCycleText ;
    ImageButton calendarButton;
    ImageButton artButton;
    DatabaseHelper db;

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
        View view = inflater.inflate(R.layout.fragment_landing_main,
                container, false);

        peakFertilityText = (TextView) view.findViewById(R.id.Peak_Fertility);
        nextCycleText = (TextView) view.findViewById(R.id.Next_Cycle);
        calendarButton = (ImageButton) view.findViewById(R.id.calendarButton);
        artButton = (ImageButton) view.findViewById(R.id.artButton);
        db = new DatabaseHelper(getActivity().getApplicationContext());



        calendarButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent calendarIntent = new Intent(getActivity(),CalendarViewActivity.class);
                calendarIntent.putExtra("couple_id", ((DashboardActivity)getActivity()).getCouple_id());
                startActivity(calendarIntent);
            }
        });

        artButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent artIntent = new Intent(getActivity(), ViralLoadActivity.class);
                artIntent.putExtra("couple_id", ((DashboardActivity) getActivity()).getCouple_id());
                startActivity(artIntent);


            }
        });

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

        long coupleId = ((DashboardActivity)getActivity()).getCouple_id();
        new GetData().execute(coupleId);

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

    class GetData extends AsyncTask<Long, Void, List<Participant>> {



        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

        @Override
        protected List<Participant> doInBackground(Long... params) {
            List<Participant> couple = db.getCoupleFromID(params[0]);
            return couple;
        }

        @Override
        protected void onPostExecute(List<Participant> couple) {
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

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}
