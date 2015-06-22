package scip.app.models;

/**
 * Created by Allie on 6/22/2015.
 *
 * This is the model for the Couple data structure.
 */
public class Couple {
    long id;
    long couple_id;

    // eventually will contain information about peak fertility

    // Constructors
    public Couple() {
    }

    public Couple(long couple_id) {
        this.couple_id = couple_id;
    }

    public Couple(long id, long couple_id) {
        this.id = id;
        this.couple_id = couple_id;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCoupleId() {
        return couple_id;
    }
}
