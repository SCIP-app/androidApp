package scip.app.models;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import scip.app.DataImportActivity;


/**
 * Created by Allie on 6/25/2015.
 * SurveyResult
 * Description: This class sets up survey result data for use elsewhere
 * Class variables:
 *  id
 *  Type: long
 *  Description: Unique identifier for db rows; this is NOT the same as the participant id.
 *
 * participant_id
 *  Type: long
 *  Description: Contains the participant id
 *
 * date
 *  Type: Date
 *  Description: Contains the date reformatted as dd/mm/yyyy
 *
 * temperature
 *  Type: double
 *  Description: Contains the temperature
 *
 * vaginaMucusSticky
 *  Type: boolean
 *  Description: Contains whether or not vaginal mucus is sticky that day
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
 * Functions:
 *
 * SurveyResult (long, String, double, int, int, int, int, int)
 *  Description: Constructor; Creates a SurveyResult object from given parameters
 *  Input parameters:
 *      long: participant's id number
 *      String: gets the date in String format
 *      double: gets the temperature as a double
 *      int: converts vaginaMucusSticky int into a boolean
 *      int: converts onPeriod int into a boolean
 *      int: converts isOvulating int into a boolean
 *      int: converts hadSex int into a boolean
 *      int: converts usedCondom int into a boolean
 *  Output parameters: Null
 *
 * booleanFromInt (int)
 *  Description: Converts integers to booleans
 *  Input parameters:
 *      int: takes integer toBool and sets it to a boolean value of true if == 1
 *   Output parameters: Null
 *
 * Getters & setters
 */
public class SurveyResult implements Comparable<SurveyResult>{

    long id;
    long participant_id;
    Date date;
    double temperature;
    boolean vaginaMucusSticky;
    boolean onPeriod;
    boolean isOvulating;
    boolean hadSex;
    boolean usedCondom;

    public SurveyResult (long participant_id, String date, double temperature, int vaginaMucusSticky, int onPeriod, int isOvulating, int hadSex, int usedCondom) {
        this.participant_id = participant_id;
        this.date = DateUtil.getDateFromString(date);
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

    public SurveyResult(long participant_id, String date, double temperature, boolean vaginaMucusSticky, boolean onPeriod, boolean isOvulating, boolean hadSex, boolean usedCondom) {
        this.participant_id = participant_id;
        this.date = DateUtil.getDateFromString(date);
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

    @Override
    public int compareTo(SurveyResult o) {
        return getDate().compareTo(o.getDate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SurveyResult)) return false;

        SurveyResult that = (SurveyResult) o;

        if (hadSex != that.hadSex) return false;
        if (isOvulating != that.isOvulating) return false;
        if (onPeriod != that.onPeriod) return false;
        if (participant_id != that.participant_id) return false;
        if (Double.compare(that.temperature, temperature) != 0) return false;
        if (usedCondom != that.usedCondom) return false;
        if (vaginaMucusSticky != that.vaginaMucusSticky) return false;
        if (!date.equals(that.date)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = date.hashCode();
        temp = Double.doubleToLongBits(temperature);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (vaginaMucusSticky ? 1 : 0);
        result = 31 * result + (onPeriod ? 1 : 0);
        result = 31 * result + (isOvulating ? 1 : 0);
        result = 31 * result + (hadSex ? 1 : 0);
        result = 31 * result + (usedCondom ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SurveyResult{" +
                "participant_id=" + participant_id +
                ", date=" + date +
                ", temperature=" + temperature +
                ", vaginaMucusSticky=" + vaginaMucusSticky +
                ", onPeriod=" + onPeriod +
                ", isOvulating=" + isOvulating +
                ", hadSex=" + hadSex +
                ", usedCondom=" + usedCondom +
                '}';
    }
}
