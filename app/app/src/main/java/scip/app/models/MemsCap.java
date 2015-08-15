package scip.app.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kristin Dew on 6/30/2015.
 * MemsCap: This class sets up data parsed from a CSV for use elsewhere
 * Class variables:
 * id
 *  Type: long
 *  Description: Unique identifier for db rows; this is NOT the same as the participant id. Has getter & setter.
 *
 * participant_id
 *  Type: long
 *  Description: Contains the participant id.
 *
 * date
 *  Type: date
 *  Description: Contains the date
 *
 * mems_id
 *  Type: long
 *  Description: Contains the id of each PrEP participant's Memscap
 *
 * Functions:
 *
 * MemsCap (long, Date, long): Constructor that creates a MemsCap object from given parameters
 *  Input parameters:
 *      long: participant's id number
 *      date: date of Memscap data
 *      long: mems_id as parsed
 *  Output parameters: Null
 *
 * Getters & setters.
 */
public class MemsCap {
    long id;
    long participant_id;
    Date date;
    long mems_id;

    public MemsCap(long participant_id, Date date, long mems_id) {
        this.participant_id = participant_id;
        this.date = date;
        this.mems_id = mems_id;
    }

    public MemsCap(long participant_id, String date, long mems_id) {
        this.participant_id = participant_id;
        this.date = DateUtil.getDateFromString(date);
        this.mems_id = mems_id;
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

    public Date getDate() {
        return date;
    }

    public long getMems_id() {
        return mems_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemsCap)) return false;

        MemsCap memsCap = (MemsCap) o;

        if (mems_id != memsCap.mems_id) return false;
        if (participant_id != memsCap.participant_id) return false;
        if (!date.equals(memsCap.date)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + (int) (mems_id ^ (mems_id >>> 32));
        return result;
    }
}

