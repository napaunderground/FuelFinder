package fuelfinder.mann.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import fuelfinder.mann.Models.MileageModel;

/**
 * Created by Action Johnny on 4/7/2015.
 */
public class MileageModelDataSource{

    //database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.Engine,
            MySQLiteHelper.Make, MySQLiteHelper.Mileage,
            MySQLiteHelper.Model,
            MySQLiteHelper.VehicleID, MySQLiteHelper.VehName,
            MySQLiteHelper.Year, MySQLiteHelper.Transmission };

    public MileageModelDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public String getType(Uri input) {
        Uri uri = null;
        MileageModel temp = new MileageModel();
        temp.getMileageModel();
        String holder = temp.getYear() + temp.getMake() + temp.getModel()
                + temp.getEngine() + temp.getTransmission()
                + temp.getUserMileage() + temp.getCarName();

        return String.valueOf(holder);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createMileageModel(double Engine, String Make, double Mileage,
                                   String Model,
                                   int counter, String VehicleName, int Year,
                                   String Transmission, int VehicleID) {
        ContentValues values = new ContentValues();

/*
        ContentValues engValues = new ContentValues();
        engValues.put(MySQLiteHelper.Engine, Engine);
        ContentValues makeValues = new ContentValues();
        values.put(MySQLiteHelper.Make, Make);
        ContentValues mileageValues = new ContentValues();
        mileageValues.put(MySQLiteHelper.Mileage, Mileage);
        ContentValues modelValues = new ContentValues();
        modelValues.put(MySQLiteHelper.Model, Model);
        ContentValues yearValues = new ContentValues();
        yearValues.put(MySQLiteHelper.Year, Year);
        ContentValues nameValues = new ContentValues();
        nameValues.put(MySQLiteHelper.VehName, VehicleName);
        ContentValues xValues = new ContentValues();
        xValues.put(MySQLiteHelper.Transmission, Transmission);
*/
        // String make = Make
        values.put(MySQLiteHelper.Year, Year);
        values.put(MySQLiteHelper.Make, Make);
        values.put(MySQLiteHelper.Model, Model);
        values.put(MySQLiteHelper.Engine, Engine);
        values.put(MySQLiteHelper.Transmission, Transmission);
        values.put(MySQLiteHelper.VehName, VehicleName);
        values.put(MySQLiteHelper.Mileage, Mileage);
        open();
        database.insert("VehicleInfo", null, values);
        close();

        String test = "test";
//        Cursor cursor = databas e.query(MySQLiteHelper.NAME,
//                allColumns, MySQLiteHelper.VehicleID + " = " + insertId, null,
//                null, null, null);
//        cursor.moveToFirst();
//        MileageModel newMileageModel = cursorToMileageModel(cursor);
//        cursor.close();
//        return newMileageModel;

    }

    public void deleteMileageModel(MileageModel Model) {
        int id = Model.getVehicleID();
        System.out.println("Vehicle deleted with id: " + id);
        database.delete(MySQLiteHelper.NAME, MySQLiteHelper.VehicleID
                + " = " + id, null);

    }

    public List<MileageModel> getAllVehicles() {
        List<MileageModel> Models = new ArrayList<MileageModel>();

        Cursor cursor = database.query(MySQLiteHelper.NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MileageModel Model = cursorToMileageModel(cursor);
            Models.add(Model);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return Models;
    }

    private MileageModel cursorToMileageModel(Cursor cursor) {
        MileageModel Model = new MileageModel();
        Model.setVehicleID(cursor.getInt(cursor.getColumnIndex("VehicleID")));
        Model.setModel(cursor.getString(cursor.getColumnIndex("Model")));
        Model.setCarName(cursor.getString(cursor.getColumnIndex("VehName")));
        Model.setYear(cursor.getInt(cursor.getColumnIndex("Year")));
        Model.setMake(cursor.getString(cursor.getColumnIndex("Make")));
        Model.setUserMileage(cursor.getDouble(cursor.getColumnIndex("Mileage")));
        Model.setEngine(cursor.getDouble(cursor.getColumnIndex("Engine")));
        Model.setTransmission(cursor.getString(cursor.getColumnIndex("Transmission")));

        return Model;
    }

}