package scip.app.models;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import scip.app.databasehelper.DatabaseHelper;

/**
 * Created by Allie on 6/22/2015.
 *
 * This is the model for the Participant data structure.
 */
public class Participant {
    long id;
    long participant_id;
    Context context;
    boolean isFemale;
    List<SurveyResult> surveyResults;
    List<ViralLoad> viralLoads;
    List<MemsCap> memscaps;
    PeakFertility peakFertility;

    // Constructors

    // this constructor only to be used for creating database entries
    public Participant(long participant_id, boolean isFemale) {
        this.participant_id = participant_id;
        this.isFemale = isFemale;
    }

    public Participant(Context context, long participant_id) {
        this.context =  context;
        this.participant_id = participant_id;
        loadData();
    }

    public Participant(Context context, long id, long participant_id, int isFemale) {
        this.context = context;
        this.id = id;
        this.participant_id = participant_id;
        if(isFemale == 1) {
            this.isFemale = true;
            Log.d("PC", "Is Female");
        }
        else
            this.isFemale = false;
        loadData();
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getParticipantId() {
        return participant_id;
    }

    public long getCoupleId() {
        return participant_id/100;
    }

    public List<SurveyResult> getSurveyResults() {
        if(isFemale)
            return surveyResults;
        return null;
    }

    public List<ViralLoad> getViralLoads() {
        if(isIndex())
            return viralLoads;
        return null;
    }

    public PeakFertility getPeakFertility() {
        if (isFemale)
            return peakFertility;
        return null;
    }

    public List<MemsCap> getMemscaps() {
        if (!isIndex())
            return memscaps;
        return null;
    }

    public boolean isIndex() {
        long tmp = (participant_id/10) % 2;
        if(tmp == 0)
            return true;
        return false;
    }

    public boolean isFemale() {
        return isFemale;
    }

    public Participant getPartner() {
        DatabaseHelper db = new DatabaseHelper(context);
        List<Participant> couple = db.getCoupleFromID(getCoupleId());
        db.closeDB();
        for(Participant p : couple) {
            if(p.getParticipantId() != participant_id)
                return p;
        }
        return null;
    }

    public void reCalculateFertilityData() {
        if(isFemale) {

        }
    }

    private void loadData() {
        DatabaseHelper db = new DatabaseHelper(context);
        if(isFemale) {
            this.surveyResults = db.getAllSurveyResultsById(participant_id);
            this.peakFertility = new PeakFertility(surveyResults);
        }
        if(isIndex()) {
            this.viralLoads = db.getAllViralLoadsById(participant_id);
        }
        else {
            this.memscaps = db.getAllMemsCapById(participant_id);
        }
        db.closeDB();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participant)) return false;

        Participant that = (Participant) o;

        if (isFemale != that.isFemale) return false;
        if (participant_id != that.participant_id) return false;


        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (participant_id ^ (participant_id >>> 32));
        result = 31 * result + (isFemale ? 1 : 0);

        return result;
    }
}