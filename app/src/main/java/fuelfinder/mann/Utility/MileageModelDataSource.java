package fuelfinder.mann.Utility;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import fuelfinder.mann.Models.MileageModel;

/**
 * Created by Action Johnny on 4/7/2015.
 */
public class MileageModelDataSource {

    //database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.Engine,
            MySQLiteHelper.Make, MySQLiteHelper.Mileage,
            MySQLiteHelper.Model, MySQLiteHelper.NAME,
            MySQLiteHelper.VehicleID, MySQLiteHelper.VehName,
            MySQLiteHelper.Year, MySQLiteHelper.Transmission
    };

    public MileageModelDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public MileageModel createMileageModel(int Engine, String Make, int Mileage, String Model, String Name, int VehicleID, String VehicleName, int Year, String Transmission) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.VehicleID, VehicleID);
        long insertId = database.insert(MySQLiteHelper.NAME, null,
                values);
        ContentValues engValues = new ContentValues();
        engValues.put(MySQLiteHelper.Engine, Engine);
        long insertEngine = database.insert(MySQLiteHelper.Engine, null, engValues );

        ContentValues makeValues = new ContentValues();
        makeValues.put(MySQLiteHelper.Make, Make);
        long insertMake = database.insert(MySQLiteHelper.Make, null, makeValues );

        ContentValues mileageValues = new ContentValues();
        mileageValues.put(MySQLiteHelper.Mileage, Mileage);
        long insertMileage = database.insert(MySQLiteHelper.Mileage, null, mileageValues );

        ContentValues modelValues = new ContentValues();
        modelValues.put(MySQLiteHelper.Model, Model);
        long insertModel = database.insert(MySQLiteHelper.Model, null, modelValues );

        ContentValues yearValues = new ContentValues();
        yearValues.put(MySQLiteHelper.Year, Year);
        long insertYear = database.insert(MySQLiteHelper.Year, null, yearValues );

        ContentValues nameValues = new ContentValues();
        nameValues.put(MySQLiteHelper.VehName, VehicleName);
        long insertName = database.insert(MySQLiteHelper.VehName, null, nameValues );

        ContentValues xValues = new ContentValues();
        xValues.put(MySQLiteHelper.Transmission, Transmission);
        long insertTransmission = database.insert(MySQLiteHelper.Transmission, null, xValues );

        Cursor cursor = database.query(MySQLiteHelper.NAME,
                allColumns, MySQLiteHelper.VehicleID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        MileageModel newMileageModel = cursorToMileageModel(cursor);
        cursor.close();
        return newMileageModel;
    }

    public void deleteMileageModel(MileageModel Model) {
        long id = Model.getVehicleID();
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
        Model.setVehicleID(cursor.getInt(0));
        Model.setModel(cursor.getString(1));
        Model.setCarName(cursor.getString(2));
        Model.setYear(cursor.getString(3));
        Model.setMake(cursor.getString(4));
        Model.setUserMileage(cursor.getDouble(5));
        Model.setEngine(cursor.getString(6));
        Model.setTransmission(cursor.getString(7));


        return Model;
    }
}
