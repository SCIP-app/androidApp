package scip.app.databasehelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allie on 7/6/2015.
 */
public class CSVFile {
    File inputFile;

    public CSVFile(File inputFile){
        this.inputFile = inputFile;
    }

    public List read(){
        List resultList = new ArrayList();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String csvLine;
            reader.readLine(); // read the first line and throw it out because it's the description
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(";");
                resultList.add(row);
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Error in reading CSV file: "+e);
        }
        return resultList;
    }
}
