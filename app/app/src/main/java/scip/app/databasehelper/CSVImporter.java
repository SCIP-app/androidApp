package scip.app.databasehelper;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Integer readMemsCapData(List<String[]> memsList) {
        DatabaseHelper db = new DatabaseHelper(context);
        Integer count = 0;
        for(String[] entry : memsList) {
//            Log.d("Participant Id", entry[0]);
//            Log.d("Mems Id", entry[1]);
//            Log.d("Date", entry[2]);

            try {
                long participant_id = Long.parseLong(entry[0]);
                long mems_id = Long.parseLong(entry[1]);
                //db.createParticipant(new Participant(participant_id, false));
                if(db.createMemsCap(new MemsCap(participant_id, entry[2], mems_id)))
                    count++;
            }
            catch (Exception e) {

            }
        }

        db.closeDB();
        return count;
    }

    public Integer readViralLoadData(List<String[]> viralLoadList) {
        Integer count = 0;
        DatabaseHelper db = new DatabaseHelper(context);
        for(String[] entry : viralLoadList) {
//            Log.d("Participant Id", entry[0]);
//            Log.d("Visit Id", entry[1]);
//            Log.d("Date", entry[2]);
//            Log.d("Load", entry[3]);
            try {
                long participant_id = Long.parseLong(entry[0]);
                int vist_id = Integer.parseInt(entry[1]);
                int load = Integer.parseInt(entry[3]);
                //db.createParticipant(new Participant(participant_id, false));
                if(db.createViralLoad(new ViralLoad(participant_id, load, entry[2], vist_id)))
                    count++;
            }
            catch (Exception e) {

            }
        }
        db.closeDB();
        return count;
    }

    public Integer readParticipantData (List<String[]> participantList) {
        Integer count = 0;
        DatabaseHelper db = new DatabaseHelper(context);
        for(String[] entry : participantList) {
            //Log.d("Participant Id", entry[0]);
            //Log.d("isFemale", "'"+entry[1]+"'");

            long participant_id = Long.parseLong(entry[0]);
            boolean isFemale = false;
            if (entry[1].contains("1"))
                isFemale = true;
            if(db.createParticipant(new Participant(participant_id, isFemale)))
                count++;

        }
        db.closeDB();
        return count;
    }

    public Integer readSurveyResults (List<String[]> surveyResultList) {
        Integer count = 0;
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
            //db.createParticipant(new Participant(participant_id, true));

            if(db.createSurveyResult(new SurveyResult(participant_id, date, temperature, vaginaMucusSticky, onPeriod, isOvulating, hadSex, usedCondom)))
                count++;
        }
        db.closeDB();
        return count;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public HashMap<String, Integer> openExternalFiles() {
        HashMap processedValues = new HashMap<String, Integer>();

        if(isExternalStorageWritable()) {
            File[] externalDirs = context.getExternalFilesDirs(null);
            Log.d("External Dirs length", String.valueOf(externalDirs.length));

            // If the SD card exists, it will be located the second directory in the list of available directories
            for(File f : externalDirs[1].listFiles()) {
                //Log.d("File ", f.getName());
                if(f.getName().contains("memscap") && !f.getName().contains("backup")) {
                    CSVFile csvFile = new CSVFile(f);
                    List<String[]> memsList = csvFile.read();
                    processedValues.put("memscap", readMemsCapData(memsList));
                    //f.delete();  // delete the file when you're done so the team knows it was successful
                }
                else if (f.getName().contains("viralload") && !f.getName().contains("backup")){
                    CSVFile csvFile = new CSVFile(f);
                    List<String[]> viralLoadList = csvFile.read();
                    processedValues.put("viralload", readViralLoadData(viralLoadList));
                    //f.delete();  // delete the file when you're done so the team knows it was successful
                }
                else if (f.getName().contains("surveyresult") && !f.getName().contains("backup")){
                    CSVFile csvFile = new CSVFile(f);
                    List<String[]> surveyResultList = csvFile.read();
                    processedValues.put("surveyresults", readSurveyResults(surveyResultList));
                    //f.delete();  // delete the file when you're done so the team knows it was successful
                }
                else if (f.getName().contains("participant") && !f.getName().contains("backup")){
                    CSVFile csvFile = new CSVFile(f);
                    List<String[]> participantList = csvFile.read();
                    processedValues.put("participant", readParticipantData(participantList));
                    //f.delete();  // delete the file when you're done so the team knows it was successful
                }
            }
        }
        return processedValues;
    }

    public void processBackUpFiles() {
        if(isExternalStorageWritable()) {
            File[] externalDirs = context.getExternalFilesDirs(null);

            // If the SD card exists, it will be located the second directory in the list of available directories
            for(File f : externalDirs[1].listFiles()) {
                //Log.d("File ", f.getName());
                // only read in backup files
                if(f.getName().contains("backup")) {
                    if (f.getName().contains("memscap")) {
                        CSVFile csvFile = new CSVFile(f);
                        List<String[]> memsList = csvFile.read();
                        readMemsCapData(memsList);
                    } else if (f.getName().contains("viralload")) {
                        CSVFile csvFile = new CSVFile(f);
                        List<String[]> viralLoadList = csvFile.read();
                        readViralLoadData(viralLoadList);
                    } else if (f.getName().contains("participant")) {
                        CSVFile csvFile = new CSVFile(f);
                        List<String[]> participantList = csvFile.read();
                        readParticipantData(participantList);
                    } else if (f.getName().contains("surveyresult")) {
                        CSVFile csvFile = new CSVFile(f);
                        List<String[]> surveyResultList = csvFile.read();
                        readSurveyResults(surveyResultList);
                    }
                }
            }
        }
    }

    public HashMap<String, Integer> openLocalFiles() {
        CSVFile vl = new CSVFile(context.getResources().openRawResource(R.raw.viralload));
        CSVFile mc = new CSVFile(context.getResources().openRawResource(R.raw.memscap));
        CSVFile p = new CSVFile(context.getResources().openRawResource(R.raw.participants));
        CSVFile sr = new CSVFile(context.getResources().openRawResource(R.raw.surveyresults));

        HashMap processedValues = new HashMap<String, Integer>();
        processedValues.put("viralload", readViralLoadData(vl.read()));
        processedValues.put("memscap", readMemsCapData(mc.read()));
        processedValues.put("participant", readParticipantData(p.read()));
        processedValues.put("surveyresults", readSurveyResults(sr.read()));

        return processedValues;
    }

}
