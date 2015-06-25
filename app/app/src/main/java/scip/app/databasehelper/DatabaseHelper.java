package scip.app.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import scip.app.models.Participant;

/**
 * Created by Allie on 6/22/2015.
 *
 * This class is designed to make performing database operations easier. It will contain methods for all direct database access actions.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String LOG = "DatabaseHelper";  // Logcat tag
    private static final int DATABASE_VERSION = 3;  // This number MUST be incremented whenever a database is created/destroyed or columns are created/removed
    private static final String DATABASE_NAME = "patientManager";

    // Table Names
    private static final String TABLE_PARTICIPANTS = "participants";
    private static final String TABLE_VIRAL_LOADS = "viralLoads";
    private static final String TABLE_SURVEY_RESULTS = "surveyResults";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_PARTICIPANT_ID = "participant_id";
    private static final String KEY_DATE = "date";

    // Viral Load specific columns
    private static final String KEY_NUMBER = "number";
    private static final String KEY_VISIT_ID = "visit_id";

    // Survey Result specific columns
    private static final String KEY_TEMPERATURE = "temperature";
    private static final String KEY_VAGINA_MUCUS_STICKY = "vaginaMucusSticky";
    private static final String KEY_HAS_PERIOD = "hasPeriod";
    private static final String KEY_IS_OVULATING = "isOvulating";
    private static final String KEY_HAD_SEX = "hadSex";
    private static final String KEY_USED_CONDOM = "usedCondom";

    // Create table statements
    private static final String CREATE_TABLE_PARTICIPANTS = "CREATE TABLE "
            + TABLE_PARTICIPANTS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PARTICIPANT_ID + " INTEGER" + ")";
    private static final String CREATE_TABLE_VIRAL_LOADS = "CREATE TABLE " + TABLE_VIRAL_LOADS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PARTICIPANT_ID + " INTEGER," + KEY_DATE + " TEXT," + KEY_NUMBER + " INTEGER," + KEY_VISIT_ID + " INTEGER)";
    private static final String CREATE_TABLE_SURVEY_RESULTS = "CREATE TABLE " + TABLE_SURVEY_RESULTS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PARTICIPANT_ID + " INTEGER," + KEY_DATE + " TEXT," + KEY_TEMPERATURE + " INTEGER," + KEY_VAGINA_MUCUS_STICKY + " INTEGER,"
            + KEY_HAS_PERIOD + " INTEGER," + KEY_IS_OVULATING + " INTEGER," + KEY_HAD_SEX + " INTEGER," + KEY_USED_CONDOM + " INTEGER)";

    // Constructors
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Global Methods
    //Each of these must be modified when tables are created/destroyed
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PARTICIPANTS);
        db.execSQL(CREATE_TABLE_VIRAL_LOADS);
        db.execSQL(CREATE_TABLE_SURVEY_RESULTS);
    }

    @Override
    // For now, when database version changes, just delete everything and start over. That's going to have to chang for production.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Kill all existing tables/data
        db.execSQL("DROP TABLE IF EXISTS " + "couples");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPANTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIRAL_LOADS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SURVEY_RESULTS);

        // Create new tables
        onCreate(db);
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    // Participant-specific CRUD Methods

    public boolean createParticipant(Participant participant) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PARTICIPANT_ID, participant.getParticipantId());

        // insert row
        long id = db.insert(TABLE_PARTICIPANTS, null, values);
        closeDB();

        if(id != -1) {
            participant.setId(id);
            return true ;
        }
        else {
            // There was an error in creating the row
            return false;
        }

    }

    public Participant getParticipant(long participant_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PARTICIPANTS + " WHERE "
                + KEY_PARTICIPANT_ID + " = " + participant_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        long id = c.getInt(c.getColumnIndex(KEY_ID));
        Participant participant = new Participant(id, participant_id);

        return participant;
    }

    public List<Participant> getAllParticipants() {
        List<Participant> participants = new ArrayList<Participant>();
        String selectQuery = "SELECT  * FROM " + TABLE_PARTICIPANTS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                long id = c.getInt((c.getColumnIndex(KEY_ID)));
                long participant_id = c.getInt((c.getColumnIndex(KEY_PARTICIPANT_ID)));
                Participant participant = new Participant(id, participant_id);

                // adding to participant list
                participants.add(participant);
            } while (c.moveToNext());
        }

        return participants;
    }


}
