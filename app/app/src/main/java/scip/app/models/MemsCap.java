package scip.app.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kristin Dew on 6/30/2015.
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

}

