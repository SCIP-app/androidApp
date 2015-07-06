package scip.app.databasehelper;

import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.util.List;

import scip.app.R;

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

        for(String[] entry : memsList) {
            Log.d("Participant Id", entry[0]);
            Log.d("Mems Id", entry[1]);
            Log.d("Date", entry[2]);
            Log.d("Time", entry[3]);
        }
    }

}
