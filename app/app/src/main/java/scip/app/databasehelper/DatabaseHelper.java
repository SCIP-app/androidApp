package scip.app.databasehelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
}
