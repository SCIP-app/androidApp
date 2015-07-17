package scip.app.models;

import android.content.Context;

import java.util.List;

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
    List<PeakFertility> peakFertilities;

    // Constructors

    // this constructor only to be used for creating database entries
    public Participant(long participant_id) {
        this.participant_id = participant_id;
    }

    public Participant(Context context, long participant_id) {
        this.context =  context;
        this.participant_id = participant_id;
        loadData();
    }

    public Participant(Context context, long id, long participant_id) {
        this.context = context;
        this.id = id;
        this.participant_id = participant_id;
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

    public List<PeakFertility> getPeakFertilities() {
        if (isFemale)
            return peakFertilities;
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

    private void loadData() {
        DatabaseHelper db = new DatabaseHelper(context);
        if(isFemale) {
            this.peakFertilities = db.getAllPeakFertilityById(participant_id);
            this.surveyResults = db.getAllSurveyResultsById(participant_id);
        }
        if(isIndex()) {
            this.viralLoads = db.getAllViralLoadsById(participant_id);
        }
        else {
            this.memscaps = db.getAllMemsCapById(participant_id);
        }
        db.closeDB();
    }

}
