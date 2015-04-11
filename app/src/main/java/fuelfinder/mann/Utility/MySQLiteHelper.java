package fuelfinder.mann.Utility;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * Created by Action Johnny on 4/2/2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String NAME = "VehicleInfo";
    public static final String VehicleID = "VehicleID";
    public static final String Model = "Model";
    public static final String VehName = "VehName";
    public static final String Year = "Year";
    public static final String Make = "Make";
    public static final String Mileage = "Mileage";
    public static final String Engine = "Engine";
    public static final String Transmission = "Transmission";

    private static final String DATABASE_NAME = "VehicleInfo.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE = "create table "
            + NAME + "(" + VehicleID
            + " integer primary key autoincrement, " + Model
            + " text not null, " +VehName+
            " text not null, " + Year +
            " integer not null, " + Make +
            " text not null, " + Mileage +
            " integer not null, " + Engine +
            " float not null, " + Transmission +" text not null);";

    public MySQLiteHelper(Context context) {
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
}
