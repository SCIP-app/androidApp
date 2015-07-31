package scip.app;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.GregorianCalendar;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import scip.app.databasehelper.CSVImporter;
import scip.app.databasehelper.DatabaseHelper;
import scip.app.models.MemsCap;
import scip.app.models.Participant;
import scip.app.models.SurveyResult;
import scip.app.models.ViralLoad;
import java.util.concurrent.TimeUnit;
import scip.app.models.DateUtil;

public class DataImportActivity extends ActionBarActivity {
    EditText statusUpdateArea;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_import);
        statusUpdateArea = (EditText)findViewById(R.id.dataStatusUpdateField);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        final Button importLocalData = (Button)findViewById(R.id.ImportLocalDataButon);
        final Button importMSurveyData = (Button) findViewById(R.id.ImportMSurveyDataButton);
        Button testDatabase = (Button) findViewById(R.id.ListDatabaseButton);
        final Button clearDatabase = (Button) findViewById(R.id.ClearDatabaseButton);
         final Button peakFertility = (Button) findViewById(R.id.TestFertilityPredictionButton);
        importLocalData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importLocalData(true);
            }
        });
        importMSurveyData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importMSurveyData();
            }
        });
        testDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testDatabase();
            }
        });
        clearDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDatabase();
            }
        });
        	        peakFertility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatePeakFertility();
            }
        });
    }
    private void calculatePeakFertility() {
        AsyncTask<Void, String, Void> pf = new CalculatePeakFertility().execute();
    }
    class CalculatePeakFertility extends AsyncTask<Void, String, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            List<Participant> participants= db.getAllParticipants();
            db.closeDB();
            Log.i("Num participants", String.valueOf(participants.size()));
            for(Participant p : participants) {
                if(p.isFemale()) {
                    List<Date> nextPeakFertilityWindow = p.getPeakFertility().getPeakFertilityWindow();
                    publishProgress("Participant id " + String.valueOf(p.getParticipantId()));
                    Calendar dec30 = new GregorianCalendar(2015, 9, 14);
                    publishProgress("Dec 30 is day " + String.valueOf(p.getPeakFertility().getDayInCycle(new Date(dec30.getTimeInMillis())))+ " in cycle.");
                    for (Date next : nextPeakFertilityWindow)
                        publishProgress(next.toString());
                }
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            statusUpdateArea.append("Calculating Peak Fertility for each Participant...\n");
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            statusUpdateArea.append("Done.\n");
            progressBar.setVisibility(View.INVISIBLE);
        }
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            statusUpdateArea.append(values[0]+"\n");
        }
    }

    private void importMSurveyData() {
        AsyncTask<Void, Void, String> ms = new RetrieveMSurveyData().execute();
    }

    private void importLocalData(boolean useLocal) {
        AsyncTask<Boolean, Void, Void> thread = new RetrieveLocalData().execute(useLocal);
    }

    class RetrieveLocalData extends AsyncTask<Boolean, Void, Void> {

        @Override
        protected Void doInBackground(Boolean... useLocal) {
            CSVImporter csvImporter = new CSVImporter(getApplicationContext());
            if(useLocal[0])
                csvImporter.openLocalFiles();
            else
                csvImporter.openExternalFiles();
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
        }
    }

    private void clearDatabase() {
        statusUpdateArea.append("Clearing database...\n");
        progressBar.setVisibility(View.VISIBLE);
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.deleteAllData();
        db.closeDB();
        statusUpdateArea.append("Database Cleared\n");
        progressBar.setVisibility(View.INVISIBLE);
    }

    class RetrieveMSurveyData extends AsyncTask<Void, Void, String> {

        private Exception exception;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            statusUpdateArea.append("Getting mSurvey Data\n");
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... voids) {
            try {
                String result = "BLANK";
                HttpClient httpclient = new DefaultHttpClient();
                BufferedReader r = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.msurvey_key)));
                String key = r.readLine();
                r.close();
                TimeZone tz = TimeZone.getTimeZone("UTC");
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
                df.setTimeZone(tz);
                String nowAsISO = df.format(new Date(System.currentTimeMillis()-24*60*60*1000));
                Log.d("yesterday", nowAsISO);
                HttpGet request = new HttpGet("https://apps.msurvey.co.ke/surveyapi/scip/data");
                HttpParams params = new BasicHttpParams();
                params.setParameter("format", "json");
                params.setParameter("start", nowAsISO);
                request.setParams(params);
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

        protected void onPostExecute(String result) {
            //Log.i("response", result);
            statusUpdateArea.append("Data retrieved. Parsing and storing...\n");
            JSONObject obj = null;
            try {
                obj = new JSONObject(result);
                JSONArray data = obj.getJSONArray("data");
                int n = data.length();
                statusUpdateArea.append("Participant count " + String.valueOf(n) + "\n");

                for (int i = 0; i < n; i++) {
                    JSONObject entry = data.getJSONObject(i);
                    Long participant_id = Long.valueOf(entry.getString("participant"));

                    Boolean hadSex;
                    if (entry.getString("had_sex").equals("No"))
                        hadSex = false;
                    else
                        hadSex = true;

                    Boolean onPeriod;
                    if (entry.getString("menses_started").equals("No"))
                        onPeriod = false;
                    else
                        onPeriod = true;

                    Boolean surveyComplete = entry.getBoolean("complete");

                    Boolean vaginaMucusSticky;
                    if (entry.getString("vaginal_mucus_stretchy").equals("No"))
                        vaginaMucusSticky = false;
                    else
                        vaginaMucusSticky = true;

                    Double temperature = Double.valueOf(entry.getString("basal_body_temp"));

                    Boolean passwordAccepted = entry.getBoolean("password_accepted");

                    Boolean usedCondom;
                    if (entry.getString("used_condom").equals("No"))
                        usedCondom = false;
                    else
                        usedCondom = true;

                    String timeStarted = entry.getString("time_started");
                    String wentToMarket = entry.getString("went_to_market");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                    timeStarted = timeStarted.replace("Z", "+00:00");
                    Date date = format.parse(timeStarted);

                    Boolean isOvulating;
                    if (entry.getString("ovulation_prediction").equals("Negative"))
                        isOvulating = false;
                    else
                        isOvulating = true;

                    SurveyResult sr = new SurveyResult(participant_id, date, temperature, vaginaMucusSticky, onPeriod, isOvulating, hadSex, usedCondom);
                    DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                    db.createSurveyResult (sr);
                    db.closeDB();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            // was getting parse exception error on format.parse(timeStarted) and adding this catch seemed to fix it
            } catch (ParseException e) {
                e.printStackTrace();
            }
            statusUpdateArea.append("Parsing complete.\n");
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    class TestDatabase extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            List<Participant> participantList = db.getAllParticipants();

            for(Participant p : participantList) {
                publishProgress("Participant id " + String.valueOf(p.getParticipantId()));
                if(p.isFemale()) {
                    publishProgress("Number of survey results: " + String.valueOf(p.getSurveyResults().size()));
                }

                if(p.isIndex()) {
                    publishProgress("Number of viral loads " + String.valueOf(p.getViralLoads().size()));
                }
                else {
                    publishProgress("Number of MemsCaps" + String.valueOf(p.getMemscaps().size()));
                }
            }

            List<Long> cids = db.getAllCoupleIDs();
            publishProgress("Total Number of Couples " + String.valueOf(cids.size()));
            List<SurveyResult> surveyResultList = db.getAllSurveyResults();
            publishProgress("Number of survey results " + String.valueOf(surveyResultList.size()));
            db.closeDB();

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            statusUpdateArea.append("Done.\n");
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            statusUpdateArea.append(values[0] + "\n");
        }
    }
    private void testDatabase() {
        AsyncTask<Void, String, Void> td = new TestDatabase().execute();
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