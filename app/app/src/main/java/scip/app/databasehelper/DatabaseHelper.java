package scip.app.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import scip.app.models.Couple;

/**
 * Created by Allie on 6/22/2015.
 *
 * This class is designed to make performing database operations easier. It will contain methods for all direct database access actions.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String LOG = "DatabaseHelper";  // Logcat tag
    private static final int DATABASE_VERSION = 1;  // This number MUST be incremented whenever a database is created/destroyed or columns are created/removed
    private static final String DATABASE_NAME = "patientManager";

    // Table Names
    private static final String TABLE_COUPLES = "couples";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_COUPLE_ID = "couple_id";

    // Create table statements
    private static final String CREATE_TABLE_COUPLES = "CREATE TABLE "
            + TABLE_COUPLES + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_COUPLE_ID + " INTEGER" + ")";

    // Constructors
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Global Methods
    //Each of these must be modified when tables are created/destroyed
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COUPLES);

    }

    @Override
    // For now, when database version changes, just delete everything and start over. That's going to have to chang for production.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Kill all existing tables/data
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUPLES);

        // Create new tables
        onCreate(db);
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    // Couple-specific CRUD Methods

    public boolean createCouple(Couple couple) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COUPLE_ID, couple.getCoupleId());

        // insert row
        long id = db.insert(TABLE_COUPLES, null, values);
        closeDB();

        if(id != -1) {
            couple.setId(id);
            return true ;
        }
        else {
            // There was an error in creating the row
            return false;
        }

    }

    public Couple getCouple(long couple_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_COUPLES + " WHERE "
                + KEY_COUPLE_ID + " = " + couple_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        long id = c.getInt(c.getColumnIndex(KEY_ID));
        Couple couple = new Couple(id, couple_id);

        closeDB();
        return couple;
    }

    public List<Couple> getAllCouples() {
        List<Couple> couples = new ArrayList<Couple>();
        String selectQuery = "SELECT  * FROM " + TABLE_COUPLES;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                long id = c.getInt((c.getColumnIndex(KEY_ID)));
                long couple_id = c.getInt((c.getColumnIndex(KEY_COUPLE_ID)));
                Couple couple = new Couple(id, couple_id);

                // adding to couple list
                couples.add(couple);
            } while (c.moveToNext());
        }

        closeDB();
        return couples;
    }


}
