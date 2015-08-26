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


/*
 * CoupleSelectionFragment: A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link CoupleSelectionFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link CoupleSelectionFragment#newInstance} factory method to create
 * an instance of this fragment.
 *
 * Class variables:
 *  m_listview:
 *      Type: A standard ListView object
 *  db:
 *      Type: DatabaseHelper object
 *  couple_ids:
 *      Type: An arraylist of couple ids
 *
 * Functions:
 *  CoupleSelectionFragment (Null): Setter for a new instance of the CoupleSelectionFragment
 *
 *  onCreateView (LayoutInflater, ViewGroup, Bundle): Creates and sets a View type layout for this
 *  fragment using a series of setter methods (is this how you'd say it?)
 *      input parameters: Standard LayoutInflator, ViewGroup, Bundle
 *      output parameters:
 *          view: A selectable list view of the couple ID
 *              Type: View
 *
 * ASK ABOUT SYNTAX
 */
public  class CoupleSelectionFragment extends Fragment {
    private ListView m_listview;
    DatabaseHelper db ;
    List<Long> couple_ids;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_couple_selection, container, false);
        m_listview = (ListView)view.findViewById(R.id.couple_list);

        db = new DatabaseHelper(getActivity().getApplicationContext());
        couple_ids = db.getAllCoupleIDs();
        db.closeDB();
        
        ArrayAdapter<Long> adapter = new ArrayAdapter<Long>(getActivity(), android.R.layout.simple_list_item_1, couple_ids);

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
