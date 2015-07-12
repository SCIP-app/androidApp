package scip.app.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kristin Dew on 6/30/2015.
 * MemsCap
 * Description: This class sets up data (parsed from a CSV?) for use in the DatabaseHelper object
 * Class variables:
 * id
 *  Type: long
 *  Description: Used by Database Helper to add rows; this is NOT the same as the participant id. Has getter & setter.
 *
 * participant_id
 *  Type: long
 *  Description: Contains the participant id.
 *
 * date
 *  Type: date
 *  Description: Contains the date
 *
 * mems_id
 *  Type: long
 *  Description: Contains the id of each PrEP participant's Memscap
 *
 * Functions (public/private?):
 *
 * MemsCap (long, Date, long) - Why are there two public MemsCap classes again? Is it related to the date?
 *  Description: Creates a MemsCap object from given parameters
 *  Input parameters:
 *      long: participant's id number (from CSV? or was this a global variable?)
 *      date: date of Memscap data from CSV
 *      long: mems_id as parsed from CSV
 *  Output parameters: Are there any for this one?
 *
 * Date (String)
 *  Description: Turns the raw string date into a formatted date (dd/mm/yy).
 *  Input parameters:
 *      String: Takes the string date and turns it into a formatted date using object SimpleDateFormat.
 */
public class MemsCap {
        long id;
        long participant_id;
        Date date;
        long mems_id;

        public MemsCap(long participant_id, Date date, long mems_id) {
            this.participant_id = participant_id;
            this.date = date;
            this.mems_id = mems_id;
        }

        public MemsCap(long participant_id, String date, long mems_id) {
            this.participant_id = participant_id;
            this.date = getDateFromString(date);
            this.mems_id = mems_id;
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

        public Date getDate() {
            return date;
        }

        public long getMems_id() {
            return mems_id;
        }

    }

