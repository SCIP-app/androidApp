package scip.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.util.Log;
import android.view.View.OnClickListener;
import java.util.List;
import android.text.Editable;
import android.text.TextWatcher;
import scip.app.databasehelper.DatabaseHelper;
import android.widget.Button;


public  class ParticipantSelectionFragment extends Fragment {
    private ListView m_listview;
    DatabaseHelper db ;
    TextWatcher filterTextWatcher;
    EditText filterText;
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
            filterText = (EditText) view.findViewById(R.id.participantIDSearch);

            final ArrayAdapter<Long> adapter =
                new ArrayAdapter<Long>(getActivity(), android.R.layout.simple_list_item_1, participant_ids);


            filterTextWatcher = new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                adapter.getFilter().filter(s);
            }

            };


            filterText.addTextChangedListener(filterTextWatcher);

            filterText.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    filterText.setText("");
                }
            });

//            Button clear = (Button) view.findViewById(R.id.clear_participant);
//            clear.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    filterText.setText("");
//                }
//            });

            m_listview.setAdapter(adapter);

            m_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Long participantId = (Long) parent.getItemAtPosition(position);
                    Log.d("Participant id selected", String.valueOf(participantId));
                    Intent participant = new Intent(getActivity().getApplicationContext(),ParticipantActivity.class);
                    participant.putExtra("participant_id", participantId);
                    startActivity(participant);
                }
            });

            return view;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        filterText.removeTextChangedListener(filterTextWatcher);
    }


}
