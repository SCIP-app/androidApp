package scip.app;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created by Yesh on 7/14/15.
 * SessionSelectionActivity
 * Description: This class customizes the view for the SessionSelectionActivity
 *
 * Class variables:
 *  PAGE_COUNT
 *      type: int
 *      description: Not sure how this is actually used
 *  tabTitles
 *      type: String [array]
 *      description: Sets two tabs
 *
 * Functions: @ Override methods update the original methods in FragmentPagerAdapter class
 *  SessionSelectionAdapter (FragmentManager, Context): Constructor for the fragment manager
 *      input parameters:
 *          FragmentManager - a standard fragment manager
 *          Context - contains the state of the app
 *      output parameters: Null
 *
 *
 *  getCount (null): Getter for the PAGE_COUNT
 *
 *  getItem (int): Fragment method that sets up the selectable lists of couples and participants
 *      input parameters:
 *          int - contains the position for the tabs
 *      output parameters:
 *          CoupleSelectionFragment - a new instance of the CoupleSelectionFragment (at index 0 - L tab)
 *          ParticipantSelectionFragment - a new instance of ParticipantSelection Fragment (at index 1 - R tab)
 *
 */

public class SessionSelectionAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Enrolled Couples", "Enrolled Participants"};
    private Context context;
    FragmentManager fragmentManager;

    public SessionSelectionAdapter(FragmentManager fm, Context context) {
        super(fm);
        fragmentManager = fm;
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
             return CoupleSelectionFragment.newInstance();
            case 1:
                return ParticipantSelectionFragment.newInstance();
        }
        return CoupleSelectionFragment.newInstance();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
