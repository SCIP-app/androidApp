package scip.app;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import scip.app.databasehelper.CSVImporter;
import scip.app.databasehelper.DatabaseHelper;
import scip.app.models.DateUtil;
import scip.app.models.MemsCap;
import scip.app.models.Participant;
import scip.app.models.SurveyResult;
import scip.app.models.ViralLoad;


public class DataImportActivity extends ActionBarActivity {
    EditText statusUpdateArea;
    ProgressBar progressBar;
    TextView welcomeMessage;
    String user;
    public Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_import);
        user = getIntent().getStringExtra("user");
        statusUpdateArea = (EditText)findViewById(R.id.dataStatusUpdateField);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        welcomeMessage = (TextView)findViewById(R.id.WelcomeMessageTextView);
        welcomeMessage.append(user);
        // getting tracker & setting User ID field (which isn't actually a solution after all, but I've left it here)
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.set("&uid", user);
        // build and send event
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("User ID")
                .setAction("Data login")
                .setLabel(user)
                .build());
        final Button importLocalData = (Button)findViewById(R.id.ImportLocalDataButon);
        final Button importMSurveyData = (Button) findViewById(R.id.ImportMSurveyDataButton);
        final Button importDatabaseBackup = (Button) findViewById(R.id.ImportDatabaseBackupButton);
        final Button prepareSDCard = (Button) findViewById(R.id.prepareSDCardButton);
        importLocalData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importLocalData(false);
            }
        });
        importMSurveyData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importMSurveyData();
            }
        });
        prepareSDCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<Void, String, Void> psd = new prepSDCard().execute();
            }
        });
        importDatabaseBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<Void, String, Void> pf = new ImportDatabaseBackup().execute();
            }
        });
    }



