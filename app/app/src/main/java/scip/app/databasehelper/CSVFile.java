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
    InputStream inputStream;
    boolean useLocal = false;

    public CSVFile(File inputFile){
        this.inputFile = inputFile;
    }

    public CSVFile(InputStream inputStream){
        this.inputStream = inputStream;
        this.useLocal = true;
    }

    public List read(){
        List resultList = new ArrayList();
        BufferedReader reader;
        try {
            if(useLocal) {
                reader = new BufferedReader(new InputStreamReader(inputStream));
            }
            else {

                reader = new BufferedReader(new FileReader(inputFile));
            }
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
