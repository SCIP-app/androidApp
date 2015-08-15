package scip.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import scip.app.databasehelper.DatabaseHelper;

/*
 * ParticipantSelectionFragment: A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link CoupleSelectionFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link CoupleSelectionFragment#newInstance} factory method to create
 * an instance of this fragment.
 *
 * Class variables:
 *  m_listview:
 *      Type: A standard ListView object
 *  ASK WHY ONLY LISTVIEW AS A CLASS VARIABLE
 *
 * Functions:
 *  ParticipantSelectionFragment (Null): Setter for a new instance of the CoupleSelectionFragment
 *
 *  onCreateView (LayoutInflater, ViewGroup, Bundle): Creates and sets a View type layout for this
 *  fragment using a series of setter methods (is this how you'd say it?)
 *      input parameters: Standard LayoutInflator, ViewGroup, Bundle
 *      output parameters:
 *          view: A selectable list view of the couple ID
 *              Type: View
 */

public class ParticipantSelectionFragment extends Fragment {

    private ListView m_listview;

    public  static ParticipantSelectionFragment newInstance() {
        ParticipantSelectionFragment fragment = new ParticipantSelectionFragment();
        return fragment;
    }

    public ParticipantSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_participant_selection, container, false);
        m_listview = (ListView) view.findViewById(R.id.participant_list);

        DatabaseHelper db = new DatabaseHelper(getActivity().getApplicationContext());
        List<Long> participants = db.getAllParticipantIds();
        db.closeDB();

        ArrayAdapter<Long> adapter =
                new ArrayAdapter<Long>(getActivity(), android.R.layout.simple_list_item_1, participants);

        m_listview.setAdapter(adapter);

        m_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Long participant_id = (Long) parent.getItemAtPosition(position);
                Log.d("participant id selected", String.valueOf(participant_id));

                // TODO: for now, just pass in couple id because don't have participant view
                Intent dashboard = new Intent(getActivity().getApplicationContext(), DashboardActivity.class);
                dashboard.putExtra("couple_id", participant_id/100);
                startActivity(dashboard);
            }
        });

        return view;

    }



}