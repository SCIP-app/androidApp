package scip.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.util.Log;
import java.util.List;
import scip.app.databasehelper.DatabaseHelper;


public  class ParticipantSelectionFragment extends Fragment {
    private ListView m_listview;
    DatabaseHelper db ;
    List<Long> participant_ids;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_participant_selection, container, false);
        m_listview = (ListView)view.findViewById(R.id.participant_list);

        db = new DatabaseHelper(getActivity().getApplicationContext());
        participant_ids = db.getAllParticipantIds();
        db.closeDB();

        ArrayAdapter<Long> adapter =
                new ArrayAdapter<Long>(getActivity(), android.R.layout.simple_list_item_1, participant_ids);

        m_listview.setAdapter(adapter);

        m_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Long participantId = (Long) parent.getItemAtPosition(position);
                //Log.d("Participant id selected", String.valueOf(participantId));
                Intent participant = new Intent(getActivity().getApplicationContext(),ParticipantActivity.class);
                participant.putExtra("participant_id", participantId);
                startActivity(participant);
            }
        });

        return view;

    }



}
