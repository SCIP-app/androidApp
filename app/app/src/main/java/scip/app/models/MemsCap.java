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

