package scip.app.models;

import android.content.Context;

import java.util.List;

import scip.app.databasehelper.DatabaseHelper;

/**
 * Created by Allie on 6/22/2015.
 * Participant
 * Description: This class sets up a participant object that contains all of a single participant's data
 * Class variables:
 *  id
 *  Type: long
 *  Description: Unique identifier for db rows; this is NOT the same as the participant id.
 *
 * participant_id
 *  Type: long
 *  Description: Contains the participant id.
 *
 * Context
 *  Type: Context
 *  Description: Contains data about the state of the app.
 *
 * isFemale
 *  Type: boolean
 *  Description: Contains whether or not the participant is female.
 *
 * surveyResults
 *  Type: ArrayList
 *  Description: Contains the participant's survey results as a list
 *
 * viralLoads
 *  Type: ArrayList
 *  Description: Contains the participant's viral loads as a list
 *
 * memscaps
 *  Type: ArrayList
 *  Description: Contains the participant's memscap data as a list
 *
 * peakFertilities
 *  Type: ArrayList
 *  Description: Contains the participant's peak fertility data as a list if available
 *
 * Functions:
 *
 * Participant (Context, long, long)
 *  Description: Constructor; creates a Participant object from given parameters
 *  Input parameters:
 *      Context: sets app state
 *      long: sets id
 *      long: sets participant id
 *  Output parameters: Null
 *
 * getPartner ()
 *  Description: Gets a couple based on the couple id; if no partner found, returns Participant p
 *  Input parameters: Null
 *  Output parameters: Null
 *
 * loadData ()
 *  Description: Pulls up all the data for a participant according to HIV status and whether or not isFemale
 *  Input parameters: Null
 *  Output parameters: Null
 *
 *  Getters & setters
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
    public Participant() {
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

    public boolean isIndex() {
        long tmp = (participant_id/10) % 2;
        if(tmp == 0)
            return true;
        return false;
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
