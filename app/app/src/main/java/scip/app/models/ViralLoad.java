package scip.app.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Allie on 6/25/2015.
 * ViralLoad
 * Description: This class sets up viral load data (from mSurvey's API?) for use in the DatabaseHelper object
 * Class variables:
 *  id
 *  Type: long
 *  Description: Used by Database Helper to add rows; this is NOT the same as the participant id. Has getter & setter.
 *
 * participant_id
 *  Type: long
 *  Description: Contains the participant id.
 *
 * number
 *  Type: int
 *  Description: Contains the viral load measurement.
 *
 * date
 *  Type: date
 *  Description: Contains the date (should I add sthg about its reformatting?)
 *
 * visit_id
 *  Type: int
 *  Description: Contains the visit id.
 *
 * Functions (public/private?):
 *
 * ViralLoad (long, int, String, int) - Two public classes?
 *  Description: Creates a ViralLoad object from given parameters
 *  Input parameters:
 *      long: participant's id number (from CSV? or was this a global variable?)
 *      int: sets viral load measurement number
 *      String: sets the date
 *      int: sets the visit id
 *  Output parameters: Are there any for this one?
 *
 * Date (String)
 *  Description: Turns the raw string date into a formatted date (dd/mm/yy).
 *  Input parameters:
 *      String: Takes the string date and turns it into a formatted date using object SimpleDateFormat.
 *
 */

public class ViralLoad {
    long id;
    long participant_id;
    int number;
    Date date;
    int visit_id;

    public ViralLoad(long participant_id, int number, String date, int visit_id) {
        this.participant_id = participant_id;
        this.number = number;
        this.date = getDateFromString(date);
        this.visit_id = visit_id;

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

    public int getNumber() {
        return number;
    }

    public Date getDate() {
        return date;
    }

    public int getVisit_id() {
        return visit_id;
    }

    private Date getDateFromString(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
}
