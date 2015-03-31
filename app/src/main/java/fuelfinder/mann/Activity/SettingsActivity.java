package fuelfinder.mann.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import fuelfinder.mann.Models.MileageModel;
import fuelfinder.mann.R;


public class SettingsActivity extends Activity {

    private TextView carName;
    private TextView year;
    private TextView make;
    private TextView vehModel;
    private TextView mileage;
    private TextView engine;
    private TextView transmission;

    MileageModel vehicleInfo = new MileageModel();

    public static final String CarName = "CarName";
    public static final String MyVehicle = "MyVeh";
    public static final String YearOfMfr = "YearKey";
    public static final String Manufacturer = "MakeKey";
    public static final String VehModel = "ModelKey";
    public static final String FuelMileage = "MileageKey";
    public static final String EngineSize = "Engine SizeKey";
    public static final String Transmission = "TransmissionKey";

    SharedPreferences sharedprefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        carName = (TextView) findViewById(R.id.editTextCarName);
        year = (TextView) findViewById(R.id.editTextYearOfMfr);
        make = (TextView) findViewById(R.id.editTextManufacturer);
        vehModel = (TextView) findViewById(R.id.editTextVehModel);
        mileage = (TextView) findViewById(R.id.editTextFuelMileage);
        engine = (TextView) findViewById(R.id.editTextEngineSize);
        transmission = (TextView) findViewById(R.id.editTextTransmission);


        vehicleInfo.setCarName(carName.toString());
        vehicleInfo.setYear(year.toString());
        vehicleInfo.setMake(make.toString());
        vehicleInfo.setModel(vehModel.toString());
        vehicleInfo.setEngine(engine.toString());
        vehicleInfo.setTransmission(transmission.toString());
        vehicleInfo.setUserMileage(mileage.toString());

      //  SavedVehicleModel.SaveVehicle(vehicleInfo);
    }


    public void run(View v) {

        SharedPreferences prefs = this.getSharedPreferences("MyVeh", Context.MODE_PRIVATE);
        String miles = prefs.getString("FuelMileage", "0");
        int intMiles = Integer.parseInt(miles);

        if(intMiles != 0)
        {
            setContentView(R.layout.activity_start);

            startActivity(new Intent(this, fuelfinder.mann.Activity.MapsActivity.class));
        }
    }


    public void settings(View v) {
        setContentView(R.layout.activity_start);

        startActivity(new Intent(this, SettingsActivity.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }
}
