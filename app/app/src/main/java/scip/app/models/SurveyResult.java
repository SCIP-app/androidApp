package scip.app.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Allie on 6/25/2015.
 */
public class SurveyResult {
    long id;
    long participant_id;
    Date date;
    double temperature;
    boolean vaginaMucusSticky;
    boolean onPeriod;
    boolean isOvulating;
    boolean hadSex;
    boolean usedCondom;

    public SurveyResult(long participant_id, String date, double temperature, int vaginaMucusSticky, int onPeriod, int isOvulating, int hadSex, int usedCondom) {
        this.participant_id = participant_id;
        this.date = getDateFromString(date);
        this.temperature = temperature;
        this.onPeriod = booleanFromInt(onPeriod);
        this.vaginaMucusSticky = booleanFromInt(vaginaMucusSticky);
        this.isOvulating = booleanFromInt(isOvulating);
        this.hadSex = booleanFromInt(hadSex);
        this.usedCondom = booleanFromInt(usedCondom);
    }

    public SurveyResult(long participant_id, String date, double temperature, boolean vaginaMucusSticky, boolean onPeriod, boolean isOvulating, boolean hadSex, boolean usedCondom) {
        this.participant_id = participant_id;
        this.date = getDateFromString(date);
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
}
