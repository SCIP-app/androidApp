package scip.app.databasehelper;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import scip.app.R;
import scip.app.models.MemsCap;
import scip.app.models.Participant;
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
            Log.d("Participant Id", entry[0]);
            Log.d("Mems Id", entry[1]);
            Log.d("Date", entry[2]);
            Log.d("Time", entry[3]);

            long participant_id = Long.parseLong(entry[0]);
            long mems_id = Long.parseLong(entry[1]);

            if(db.getParticipant(participant_id) == null) {
                db.createParticipant(new Participant(participant_id));
            }

            db.createMemsCap(new MemsCap(participant_id, entry[2], mems_id));
        }

        db.closeDB();
    }

    public void readViralLoadData(List<String[]> viralLoadList) {
        DatabaseHelper db = new DatabaseHelper(context);
        for(String[] entry : viralLoadList) {
            Log.d("Participant Id", entry[0]);
            Log.d("Visit Id", entry[1]);
            Log.d("Date", entry[2]);
            Log.d("Load", entry[3]);

            long participant_id = Long.parseLong(entry[0]);
            int vist_id = Integer.parseInt(entry[1]);
            int load = Integer.parseInt(entry[3]);

            if(db.getParticipant(participant_id) == null) {
                db.createParticipant(new Participant(participant_id));
            }

           db.createViralLoad(new ViralLoad(participant_id, load, entry[2], vist_id));

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
            Log.d("External Dirs length", String.valueOf(externalDirs.length));
            Log.d("Path", externalDirs[0].getAbsolutePath());
            for(File f : externalDirs[0].listFiles()) {
                Log.d("File ", f.getName());
                if(f.getName().contains("memscap")) {
                    CSVFile csvFile = new CSVFile(f);
                    List<String[]> memsList = csvFile.read();
                    readMemsCapData(memsList);
                }
                else if (f.getName().contains("viralload")){
                    CSVFile csvFile = new CSVFile(f);
                    List<String[]> viralLoadList = csvFile.read();
                    readViralLoadData(viralLoadList);
                }
            }
        }
    }

    public void openLocalFiles() {
        CSVFile vl = new CSVFile(context.getResources().openRawResource(R.raw.viralload_test));
        CSVFile mc = new CSVFile(context.getResources().openRawResource(R.raw.memscap_test));
        readViralLoadData(vl.read());
        readMemsCapData(mc.read());

    }

}
