package scip.app;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import scip.app.databasehelper.DatabaseHelper;

/*
SessionSelectionActivity
    Description: This class is a tabbed view presenting couples or participant ids for selection

    Functions:
        onCreate (Bundle): Sets up the participant and couple selection
        for the SessionSelectionAdapter

        Input parameters:
            Bundle: Contains the instance state to initialize (terminology?)

        Output parameters: null
 */

public class SessionSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_selection);

        // Get the ViewPager and set its PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SessionSelectionAdapter(getSupportFragmentManager(),
                SessionSelectionActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
    }

}
