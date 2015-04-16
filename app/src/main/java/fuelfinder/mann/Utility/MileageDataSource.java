package fuelfinder.mann.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;
import java.util.List;

import fuelfinder.mann.Models.MileageDBModel;

// Data Access Object

public class MileageDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_MILEAGE };

    public MileageDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }


    public void close() {
        dbHelper.close();
    }

    public MileageDBModel createMileage(String mileage) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_MILEAGE, mileage);
        long insertId = database.insert(MySQLiteHelper.TABLE_NAME,
                null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId,
                null, null, null, null);
        cursor.moveToFirst();
        MileageDBModel newMileage = cursorToMileage(cursor);
        cursor.close();
        return newMileage;
    }

    public void deleteMileage(MileageDBModel mileage) {
        long id = mileage.getId();
        System.out.println("mileage Deleted with ID: " + id);
        database.delete(MySQLiteHelper.TABLE_NAME, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<MileageDBModel> getAllComments() {
        List<MileageDBModel> comments = new ArrayList<MileageDBModel>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            MileageDBModel comment = cursorToMileage(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }

        cursor.close();
        return comments;
    }

    private MileageDBModel cursorToMileage(Cursor cursor) {
        MileageDBModel comment = new MileageDBModel();
        comment.setId(cursor.getLong(0));
        comment.setMileage(cursor.getString(1));
        return comment;
    }
}