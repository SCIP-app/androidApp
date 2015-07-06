package scip.app.databasehelper;

import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.util.List;

import scip.app.R;
import scip.app.models.MemsCap;
import scip.app.models.Participant;

/**
 * Created by Allie on 7/6/2015.
 */
public class CSVImporter {
    Context context;

    public CSVImporter(Context context) {
        this.context = context;
    }

    public void readMemsCapData(InputStream input) {
        CSVFile csvFile = new CSVFile(input);
        List<String[]> memsList = csvFile.read();

        DatabaseHelper db = new DatabaseHelper(context);
        for(String[] entry : memsList) {
//            Log.d("Participant Id", entry[0]);
//            Log.d("Mems Id", entry[1]);
//            Log.d("Date", entry[2]);
//            Log.d("Time", entry[3]);

            long participant_id = Long.parseLong(entry[0]);
            long mems_id = Long.parseLong(entry[1]);

            if(db.getParticipant(participant_id) == null) {
                db.createParticipant(new Participant(participant_id));
            }

            db.createMemsCap(new MemsCap(participant_id, entry[2], mems_id));
        }

        db.closeDB();
    }

}
