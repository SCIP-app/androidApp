package scip.app.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Allie on 6/25/2015.
 * ViralLoad
 * Description: This class sets up viral load data for use elsewhere
 * Class variables:
 *  id
 *  Type: long
 *  Description: Unique identifier for db rows; this is NOT the same as the participant id.
 *
 * participant_id
 *  Type: long
 *  Description: Contains the participant id
 *
 * number
 *  Type: int
 *  Description: Contains the viral load measurement
 *
 * date
 *  Type: date
 *  Description: Contains the date
 *
 * visit_id
 *  Type: int
 *  Description: Contains the visit id
 *
 * Functions:
 *
 * ViralLoad (long, int, String, int)
 *  Description: Constructor; Creates a ViralLoad object from given parameters
 *  Input parameters:
 *      long: participant's id number
 *      int: sets viral load measurement number
 *      String: sets the date
 *      int: sets the visit id
 *  Output parameters: Null
 *
 * Getters & setters.
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
        this.date = DateUtil.getDateFromString(date);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViralLoad)) return false;

        ViralLoad viralLoad = (ViralLoad) o;

        if (number != viralLoad.number) return false;
        if (participant_id != viralLoad.participant_id) return false;
        if (visit_id != viralLoad.visit_id) return false;
        if (!date.equals(viralLoad.date)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (participant_id ^ (participant_id >>> 32));
        result = 31 * result + number;
        result = 31 * result + date.hashCode();
        result = 31 * result + visit_id;
        return result;
    }
}
