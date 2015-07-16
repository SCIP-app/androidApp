package scip.app.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Allie on 6/29/2015.
 * PeakFertility
 * Description: This class sets up fertility data for use elsewhere
 * Class variables:
 *  id
 *  Type: long
 *  Description: Unique identifier for db rows; this is NOT the same as the participant id
 *
 * participant_id
 *  Type: long
 *  Description: Contains the participant id
 *
 * start
 *  Type: Date (private class)
 *  Description: Contains the menses start date
 *
 * end
 *  Type: date
 *  Description: Contains the menses end date
 *
 * Functions:
 *
 * PeakFertility (long, Date, Date)
 *  Description: Constructor; creates a PeakFertility object from given parameters
 *  Input parameters:
 *      long: participant's id number (from CSV? or was this a global variable?)
 *      Date: start of peak fertility period as calculated by algorithm
 *      Date: end of peak fertility period as calculated by algorithm
 *  Output parameters: Null
 *
 * Getters & setters for id (long), participant id (long), start & end (Date).
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
