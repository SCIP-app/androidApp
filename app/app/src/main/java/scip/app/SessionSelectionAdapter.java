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
 * Functions:
 * @ Override methods update the original methods in FragmentPagerAdapter class
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
