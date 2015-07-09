package scip.app.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Allie on 6/25/2015.
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
