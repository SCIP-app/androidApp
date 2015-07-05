package scip.app.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kristin Dew on 6/30/2015.
 */
public class MemsCap {
        long id;
        long participant_id;
        Date date;
        Date mems;

        public MemsCap(long participant_id, Date date, Date mems) {
            this.participant_id = participant_id;
            this.date = date;
            this.mems = mems;
        }

        public MemsCap(long participant_id, String date, String mems) {
            this.participant_id = participant_id;
            this.date = getDateFromString(date);
            this.mems = getDateFromString(mems);
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

        public Date getMems() {
            return mems;
        }

    }

