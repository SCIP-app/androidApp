package scip.app.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import scip.app.models.DateUtil;
import scip.app.models.MemsCap;
import scip.app.models.Participant;
import scip.app.models.PeakFertility;
import scip.app.models.SurveyResult;
import scip.app.models.ViralLoad;

/**
 * Created by Allie on 6/22/2015.
 *
 * This class is designed to make performing database operations easier. It will contain methods for all direct database access actions.
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    Context context;

    private static final String LOG = "DatabaseHelper";  // Logcat tag
    private static final int DATABASE_VERSION = 9;  // This number MUST be incremented whenever a database is created/destroyed or columns are created/removed
    private static final String DATABASE_NAME = "patientManager";

    // Table Names
    private static final String TABLE_PARTICIPANTS = "participants";
    private static final String TABLE_VIRAL_LOADS = "viralLoads";
    private static final String TABLE_SURVEY_RESULTS = "surveyResults";
    private static final String TABLE_MEMS_CAP = "memsCap";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_PARTICIPANT_ID = "participant_id";
    private static final String KEY_DATE = "date";

    // Participant specific columns
    private static final String KEY_IS_FEMALE = "isFemale";

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

    // MEMSCap specific column
    private static final String KEY_MEMS_ID = "memsDates";

    // Create table statements
    private static final String CREATE_TABLE_PARTICIPANTS = "CREATE TABLE "
            + TABLE_PARTICIPANTS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PARTICIPANT_ID + " INTEGER," + KEY_IS_FEMALE + " INTEGER" + ")";
    private static final String CREATE_TABLE_VIRAL_LOADS = "CREATE TABLE " + TABLE_VIRAL_LOADS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PARTICIPANT_ID + " INTEGER," + KEY_DATE + " TEXT," + KEY_NUMBER + " INTEGER," + KEY_VISIT_ID + " INTEGER)";
    private static final String CREATE_TABLE_SURVEY_RESULTS = "CREATE TABLE " + TABLE_SURVEY_RESULTS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PARTICIPANT_ID + " INTEGER," + KEY_DATE + " TEXT," + KEY_TEMPERATURE + " REAL," + KEY_VAGINA_MUCUS_STICKY + " INTEGER,"
            + KEY_HAS_PERIOD + " INTEGER," + KEY_IS_OVULATING + " INTEGER," + KEY_HAD_SEX + " INTEGER," + KEY_USED_CONDOM + " INTEGER)";
    private static final String CREATE_TABLE_MEMS_CAP = "CREATE TABLE " + TABLE_MEMS_CAP + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PARTICIPANT_ID + " INTEGER," + KEY_MEMS_ID + " INTEGER," + KEY_DATE + " TEXT)";

    // Constructors
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    //Global Methods
    //Each of these must be modified when tables are created/destroyed
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PARTICIPANTS);
        db.execSQL(CREATE_TABLE_VIRAL_LOADS);
        db.execSQL(CREATE_TABLE_SURVEY_RESULTS);
        db.execSQL(CREATE_TABLE_MEMS_CAP);
    }

    @Override
    // For now, when database version changes, just delete everything and start over. That's going to have to change for production.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Kill all existing tables/data
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPANTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIRAL_LOADS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SURVEY_RESULTS);
        db.execSQL("DROP TABLE IF EXISTS " + "peakFertility");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMS_CAP);

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
        values.put(KEY_IS_FEMALE, intFromBoolean(participant.isFemale()));

        // insert row
        long id = db.insert(TABLE_PARTICIPANTS, null, values);

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

        if (c == null || !c.moveToFirst())
            return null;

        long id = c.getInt(c.getColumnIndex(KEY_ID));
        int isFemale = c.getInt(c.getColumnIndex(KEY_IS_FEMALE));
        Participant participant = new Participant(context, id, participant_id, isFemale);

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
                int isFemale = c.getInt(c.getColumnIndex(KEY_IS_FEMALE));
                Participant participant = new Participant(context, id, participant_id, isFemale);

                // adding to participant list
                participants.add(participant);
            } while (c.moveToNext());
        }

        return participants;
    }

    public List<Long> getAllCoupleIDs() {
        List<Long> couples = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_PARTICIPANTS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                long id = c.getInt((c.getColumnIndex(KEY_ID)));
                long participant_id = c.getInt((c.getColumnIndex(KEY_PARTICIPANT_ID)));
                int isFemale = c.getInt(c.getColumnIndex(KEY_IS_FEMALE));
                //Participant participant = new Participant(context, id, participant_id, isFemale);

                // adding to couple list
                couples.add(participant_id/100);
            } while (c.moveToNext());
        }

        couples = new ArrayList<Long>(new LinkedHashSet<Long>(couples));
        return couples;
    }

    public List<Participant> getCoupleFromID(Long coupleId) {
        List<Participant> couple = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_PARTICIPANTS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                long id = c.getInt((c.getColumnIndex(KEY_ID)));
                long participant_id = c.getInt((c.getColumnIndex(KEY_PARTICIPANT_ID)));
                int isFemale = c.getInt(c.getColumnIndex(KEY_IS_FEMALE));
                Participant participant = new Participant(context, id, participant_id, isFemale);

                if(coupleId == participant.getCoupleId()) {
                    couple.add(participant);
                }

            } while (c.moveToNext());
        }

        return couple;
    }

    // ViralLoad-specific CRUD Methods

    public boolean createViralLoad(ViralLoad viralLoad) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PARTICIPANT_ID, viralLoad.getParticipant_id());
        values.put(KEY_NUMBER, viralLoad.getNumber());
        values.put(KEY_DATE, DateUtil.getStringFromDate(viralLoad.getDate()));
        values.put(KEY_VISIT_ID, viralLoad.getVisit_id());

        // insert row
        long id = db.insert(TABLE_VIRAL_LOADS, null, values);

        if(id != -1) {
            viralLoad.setId(id);
            return true ;
        }
        else {
            // There was an error in creating the row
            return false;
        }

    }

    public List<ViralLoad> getAllViralLoadsById(long participant_id) {
        List<ViralLoad> viralLoads = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_VIRAL_LOADS + " WHERE "
                + KEY_PARTICIPANT_ID + " = " + participant_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                long id = c.getInt((c.getColumnIndex(KEY_ID)));
                String date = c.getString((c.getColumnIndex(KEY_DATE)));
                int number = c.getInt((c.getColumnIndex(KEY_NUMBER)));
                int visit_id = c.getInt((c.getColumnIndex(KEY_VISIT_ID)));
                ViralLoad vl = new ViralLoad(participant_id, number, date, visit_id);
                vl.setId(id);

                // adding to participant list
                viralLoads.add(vl);
            } while (c.moveToNext());
        }

        return viralLoads;
    }

    // Survey Result-specific CRUD Methods

    public boolean createSurveyResult(SurveyResult surveyResult) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PARTICIPANT_ID, surveyResult.getParticipant_id());
        values.put(KEY_DATE, DateUtil.getStringFromDate(surveyResult.getDate()));
        values.put(KEY_TEMPERATURE, surveyResult.getTemperature());
        values.put(KEY_VAGINA_MUCUS_STICKY, intFromBoolean(surveyResult.isVaginaMucusSticky()));
        values.put(KEY_HAS_PERIOD, intFromBoolean(surveyResult.isOnPeriod()));
        values.put(KEY_IS_OVULATING, intFromBoolean(surveyResult.isOvulating()));
        values.put(KEY_HAD_SEX, intFromBoolean(surveyResult.isHadSex()));
        values.put(KEY_USED_CONDOM, intFromBoolean(surveyResult.isUsedCondom()));

        // insert row
        long id = db.insert(TABLE_SURVEY_RESULTS, null, values);

        if(id != -1) {
            surveyResult.setId(id);
            return true ;
        }
        else {
            // There was an error in creating the row
            return false;
        }

    }

    public List<SurveyResult> getAllSurveyResultsById(long participant_id) {
        List<SurveyResult> surveyResults = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_SURVEY_RESULTS + " WHERE "
                + KEY_PARTICIPANT_ID + " = " + participant_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                long id = c.getInt((c.getColumnIndex(KEY_ID)));
                double temp = c.getDouble((c.getColumnIndex(KEY_TEMPERATURE)));
                String date = c.getString((c.getColumnIndex(KEY_DATE)));
                int vms = c.getInt((c.getColumnIndex(KEY_VAGINA_MUCUS_STICKY)));
                int hp = c.getInt((c.getColumnIndex(KEY_HAS_PERIOD)));
                int io = c.getInt((c.getColumnIndex(KEY_IS_OVULATING)));
                int hs = c.getInt((c.getColumnIndex(KEY_HAD_SEX)));
                int uc = c.getInt((c.getColumnIndex(KEY_USED_CONDOM)));
                SurveyResult sr = new SurveyResult(participant_id, date, temp, vms, hp, io, hs, uc);
                sr.setId(id);

                // adding to participant list
                surveyResults.add(sr);
            } while (c.moveToNext());
        }

        return surveyResults;
    }
    public List<SurveyResult> getAllSurveyResults() {
        List<SurveyResult> surveyResults = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_SURVEY_RESULTS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                long id = c.getInt((c.getColumnIndex(KEY_ID)));
                long participant_id = c.getInt(c.getColumnIndex(KEY_PARTICIPANT_ID));
                double temp = c.getDouble((c.getColumnIndex(KEY_TEMPERATURE)));
                String date = c.getString((c.getColumnIndex(KEY_DATE)));
                int vms = c.getInt((c.getColumnIndex(KEY_VAGINA_MUCUS_STICKY)));
                int hp = c.getInt((c.getColumnIndex(KEY_HAS_PERIOD)));
                int io = c.getInt((c.getColumnIndex(KEY_IS_OVULATING)));
                int hs = c.getInt((c.getColumnIndex(KEY_HAD_SEX)));
                int uc = c.getInt((c.getColumnIndex(KEY_USED_CONDOM)));
                SurveyResult sr = new SurveyResult(participant_id, date, temp, vms, hp, io, hs, uc);
                sr.setId(id);

                // adding to participant list
                surveyResults.add(sr);
            } while (c.moveToNext());
        }

        return surveyResults;
    }
    // MEMSCap-specific CRUD Methods

    public boolean createMemsCap(MemsCap memsCap) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PARTICIPANT_ID, memsCap.getParticipant_id());
        values.put(KEY_DATE, DateUtil.getStringFromDate(memsCap.getDate()));
        values.put(KEY_MEMS_ID, memsCap.getMems_id());

        // insert row
        long id = db.insert(TABLE_MEMS_CAP, null, values);

        if(id != -1) {
            memsCap.setId(id);
            return true ;
        }
        else {
            // There was an error in creating the row
            return false;
        }

    }

    public List<MemsCap> getAllMemsCapById(long participant_id) {
        List<MemsCap> prepAdherence = new ArrayList<MemsCap>();
        String selectQuery = "SELECT  * FROM " + TABLE_MEMS_CAP + " WHERE "
                + KEY_PARTICIPANT_ID + " = " + participant_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                long id = c.getInt((c.getColumnIndex(KEY_ID)));
                String date = c.getString((c.getColumnIndex(KEY_DATE)));
                long mems = c.getInt((c.getColumnIndex(KEY_MEMS_ID)));
                MemsCap mc = new MemsCap(participant_id, date, mems);
                mc.setId(id);

                // adding to participant list
                prepAdherence.add(mc);
            } while (c.moveToNext());
        }

        return prepAdherence;
    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPANTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIRAL_LOADS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SURVEY_RESULTS);
        db.execSQL("DROP TABLE IF EXISTS " + "peakFertility");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMS_CAP);

        db.execSQL(CREATE_TABLE_PARTICIPANTS);
        db.execSQL(CREATE_TABLE_VIRAL_LOADS);
        db.execSQL(CREATE_TABLE_SURVEY_RESULTS);
        db.execSQL(CREATE_TABLE_MEMS_CAP);

    }

    // Utility Methods
    private int intFromBoolean(boolean test) {
        if(test)
            return 1;
        return 0;
    }
}
