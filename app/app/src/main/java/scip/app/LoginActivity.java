package scip.app;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;


import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

import scip.app.databasehelper.CSVImporter;
import scip.app.databasehelper.DatabaseHelper;
import scip.app.models.MemsCap;
import scip.app.models.Participant;
import scip.app.models.PeakFertility;
import scip.app.models.SurveyResult;
import scip.app.models.ViralLoad;

public class LoginActivity extends ActionBarActivity {
    public static GoogleAnalytics analytics;
    public static Tracker tracker;
    private String currentUser = "";

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);
        tracker = analytics.newTracker("UA-65988344-1");
        tracker.enableExceptionReporting(true);
        tracker.enableAutoActivityTracking(true);


        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                loadCouples();
            }
        });

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * Does not take into account if the user account already exists
     */
    public void loadCouples() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid username/password combination.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }else if (!isLoginValid(email, password)) {
            mEmailView.setError(getString(R.string.error_login_invalid));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else if (currentUser.equals("clinicteam")) {
            Intent intent = new Intent(this, SessionSelectionActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, DataImportActivity.class);
            intent.putExtra("user", currentUser);
            startActivity(intent);
        }
    }

    private boolean isLoginValid(String username, String password) {
        SharedPreferences settings = getPreferences(0);
        if(!settings.getBoolean("loginsSet", false)) {
            // process the logins file
            Log.d("Login", "Reading passwords");
            SharedPreferences.Editor editor = settings.edit();
            BufferedReader reader = new BufferedReader(new InputStreamReader(getApplicationContext().getResources().openRawResource(R.raw.users)));
            try {
                String user;
                while ((user = reader.readLine()) != null) {
                    String pwd = reader.readLine();
                    editor.putString(user, pwd);
                }
            }
            catch (Exception e) {
                Log.d("Login", "exception in reading");
            }
            editor.putBoolean("loginsSet", true);
            editor.commit();
        }
        Log.d("Username", username);
        Log.d("Password", password);
        Log.d("password stored", settings.getString(username, "BLANK"));

        // Check for the username, if it doesn't exist, say the login isn't valid
        // Check if the password matches, if not, login isn't valid
        if(settings.contains(username) && password.equals(settings.getString(username, ""))) {
            currentUser = username;
            return true;
        }

        return false;
    }


}
