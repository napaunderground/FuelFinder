package fuelfinder.mann.Utility;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fuelfinder.mann.Models.MileageModel;

/**
 * Created by Action Johnny on 4/2/2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = "DBAdapter";

    public static final String NAME = "VehicleInfo";
    public static final String VehicleID = "VehicleID"; // keyID
    public static final String Model = "Model";
    public static final String VehName = "VehName";     // keyName
    public static final String Year = "Year";
    public static final String Make = "Make";
    public static final String Mileage = "Mileage";
    public static final String Engine = "Engine";
    public static final String Transmission = "Transmission";
    private static final String TABLE_LABELS = "";
    public static final String VEHICLE_TABLE = "tbl_veh";
    public static final String[] ALL_TABLES = {VEHICLE_TABLE};

    private static final String DATABASE_NAME = "VehicleInfo.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE = "create table "
            + NAME + "(" + VehicleID
            + " integer primary key AUTOINCREMENT unique, " + Model
            + " text not null, " + VehName +
            " text not null unique, " + Year +
            " integer not null, " + Make +
            " text not null, " + Mileage +
            " real not null, " + Engine +
            " double not null, " + Transmission + " text not null);";

    public MySQLiteHelper(Context context) {            //  DatabaseHandler
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + NAME);
        onCreate(db);
    }

    public static String getDATABASE_NAME() {
        return DATABASE_NAME;
    }

    public void insertLabel(MileageModel label){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VehName, label.getCarName());
        values.put(Mileage, label.getUserMileage());
        values.put(Transmission, "any");
        values.put(Engine, 2);
        values.put(Make, "Ford");
        values.put(Model, "Mustang");
        values.put(Year, 2015);


        // Inserting Row
        db.insert(TABLE_LABELS, null, values);
        db.close(); // Closing database connection
    }
    public List<String> getAllLabels(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }



    public int getAllCount() {
        int count = 0;

        // Select All Query
        String selectQuery = "SELECT  * FROM " + NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                count++;
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return count;
    }
}