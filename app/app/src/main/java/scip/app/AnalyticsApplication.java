package scip.app;

import android.app.Application;

import android.app.Application;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Kristin Dew on 8/27/2015.
 */
public class  AnalyticsApplication extends Application {
        private Tracker mTracker;

        /**
         * Gets the default {@link Tracker} for this {@link Application}.
         * @return tracker
         */
        synchronized public Tracker getDefaultTracker() {
            if (mTracker == null) {
                GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
                // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
                mTracker = analytics.newTracker(R.xml.global_tracker);
            }
            return mTracker;
        }
    }

