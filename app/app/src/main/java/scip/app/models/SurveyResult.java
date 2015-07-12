package scip.app.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Allie on 6/25/2015.
 * SurveyResult
 * Description: This class sets up survey result data (from mSurvey's API?) for use in the DatabaseHelper object
 * Class variables:
 *  id
 *  Type: long
 *  Description: Used by Database Helper to add rows; this is NOT the same as the participant id. Has getter & setter.
 *
 * participant_id
 *  Type: long
 *  Description: Contains the participant id.
 *
 * date
 *  Type: Date (private class)
 *  Description: Contains the date reformatted as dd/mm/yyyy.
 *
 * temperature
 *  Type: double
 *  Description: Contains the temperature pulled from mSurvey as a double
 *
 * vaginaMucusSticky
 *  Type: boolean
 *  Description: Contains whether or not vaginal mucus is sticky that day (from mSurvey)
 *
 * onPeriod
 *  Type: boolean
 *  Description: Contains whether or not participant is menstruating that day
 *
 * isOvulating
 *  Type: boolean
 *  Description: Contains whether OPK results were positive or not that day
 *
 * hadSex
 *  Type: boolean
 *  Description: Contains whether or not the participant had sex that day
 *
 * usedCondom
 *  Type: boolean
 *  Description: Contains whether or not the participant used a condom if they had sex (should I add something about how it depends on the previous value? Or it doesn't matter b/c it will just return null if not)
 *
 * Functions (public/private?):
 *
 * SurveyResult (long, String, double, int, int, int, int, int) - Two public classes?
 *  Description: Creates a SurveyResult object from given parameters
 *  Input parameters:
 *      long: participant's id number (from CSV? or was this a global variable?)
 *      String: gets the date in String format
 *      double: gets the temperature as a double
 *      int: converts vaginaMucusSticky int into a boolean
 *      int: converts onPeriod int into a boolean
 *      int: converts isOvulating int into a boolean
 *      int: converts hadSex int into a boolean
 *      int: converts usedCondom int into a boolean
 *  Output parameters: Are there any for this one?
 *
 * booleanFromInt (int)
 *  Description: Converts integers to booleans
 *  Input parameters:
 *      int: takes integer toBool and sets it to a boolean value of 1 (what about 0?)
 *
 * Date (String)
 *  Description: Turns the raw string date into a formatted date (dd/mm/yy).
 *  Input parameters:
 *      String: Takes the string date and turns it into a formatted date using object SimpleDateFormat.
 *
 */
public class SurveyResult {
    long id;
    long participant_id;
    Date date;
    double temperature;
    boolean vaginaMucusSticky;
    boolean onPeriod;
    boolean isOvulating;
    boolean hadSex;
    boolean usedCondom;

    public SurveyResult(long participant_id, String date, double temperature, int vaginaMucusSticky, int onPeriod, int isOvulating, int hadSex, int usedCondom) {
        this.participant_id = participant_id;
        this.date = getDateFromString(date);
        this.temperature = temperature;
        this.onPeriod = booleanFromInt(onPeriod);
        this.vaginaMucusSticky = booleanFromInt(vaginaMucusSticky);
        this.isOvulating = booleanFromInt(isOvulating);
        this.hadSex = booleanFromInt(hadSex);
        this.usedCondom = booleanFromInt(usedCondom);
    }

    public SurveyResult(long participant_id, Date date, double temperature, boolean vaginaMucusSticky, boolean onPeriod, boolean isOvulating, boolean hadSex, boolean usedCondom) {
        this.participant_id = participant_id;
        this.date = date;
        this.temperature = temperature;
        this.vaginaMucusSticky = vaginaMucusSticky;
        this.onPeriod = onPeriod;
        this.isOvulating = isOvulating;
        this.hadSex = hadSex;
        this.usedCondom = usedCondom;
    }

    private boolean booleanFromInt(int toBool) {
        return toBool==1;
    }

    private Date getDateFromString(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }


    public long getId() {
        return id;
    }

    public long getParticipant_id() {
        return participant_id;
    }

    public Date getDate() {
        return date;
    }

    public double getTemperature() {
        return temperature;
    }

    public boolean isVaginaMucusSticky() {
        return vaginaMucusSticky;
    }

    public boolean isOnPeriod() {
        return onPeriod;
    }

    public boolean isOvulating() {
        return isOvulating;
    }

    public boolean isHadSex() {
        return hadSex;
    }

    public boolean isUsedCondom() {
        return usedCondom;
    }

    public void setId(long id) {
        this.id = id;
    }
}
