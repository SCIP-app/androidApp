package scip.app;


import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import scip.app.databasehelper.DatabaseHelper;
import scip.app.models.Participant;
import scip.app.models.PeakFertility;
import scip.app.models.SurveyResult;
import scip.app.models.ViralLoad;


public class LoginActivity extends Activity{

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);

        List<Participant> participants = getParticipantList();
        //populateDatabase(participants);
        //testDatabase(participants);


        for(Participant p : participants) {
            Log.d("P ID", String.valueOf(p.getParticipantId()));
            Log.d("C ID", String.valueOf(p.getCoupleId()));
        }


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

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            Intent intent = new Intent(this,SessionSelectionActivity.class);
            startActivity(intent);

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private List<Participant> getParticipantList() {
        List<Participant> participants = new ArrayList<>();

        Participant participant1 = new Participant(getApplicationContext(), 987654321); // Male
        Participant participant2 = new Participant(getApplicationContext(), 123456789); // Female in partner
        Participant participant3 = new Participant(getApplicationContext(), 123456766); // Male in partner

        participants.add(participant1);
        participants.add(participant2);
        participants.add(participant3);

        return participants;
    }
    private void populateDatabase(List<Participant> participants) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        ViralLoad viralLoad1 = new ViralLoad(participants.get(0).getParticipantId(), 1234, "25/06/2015", 6789);
        ViralLoad viralLoad2 = new ViralLoad(participants.get(0).getParticipantId(), 5555, "25/08/2015", 1245);
        ViralLoad viralLoad3 = new ViralLoad(participants.get(2).getParticipantId(), 3333, "21/06/2015", 5894);

        SurveyResult surveyResult1 = new SurveyResult(participants.get(1).getParticipantId(), "25/06/2015", 98.4, 1, 0, 0, 0, 0);
        SurveyResult surveyResult2 = new SurveyResult(participants.get(1).getParticipantId(), "26/06/2015", 98.1, 1, 0, 0, 0, 0);
        SurveyResult surveyResult3 = new SurveyResult(participants.get(1).getParticipantId(), "27/06/2015", 98.5, 0, 1, 0, 0, 0);

        PeakFertility peakFertility1 = new PeakFertility(participants.get(1).getParticipantId(), "24/07/2015", "26/07/2015");

        db.createParticipant(participants.get(0));
        db.createParticipant(participants.get(1));
        db.createParticipant(participants.get(2));

        db.createViralLoad(viralLoad1);
        db.createViralLoad(viralLoad2);
        db.createViralLoad(viralLoad3);

        db.createSurveyResult(surveyResult1);
        db.createSurveyResult(surveyResult2);
        db.createSurveyResult(surveyResult3);

        db.createPeakFertility(peakFertility1);

        db.closeDB();
    }

    private void testDatabase(List<Participant> participantList) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List<Participant> participants= db.getAllParticipants();
        for(Participant participant : participants) {
            Log.d("Participant id", String.valueOf(participant.getParticipantId()));
        }

        List<PeakFertility> pfs = db.getAllPeakFertilityById(participantList.get(1).getParticipantId());
        for(PeakFertility pf : pfs) {
            Log.d("PF", String.valueOf(pf.getParticipant_id()));
        }

        List<ViralLoad> vls = db.getAllViralLoadsById(participantList.get(0).getParticipantId());
        for(ViralLoad vl : vls) {
            Log.d("VL", String.valueOf(vl.getParticipant_id()));
            Log.d("VL: number", String.valueOf(vl.getNumber()));
        }

        vls = db.getAllViralLoadsById(participantList.get(2).getParticipantId());
        for(ViralLoad vl : vls) {
            Log.d("VL", String.valueOf(vl.getParticipant_id()));
            Log.d("VL: number", String.valueOf(vl.getNumber()));
        }

        List<SurveyResult> srs = db.getAllSurveyResultsById(participantList.get(1).getParticipantId());
        for (SurveyResult sr : srs) {
            Log.d("SR", String.valueOf(sr.getParticipant_id()));
            Log.d("SR: Temp", String.valueOf(sr.getTemperature()));
        }

        List<Long> cids = db.getAllCoupleIDs();
        for(Long cid : cids) {
            Log.d("Couple ID", String.valueOf(cid));
        }

        List<Participant> couple = db.getCoupleFromID(participantList.get(1).getCoupleId());
        for(Participant p : couple) {
            Log.d("P in C", String.valueOf(p.getParticipantId()));
        }

        Participant partner = participantList.get(1).getPartner();
        Log.d("Participant", String.valueOf(participantList.get(1).getParticipantId()));
        Log.d("Partner is", String.valueOf(partner.getParticipantId()));

        db.closeDB();
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */

/**
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
        */
}

