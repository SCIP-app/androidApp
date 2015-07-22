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
    private int mPage;

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
        m_listview = (ListView)view.findViewById(R.id.listView);

        String[] items = new String[] {"CoupleID 1", "CoupleID 2", "CoupleID 3"};
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);

        m_listview.setAdapter(adapter);

        m_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent dashboard = new Intent(getActivity().getApplicationContext(), DashboardActivity.class);
                startActivity(dashboard);
            }
        });
        
        return view;

    }



}
