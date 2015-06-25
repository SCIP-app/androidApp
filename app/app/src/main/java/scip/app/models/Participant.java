package scip.app.models;

/**
 * Created by Allie on 6/22/2015.
 *
 * This is the model for the Participant data structure.
 */
public class Participant {
    long id;
    long participant_id;

    // eventually will contain information about peak fertility

    // Constructors
    public Participant() {
    }

    public Participant(long participant_id) {
        this.participant_id = participant_id;
    }

    public Participant(long id, long participant_id) {
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
}
