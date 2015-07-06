package scip.app.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Allie on 6/29/2015.
 */
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