//    private void calculatePeakFertility() {
//        AsyncTask<Void, String, Void> pf = new CalculatePeakFertility().execute();
//    }
//    class CalculatePeakFertility extends AsyncTask<Void, String, Void> {
//        @Override
//        protected Void doInBackground(Void... params) {
//            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
//            List<Participant> participants= db.getAllParticipants();
//            db.closeDB();
//            Log.i("Num participants", String.valueOf(participants.size()));
//            for(Participant p : participants) {
//                if(p.isFemale()) {
//                    List<Date> nextPeakFertilityWindow = p.getPeakFertility().getPeakFertilityWindow();
//                    publishProgress("Participant id " + String.valueOf(p.getParticipantId()));
//                    Calendar dec30 = new GregorianCalendar(2015, 9, 14);
//                    publishProgress("Dec 30 is day " + String.valueOf(p.getPeakFertility().getDayInCycle(new Date(dec30.getTimeInMillis())))+ " in cycle.");
//                    for (Date next : nextPeakFertilityWindow)
//                        publishProgress(next.toString());
//                }
//            }
//            return null;
//        }
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            statusUpdateArea.append("Calculating Peak Fertility for each Participant...\n");
//            progressBar.setVisibility(View.VISIBLE);
//        }
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            statusUpdateArea.append("Done.\n");
//            progressBar.setVisibility(View.INVISIBLE);
//        }
//        @Override
//        protected void onProgressUpdate(String... values) {
//            super.onProgressUpdate(values);
//            statusUpdateArea.append(values[0] + "\n");
//        }
//    }

    private void importMSurveyData() {
        AsyncTask<Void, String, String> ms = new RetrieveMSurveyData().execute();
    }

    private void importLocalData(boolean useLocal) {
        AsyncTask<Boolean, String, Void> thread = new RetrieveLocalData().execute(useLocal);
    }

        class RetrieveLocalData extends AsyncTask<Boolean, String, Void> {

        private String TAG;

        @Override
        protected Void doInBackground(Boolean... useLocal) {
            CSVImporter csvImporter = new CSVImporter(getApplicationContext());
            HashMap resultsProcessed = null;
            try {
                //if (useLocal[0]) {
                if (true) {
                    resultsProcessed = csvImporter.openLocalFiles();
                }
                else {
                    resultsProcessed = csvImporter.openExternalFiles();
                }

                if(resultsProcessed.containsKey("participant"))
                    publishProgress(resultsProcessed.get("participant") + " participant records added");
                if(resultsProcessed.containsKey("memscap"))
                    publishProgress(resultsProcessed.get("memscap") + " memscap records added");
                if(resultsProcessed.containsKey("viralload"))
                    publishProgress(resultsProcessed.get("viralload") + " viral load records added");
                if(resultsProcessed.containsKey("surveyresults"))
                    publishProgress(resultsProcessed.get("surveyresults") + " survey result records added");

                return null;

            } catch (Exception e) {
                publishProgress("Error Processing Files");
                       }

           // mTracker.send(new EventBuilder()
           //         .setCategory(user)
           //         .setAction(user)
           //         .build());
            return null;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            statusUpdateArea.append("Importing local data...\n");
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            statusUpdateArea.append("Local data imported\n");
            progressBar.setVisibility(View.INVISIBLE);
            // automatically run the database backup
            AsyncTask<Void, String, String> pf = new BackupDatabase().execute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            statusUpdateArea.append(values[0]+"\n");
        }
    }

    class ImportDatabaseBackup extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... useLocal) {
            CSVImporter csvImporter = new CSVImporter(getApplicationContext());
            csvImporter.processBackUpFiles();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            statusUpdateArea.append("Reinstating database from backup...\n");
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            statusUpdateArea.append("Database restored\n");
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

//    private void clearDatabase() {
//        statusUpdateArea.append("Clearing database...\n");
//        progressBar.setVisibility(View.VISIBLE);
//        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
//        db.deleteAllData();
//        db.closeDB();
//        statusUpdateArea.append("Database Cleared\n");
//        progressBar.setVisibility(View.INVISIBLE);
//    }

    class RetrieveMSurveyData extends AsyncTask<Void, String, String> {

        private Exception exception;
        private String date;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            statusUpdateArea.append("Getting mSurvey Data\n");
            progressBar.setVisibility(View.VISIBLE);

            // Set up the date formatter
            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            df.setTimeZone(tz);

            // See if we've checked msurvey before. If not, look for 3 days worth of information
            SharedPreferences settings = getPreferences(0);
            date = settings.getString("lastReadMsurvey", null);
        }

        protected String fetchData(Uri url) {
            try {
                String result = "BLANK";
                HttpClient httpclient = new DefaultHttpClient();
                //BufferedReader r = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.msurvey_key)));
                BufferedReader r = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.memscap)));
                String key = r.readLine();
                r.close();

                HttpGet request = new HttpGet(url.toString());
                //Log.d("request", url.toString());
                request.addHeader("TOKEN", key);
                ResponseHandler<String> handler = new BasicResponseHandler();
                try {
                    result = httpclient.execute(request, handler);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                httpclient.getConnectionManager().shutdown();

                return result;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected Integer processSurveyResults(String result) {
            publishProgress("Survey data retrieved. Parsing and storing...");
            JSONObject obj = null;
            Integer count = 0;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                obj = new JSONObject(result);
                JSONArray data = obj.getJSONArray("data");
                int n = data.length();
                DatabaseHelper db = new DatabaseHelper(getApplicationContext());

                for (int i = 0; i < n; i++) {
                    JSONObject entry = data.getJSONObject(i);
                    Long participant_id = Long.valueOf(entry.getString("participant"));
                    Boolean hadSex = false;
                    if (entry.getString("had_sex").equals("Yes"))
                        hadSex = true;
                    Boolean onPeriod = false;
                    if (entry.getString("menses_started").equals("Yes"))
                        onPeriod = true;
                    Boolean surveyComplete = entry.getBoolean("complete");
                    Boolean vaginaMucusSticky = false;
                    if (entry.getString("vaginal_mucus_stretchy").equals("Yes"))
                        vaginaMucusSticky = true;
                    Double temperature;
                    try {
                        temperature = Double.parseDouble(entry.getString("basal_body_temp"));
                    } catch (Exception e) {
                        temperature = 0.0;
                    }
                    Boolean passwordAccepted = entry.getBoolean("password_accepted");
                    Boolean usedCondom = false;
                    if (entry.getString("used_condom").equals("Yes"))
                        usedCondom = true;
                    String lastUpdate = entry.getString("last_update");
                    String wentToMarket = entry.getString("went_to_market");
                    lastUpdate = lastUpdate.replace("Z", "+00:00");
                    Date date = df.parse(lastUpdate);
                    Boolean isOvulating = false;
                    if (entry.getString("ovulation_prediction").equals("Positive"))
                        isOvulating = true;

                    SurveyResult sr = new SurveyResult(participant_id, date, temperature, vaginaMucusSticky, onPeriod, isOvulating, hadSex, usedCondom);

                    if(db.createSurveyResult (sr))
                        count++;
                }
                db.closeDB();

                // Set up the date formatter
                TimeZone tz = TimeZone.getTimeZone("UTC");

                df.setTimeZone(tz);
                // Save the current date and time as the last time msurvey was checked
                SharedPreferences settings = getPreferences(0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("lastReadMsurvey", df.format(new Date(System.currentTimeMillis())));
                editor.commit();

                return count;
            } catch (Exception e) {
                e.printStackTrace();
                publishProgress("Error parsing survey result data.");
                return -1;
            }

        }

        protected Integer processParticipantData(String result) {
            publishProgress("Participants retrieved. Parsing and storing...");
            Integer count = 0;
            JSONObject obj = null;
            try {
                obj = new JSONObject(result);
                JSONArray data = obj.getJSONArray("data");
                int n = data.length();
                DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                for (int i = 0; i < n; i++) {
                    JSONObject entry = data.getJSONObject(i);
                    Long participantId = entry.getLong("participantID");
                    Boolean isFemale = false;
                    if (entry.getString("gender").toLowerCase().equals("female"))
                        isFemale = true;
                    if(db.createParticipant(new Participant(participantId, isFemale)))
                        count++;
                }
                db.closeDB();
                return count;
            } catch (Exception e) {
                e.printStackTrace();
                publishProgress("Error parsing participants.");
                return -1;
            }

        }

        protected String doInBackground(Void... voids) {

            Uri.Builder builder = new Uri.Builder()
                    .scheme("https")
                    .authority("apps.msurvey.co.ke")
                    .path("surveyapi/scip/data/")
                    .appendQueryParameter("format", "json");

            if (date != null)
                builder = builder.appendQueryParameter("start", date);

            Uri survey_url = builder.build();

            Uri participant_url = new Uri.Builder()
                    .scheme("https")
                    .authority("apps.msurvey.co.ke")
                    .path("surveyapi/scip/participants/")
                    .appendQueryParameter("format", "json")
                    .build();

            Integer newParticipantCount = processParticipantData(fetchData(participant_url));
            Integer newSurveyResultsCount = processSurveyResults(fetchData(survey_url));

            if(newParticipantCount != -1)
                publishProgress(newParticipantCount + " participant records added");
            if(newSurveyResultsCount != -1)
                publishProgress(newSurveyResultsCount + " survey result records added");

            return null;
        }

        protected void onPostExecute(String result) {
            //publishProgress("Parsing complete.");
            progressBar.setVisibility(View.INVISIBLE);
            //Log.i("response", result);

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            statusUpdateArea.append(values[0]+"\n");
        }
    }

