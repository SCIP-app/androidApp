package scip.app.models;

/**
 * Created by Allie on 6/22/2015.
 *
 * This is the model for the Couple data structure.
 */
public class Couple {
    int id;
    // eventually will contain information about peak fertility

    // Constructors
    public Couple() {
    }

    public Couple(int id) {
        this.id = id;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
