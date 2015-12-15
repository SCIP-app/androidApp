package scip.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.util.Log;
import java.util.List;
import scip.app.databasehelper.DatabaseHelper;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CoupleSelectionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CoupleSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public  class CoupleSelectionFragment extends Fragment {
    private ListView m_listview;
    DatabaseHelper db ;
    TextWatcher filterTextWatcher;
    EditText filterText;
    List<Long> couple_ids;
    Button clear;

    public  static CoupleSelectionFragment newInstance() {
        CoupleSelectionFragment fragment = new CoupleSelectionFragment();
        return fragment;
    }

    public CoupleSelectionFragment() {

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

        View view =  inflater.inflate(R.layout.fragment_couple_selection, container, false);
        m_listview = (ListView)view.findViewById(R.id.couple_list);

        db = new DatabaseHelper(getActivity().getApplicationContext());
        couple_ids = db.getAllCoupleIDs();
        db.closeDB();
        
        final ArrayAdapter<Long> adapter =
                new ArrayAdapter<Long>(getActivity(), android.R.layout.simple_list_item_1, couple_ids);

        filterText = (EditText) view.findViewById(R.id.coupleIDSearch);
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
        filterText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                filterText.setText("");
            }
        });

//      clear = (Button) view.findViewById(R.id.clear_couple);
//        clear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                filterText.setText("");
//            }
//       });

        m_listview.setAdapter(adapter);

        m_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Long couple_id = (Long) parent.getItemAtPosition(position);
                Log.d("Couple id selected", String.valueOf(couple_id));
                Intent dashboard = new Intent(getActivity().getApplicationContext(),DashboardActivity.class);
                dashboard.putExtra("couple_id", couple_id);
                startActivity(dashboard);
            }
        });
        
        return view;

    }



}