//    class TestDatabase extends AsyncTask<Void, String, Void> {
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
//            List<Participant> participantList = db.getAllParticipants();
//
//            for(Participant p : participantList) {
//                publishProgress("Participant id " + String.valueOf(p.getParticipantId()));
//                if(p.isFemale()) {
//                    publishProgress("Number of survey results: " + String.valueOf(p.getSurveyResults().size()));
//                }
//
//                if(p.isIndex()) {
//                    publishProgress("Number of viral loads " + String.valueOf(p.getViralLoads().size()));
//                }
//                else {
//                    publishProgress("Number of MemsCaps" + String.valueOf(p.getMemscaps().size()));
//                }
//            }
//
//            List<Long> cids = db.getAllCoupleIDs();
//            publishProgress("Total Number of Couples " + String.valueOf(cids.size()));
//            List<SurveyResult> surveyResultList = db.getAllSurveyResults();
//            publishProgress("Number of survey results " + String.valueOf(surveyResultList.size()));
//            List<SurveyResult> surveyResults = db.getAllSurveyResults();
//            for(SurveyResult sr : surveyResults) {
//                Date sinceDate = new Date(System.currentTimeMillis()-24*60*60*1000);
//                if(sinceDate.compareTo(sr.getDate()) != 0)
//                    publishProgress(sr.toString());
//            }
//            db.closeDB();
//
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressBar.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            statusUpdateArea.append("Done.\n");
//            progressBar.setVisibility(View.INVISIBLE);
//        }
//
//        @Override
//        protected void onProgressUpdate(String... values) {
//            super.onProgressUpdate(values);
//            statusUpdateArea.append(values[0] + "\n");
//        }
//    }
//    private void testDatabase() {
//        AsyncTask<Void, String, Void> td = new TestDatabase().execute();
//    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {

            return true;
        }
        return false;
    }
    class prepSDCard extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (isExternalStorageWritable()) {
                File[] dirs = getExternalFilesDirs(null);
                if (!dirs[1].exists()) {
                    dirs[1].mkdir();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            statusUpdateArea.append("Preparing SD card...\n");
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            statusUpdateArea.append("SD card is ready \n");
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
    class BackupDatabase extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            try {
                // Get all data from database
                List<Participant> participants = db.getAllParticipants();
                List<SurveyResult> surveyResults = db.getAllSurveyResults();
                List<MemsCap> memsCaps = db.getAllMemsCap();
                List<ViralLoad> viralLoads = db.getAllViralLoads();

                if(isExternalStorageWritable()) {
                    File[] dirs = getExternalFilesDirs(null);
                    if(!dirs[1].exists()){
                        dirs[1].mkdir();
                    }
                    File file = new File(dirs[1], "participant_backup.txt");
                    file.createNewFile();
                    OutputStream participantFile = new FileOutputStream(file);

                    // Write participant file
                    //FileOutputStream participantFile = openFileOutput("participant_backup.txt", Context.MODE_PRIVATE);
                    for (Participant p : participants) {
                        String gender = "0";
                        if (p.isFemale())
                            gender = "1";
                        String string = p.getParticipantId() + ";" + gender + "\n";
                        participantFile.write(string.getBytes());
                    }
                    participantFile.close();

                    // Write survey results file
                    file = new File(dirs[1], "surveyresult_backup.txt");
                    OutputStream surveyResultFile = new FileOutputStream(file);
                    for (SurveyResult sr : surveyResults) {
                        String string = sr.getParticipant_id() + ";"
                                + DateUtil.getStringFromDate(sr.getDate()) + ";"
                                + sr.getTemperature() + ";"
                                + sr.isVaginaMucusSticky() + ";"
                                + sr.isOvulating() + ";"
                                + sr.isOnPeriod() + ";"
                                + sr.isHadSex() + ";"
                                + sr.isUsedCondom() + "\n";
                        surveyResultFile.write(string.getBytes());
                    }
                    surveyResultFile.close();

                    // Write viral loads file
                    file = new File(dirs[1], "viralload_backup.txt");
                    OutputStream viralLoadsFile = new FileOutputStream(file);
                    for (ViralLoad vl : viralLoads) {
                        String string = vl.getParticipant_id() + ";"
                                + vl.getVisit_id() + ";"
                                + DateUtil.getStringFromDate(vl.getDate()) + ";"
                                + vl.getNumber() + "\n";
                        viralLoadsFile.write(string.getBytes());
                    }
                    viralLoadsFile.close();

                    // Write mems file
                    file = new File(dirs[1], "memscap_backup.txt");
                    OutputStream memscapFile = new FileOutputStream(file);
                    for (MemsCap mc : memsCaps) {
                        String string = mc.getParticipant_id() + ";"
                                + mc.getMems_id() + ";"
                                + DateUtil.getStringFromDate(mc.getDate()) + "\n";
                        memscapFile.write(string.getBytes());
                    }
                    memscapFile.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                publishProgress("Backup failed.");
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            statusUpdateArea.append("Backing up database to SD card.\n");
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            statusUpdateArea.append("Done.\n");
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            statusUpdateArea.append(values[0] + "\n");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data_import, menu);
        return true;
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
