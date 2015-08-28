package scip.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import scip.app.databasehelper.DatabaseHelper;
import scip.app.models.Participant;


public class DashboardActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    List<Participant> couple;
    long couple_id;

    FragmentTransaction fragmentTransaction = null;
    private void endSession() {
        Intent intent = new Intent(this, SessionSelectionActivity.class);
        startActivity(intent);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        couple_id = getIntent().getLongExtra("couple_id", 0);
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        couple = db.getCoupleFromID(couple_id);
        db.closeDB();

//        for(Participant p : couple) {
//            Log.d("Participant id in couple", String.valueOf(p.getParticipantId()));
//            if(p.isFemale()) {
//                int length = p.getSurveyResults().size();
//                Log.d("# of SurveyResults", String.valueOf(length));
//            }
//
//            if(p.isIndex()) {
//                int length = p.getViralLoads().size();
//                Log.d("# of ViralLoads", String.valueOf(length));
//            }
//            else {
//                int length = p.getMemscaps().size();
//                Log.d("# of MemsCaps", String.valueOf(length));
//            }
//        }
        if (couple.size() > 2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.couple_num_error_message)
                    .setPositiveButton(R.string.couple_num_error_positive, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Do nothing and let them continue
                        }
                    })
                    .setNegativeButton(R.string.couple_num_error_negative, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            endSession();
                        }
                    });
            // Create the AlertDialog object and return it
            AlertDialog alert = builder.create();
            alert.show();
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


    }


    public List<Participant> getCouple() {
        return couple;
    }

    public long getCouple_id() {
        return couple_id;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        // defines the behavior of each element inside the navigation drawer
        switch (position) {
            case 0:

                Bundle bundle = new Bundle();
                bundle.putLong("coupleId",couple_id);
                LandingFragment coupleFragment = new LandingFragment();
                coupleFragment.setArguments(bundle);
                mTitle  = "Couples Main";
                fragmentTransaction.replace(R.id.content_frame, coupleFragment);
                fragmentTransaction.commit();
                break;
            case 1:
                Intent sessionIntent = new Intent(this,SessionSelectionActivity.class);
                startActivity(sessionIntent);
                break;
            case 2:
                Intent loginIntent = new Intent(this,LoginActivity.class);
                startActivity(loginIntent);
                break;

            default:
                break;
        }



    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.dashboard, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
