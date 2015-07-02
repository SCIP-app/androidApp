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

    // Constructors
    public Participant() {
    }

    public Participant(Context context, long participant_id) {
        this.context =  context;
        this.participant_id = participant_id;
    }

    public Participant(Context context, long id, long participant_id) {
        this.id = id;
        this.participant_id = participant_id;
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

    public Participant getPartner() {
        DatabaseHelper db = new DatabaseHelper(context);
        List<Participant> couple = db.getCoupleFromID(getCoupleId());
        for(Participant p : couple) {
            if(p.getParticipantId() != participant_id)
                return p;
        }
        return null;
    }
}
