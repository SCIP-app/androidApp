package scip.app;

import android.app.ActionBar;
import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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


public class ParticipantFragment extends Fragment {
    TextView peakFertilityText;
    TextView nextCycleText ;
    TextView peakFertilityLabel;
    TextView nextCycleLabel;
    ImageButton calendarButton;
    ImageButton artButton;
    TextView calendarText;
    TextView artText;
    DatabaseHelper db;

    public ParticipantFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_participant,
                container, false);

        peakFertilityText = (TextView) view.findViewById(R.id.Participant_peak_fertility);
        nextCycleText = (TextView) view.findViewById(R.id.Participant_Next_Cycle);
        peakFertilityLabel = (TextView) view.findViewById(R.id.ParticipantNextCycleLabel);
        nextCycleLabel = (TextView) view.findViewById(R.id.ParticipantPeriodCycleLabel);
        calendarButton = (ImageButton) view.findViewById(R.id.Participant_Calendar_Button);
        calendarText = (TextView) view.findViewById(R.id.caltext);
        artText = (TextView) view.findViewById(R.id.artLabel);
        artButton = (ImageButton) view.findViewById(R.id.Participant_Art_Button);

        db = new DatabaseHelper(getActivity().getApplicationContext());
        long participant_id = ((ParticipantActivity)getActivity()).getParticipant_id();
        new GetData().execute(participant_id);

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

    class GetData extends AsyncTask<Long, Void, Participant> {



        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

        @Override
        protected Participant doInBackground(Long... params) {
            Participant participant = db.getParticipant(params[0]);
            return participant;
        }

        @Override
        protected void onPostExecute(Participant participant) {
            db.closeDB();

            if(participant!=null) {
                if(participant.isFemale()) {
                    if(participant.getPeakFertility()!=null) {
                        List<Date> nextPeakFertilityValues = participant.getPeakFertility().getPeakFertilityWindow();
                        List<Date> nextCycleDates = participant.getPeakFertility().getNextCycleDates();
                        peakFertilityLabel.setVisibility(View.VISIBLE);
                        nextCycleLabel.setVisibility(View.VISIBLE);
                        peakFertilityText.setVisibility(View.VISIBLE);
                        nextCycleText.setVisibility(View.VISIBLE);

                        if(nextPeakFertilityValues!=null && nextPeakFertilityValues.size() == 4) {
                            String fertilityRange = formatter.format(nextPeakFertilityValues.get(0)) + " - " + formatter.format(nextPeakFertilityValues.get(nextPeakFertilityValues.size() - 1));
                            peakFertilityText.setText(fertilityRange);

                        }

                        if(nextCycleDates!=null && nextCycleDates.size() == 2) {
                            String nextFertilityRange = formatter.format(nextCycleDates.get(0)) + " - " +formatter.format(nextCycleDates.get(nextCycleDates.size()-1));
                            nextCycleText.setText(nextFertilityRange);
                        }

                        calendarButton.setVisibility(View.VISIBLE);
                        calendarText.setVisibility(View.VISIBLE);
                        calendarButton.setOnClickListener(new OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                Intent calendarIntent = new Intent(getActivity(),CalendarViewActivity.class);
                                calendarIntent.putExtra("participant_id", ((ParticipantActivity) getActivity()).getParticipant_id());
                                startActivity(calendarIntent);
                            }
                        });

                        if(participant.isIndex()) {
                            calendarButton.setX(160);
                            calendarButton.setTop(68);
                            calendarText.setX(280);
                            calendarText.setTop(460);
                            artText.setVisibility(View.VISIBLE);
                            artButton.setVisibility(View.VISIBLE);
                            artButton.setX(650);
                            artButton.setTop(68);
                            artButton.setRight(16);
                            artText.setTop(460);
                            artText.setX(820);
                            artButton.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent artIntent = new Intent(getActivity(), ViralLoadActivity.class);
                                    artIntent.putExtra("participant_id", ((ParticipantActivity) getActivity()).getParticipant_id());
                                    startActivity(artIntent);
                                }
                            });
                        }

                    }
                }
                if(participant.isIndex()) {
                    artText.setVisibility(View.VISIBLE);
                    artButton.setVisibility(View.VISIBLE);
                    artButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent artIntent = new Intent(getActivity(), ViralLoadActivity.class);
                            artIntent.putExtra("participant_id", ((ParticipantActivity) getActivity()).getParticipant_id());
                            startActivity(artIntent);
                        }
                    });
                }
            }

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}
