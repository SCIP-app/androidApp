package scip.app.databasehelper;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import scip.app.R;
import scip.app.models.MemsCap;
import scip.app.models.Participant;
import scip.app.models.SurveyResult;
import scip.app.models.ViralLoad;

/**
 * Created by Allie on 7/6/2015.
 */
public class CSVImporter {
    Context context;

    public CSVImporter(Context context) {
        this.context = context;
    }

    public void readMemsCapData(List<String[]> memsList) {
        DatabaseHelper db = new DatabaseHelper(context);
        for(String[] entry : memsList) {
//            Log.d("Participant Id", entry[0]);
//            Log.d("Mems Id", entry[1]);
//            Log.d("Date", entry[2]);
//            Log.d("Time", entry[3]);

            long participant_id = Long.parseLong(entry[0]);
            long mems_id = Long.parseLong(entry[1]);

//            if(db.getParticipant(participant_id) == null) {
//                db.createParticipant(new Participant(participant_id));
//            }

            db.createMemsCap(new MemsCap(participant_id, entry[2], mems_id));
        }

        db.closeDB();
    }

    public void readViralLoadData(List<String[]> viralLoadList) {
        DatabaseHelper db = new DatabaseHelper(context);
        for(String[] entry : viralLoadList) {
//            Log.d("Participant Id", entry[0]);
//            Log.d("Visit Id", entry[1]);
//            Log.d("Date", entry[2]);
//            Log.d("Load", entry[3]);

            long participant_id = Long.parseLong(entry[0]);
            int vist_id = Integer.parseInt(entry[1]);
            int load = Integer.parseInt(entry[3]);

//            if(db.getParticipant(participant_id) == null) {
//                db.createParticipant(new Participant(participant_id));
//            }

           db.createViralLoad(new ViralLoad(participant_id, load, entry[2], vist_id));

        }
    }

    public void readParticipantData (List<String[]> participantList) {
        DatabaseHelper db = new DatabaseHelper(context);
        for(String[] entry : participantList) {
            Log.d("Participant Id", entry[0]);
            Log.d("isFemale", "'"+entry[1]+"'");

            long participant_id = Long.parseLong(entry[0]);
            boolean isFemale = false;
            if (entry[1].contains("1"))
                isFemale = true;
            db.createParticipant(new Participant(participant_id, isFemale));

        }
    }

    public void readSurveyResults (List<String[]> surveyResultList) {
        DatabaseHelper db = new DatabaseHelper(context);
        for(String[] entry : surveyResultList) {
//            Log.d("Participant Id", entry[0]);
//            Log.d("date", entry[1]);
//            Log.d("temperature", entry[2]);
//            Log.d("vaginaMucusSticky", entry[3]);
//            Log.d("onPeriod", entry[4]);
//            Log.d("isOvulating", entry[5]);
//            Log.d("hadSex", entry[6]);
//            Log.d("usedCondom", entry[7]);

            long participant_id = Long.parseLong(entry[0]);
            String date = entry[1];
            double temperature = Double.parseDouble(entry[2]);
            boolean vaginaMucusSticky = false;
            if(entry[3].contains("true"))
                vaginaMucusSticky = true;
            boolean onPeriod = false;
            if(entry[4].contains("true"))
                onPeriod = true;
            boolean isOvulating = false;
            if(entry[5].contains("true"))
                isOvulating = true;
            boolean hadSex = false;
            if(entry[6].contains("true"))
                hadSex = true;
            boolean usedCondom = false;
            if(entry[7].contains("true"))
                usedCondom = true;

            db.createSurveyResult(new SurveyResult(participant_id, date, temperature, vaginaMucusSticky, onPeriod, isOvulating, hadSex, usedCondom));

        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public void openExternalFiles() {
        if(isExternalStorageWritable()) {
            File[] externalDirs = context.getExternalFilesDirs(null);
            Toast.makeText(context, String.valueOf(externalDirs.length), Toast.LENGTH_LONG).show();
            Log.d("External Dirs length", String.valueOf(externalDirs.length));
            Log.d("Path", externalDirs[0].getAbsolutePath());
//            for(File f : externalDirs[0].listFiles()) {
//                Log.d("File ", f.getName());
//                if(f.getName().contains("memscap")) {
//                    CSVFile csvFile = new CSVFile(f);
//                    List<String[]> memsList = csvFile.read();
//                    readMemsCapData(memsList);
//                }
//                else if (f.getName().contains("viralload")){
//                    CSVFile csvFile = new CSVFile(f);
//                    List<String[]> viralLoadList = csvFile.read();
//                    readViralLoadData(viralLoadList);
//                }
//            }
            for(File f : externalDirs[1].listFiles()) {
                Log.d("File ", f.getName());
                if(f.getName().contains("memscap")) {
                    CSVFile csvFile = new CSVFile(f);
                    List<String[]> memsList = csvFile.read();
                    readMemsCapData(memsList);
                    Toast.makeText(context, "Done with MEMS Cap", Toast.LENGTH_LONG).show();
                }
                else if (f.getName().contains("viralload")){
                    CSVFile csvFile = new CSVFile(f);
                    List<String[]> viralLoadList = csvFile.read();
                    readViralLoadData(viralLoadList);
                    Toast.makeText(context, "Done with Viral Load", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void openLocalFiles() {
        CSVFile vl = new CSVFile(context.getResources().openRawResource(R.raw.viralload));
        CSVFile mc = new CSVFile(context.getResources().openRawResource(R.raw.memscap));
        CSVFile p = new CSVFile(context.getResources().openRawResource(R.raw.participants));
        CSVFile sr = new CSVFile(context.getResources().openRawResource(R.raw.surveyresults));
        readViralLoadData(vl.read());
        readMemsCapData(mc.read());
        readParticipantData(p.read());
        readSurveyResults(sr.read());
    }

}
