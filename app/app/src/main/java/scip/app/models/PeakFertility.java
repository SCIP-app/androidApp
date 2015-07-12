package scip.app.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Allie on 6/29/2015.
 * PeakFertility
 * Description: This class sets up fertility data (from mSurvey's API?) for use in the DatabaseHelper object
 * Class variables:
 *  id
 *  Type: long
 *  Description: Used by Database Helper to add rows; this is NOT the same as the participant id. Has getter & setter.
 *
 * participant_id
 *  Type: long
 *  Description: Contains the participant id.
 *
 * start
 *  Type: Date (private class)
 *  Description: Contains the date reformatted as dd/mm/yyyy
 *
 * end
 *  Type: date
 *  Description: Contains the date
 *
 * Functions (public/private?):
 *
 * PeakFertility (long, Date, Date) - Two public  classes?
 *  Description: Creates a PeakFertility object from given parameters
 *  Input parameters:
 *      long: participant's id number (from CSV? or was this a global variable?)
 *      Date: start of peak fertility period as calculated by algorithm
 *      Date: end of peak fertility period as calculated by algorithm
 *  Output parameters: Are there any for this one?
 *
 * Date (String)
 *  Description: Turns the raw string date into a formatted date (dd/mm/yy).
 *  Input parameters:
 *      String: Takes the string date and turns it into a formatted date using object SimpleDateFormat.
 **/

public class PeakFertility {
    long id;
    long participant_id;
    Date start;
    Date end;

    public PeakFertility(long participant_id, Date start, Date end) {
        this.participant_id = participant_id;
        this.start = start;
        this.end = end;
    }

    public PeakFertility(long participant_id, String start, String end) {
        this.participant_id = participant_id;
        this.start = getDateFromString(start);
        this.end = getDateFromString(end);
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

    public void setId(long id) {
        this.id = id;
    }

    public long getParticipant_id() {
        return participant_id;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

}
