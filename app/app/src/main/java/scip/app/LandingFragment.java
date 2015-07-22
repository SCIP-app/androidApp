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

import scip.app.models.ViralLoad;


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
        View view = inflater.inflate(R.layout.fragment_landing_main,
                container, false);
        ImageButton calendarButton = (ImageButton) view.findViewById(R.id.calendarButton);
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

        ImageButton artButton = (ImageButton) view.findViewById(R.id.artButton);
        artButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent artIntent = new Intent(getActivity(),ViralLoadActivity.class);
                startActivity(artIntent);


            }
        });

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
