package fuelfinder.mann.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fuelfinder.mann.Models.MileageModel;
import fuelfinder.mann.R;
import fuelfinder.mann.Utility.MileageModelDataSource;


public class SettingsActivity extends Activity {

    private MileageModelDataSource datasource;
    private Button moreInputsButton;
    private Button pickTheBestButton;
    private Button pickFourButton;
    private TextView tCarName;
    private TextView tYear;
    private TextView tMake;
    private TextView tVehModel;
    private TextView tMileage;
    private TextView tEngine;
    private TextView tTransmission;

    public int counter = 0;

    MileageModel vehicleInfo = new MileageModel();

    /*
        public static final String CarName = "CarName";
        public static final String MyVehicle = "MyVeh";
        public static final String YearOfMfr = "YearKey";
        public static final String Manufacturer = "MakeKey";
        public static final String VehModel = "ModelKey";
        public static final String FuelMileage = "MileageKey";
        public static final String EngineSize = "Engine SizeKey";
        public static final String Transmission = "TransmissionKey";

        SharedPreferences sharedprefs;
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        datasource = new MileageModelDataSource(this);
        datasource.open();

        moreInputsButton = (Button) findViewById(R.id.moreInputsButton);
        pickTheBestButton = (Button) findViewById(R.id.pickTheBestButton);
        pickFourButton = (Button) findViewById(R.id.pickFourButton);
        tCarName = (EditText) findViewById(R.id.editTextCarName);
        tYear = (EditText) findViewById(R.id.editTextYearOfMfr);
        tMake = (EditText) findViewById(R.id.editTextManufacturer);
        tVehModel = (EditText) findViewById(R.id.editTextVehModel);
        tMileage = (EditText) findViewById(R.id.editTextFuelMileage);
        tEngine = (EditText) findViewById(R.id.editTextEngineSize);
        tTransmission = (EditText) findViewById(R.id.editTextTransmission);
/*
        sharedprefs = getSharedPreferences(MyVehicle, 0);

        if(sharedprefs.contains(CarName))
        {
            carName.setText(sharedprefs.getString(CarName, ""));
        }
        if(sharedprefs.contains(YearOfMfr))
        {
            year.setText(sharedprefs.getString(YearOfMfr, ""));
        }
        if(sharedprefs.contains(Manufacturer))
        {
            make.setText(sharedprefs.getString(Manufacturer, ""));
        }
        if(sharedprefs.contains(VehModel))
        {
            vehModel.setText(sharedprefs.getString(VehModel, ""));
        }
        if(sharedprefs.contains(FuelMileage))
        {
            mileage.setText(sharedprefs.getString(FuelMileage, ""));
        }
        if(sharedprefs.contains(EngineSize))
        {
            engine.setText(sharedprefs.getString(EngineSize, ""));
        }
        if(sharedprefs.contains(Transmission))
        {
            transmission.setText(sharedprefs.getString(Transmission, ""));
        }
*/

        moreInputsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setModel(vehicleInfo);

                double currMileage = vehicleInfo.getUserMileage();
                if (currMileage == 0) {
                    finish();
                    startActivity(new Intent(SettingsActivity.this, MapsActivity.class));
                } else if (counter < 8) {

                    Context context = null;

                    // TODO Show Nick this is where the error was.  The new declaration here of
                    // data was leaving "datasource" un-set aka null...was previously "data"

         //           MileageModelDataSource data = new MileageModelDataSource(context);
                    // was it really here ->
                        datasource.createMileageModel(vehicleInfo.getEngine(), vehicleInfo.getMake(),
                        vehicleInfo.getUserMileage(), vehicleInfo.getModel(),
                        counter, vehicleInfo.getCarName(), vehicleInfo.getYear(),
                        vehicleInfo.getTransmission(), vehicleInfo.getVehicleID());
                counter++;
      //          finish();
                startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                } else {
                    //goto pick vehicle screen yet to be implemented
                }
            }
        });

        pickTheBestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(SettingsActivity.this, PickCheapestActivity.class));
            }
        });


        pickFourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(SettingsActivity.this, StationPickerActivity.class));
                setContentView(R.layout.activity_station_picker);

            }
        });
        //   }
        //   public void run(View v) {
/*
        String ca = carName.getText().toString();
        String y = year.getText().toString();
        String ma = make.getText().toString();
        String mo = vehModel.getText().toString();
        String m = mileage.getText().toString();
        String e = engine.getText().toString();
        String t = transmission.getText().toString();
        SharedPreferences.Editor editor = sharedprefs.edit();

        vehicleInfo.setCarName(ca);
        vehicleInfo.setYear(y);
        vehicleInfo.setMake(ma);
        vehicleInfo.setModel(mo);
        vehicleInfo.setEngine(e);
        vehicleInfo.setTransmission(t);
     //   vehicleInfo.setUserMileage(m);

        editor.putString("CarName", ca);
        editor.putString("YearOfMfr", y);
        editor.putString("Manufacturer", ma);
        editor.putString("VehModel", mo);
        editor.putString("FuelMileage", m);
        editor.putString("EngineSize", e);
        editor.putString("Transmission", t);

        editor.commit();

        SharedPreferences prefs = this.getSharedPreferences("MyVeh", Context.MODE_PRIVATE);


        String miles = prefs.getString("FuelMileage", "0");
        int intMiles = Integer.parseInt(miles);
*/


    }
    public void setModel(MileageModel currVeh)
    {
        // should this be decomposed more????


        MileageModel vehicleInfo = currVeh;

        int year;
        double engine;
        double mileage;

        try {
            year = Integer.parseInt(tYear.getText().toString());
        } catch (final NumberFormatException e) {
            year = 0;
        }
        try {
            engine = Double.parseDouble(tEngine.getText().toString());
        } catch (final NumberFormatException e) {
            engine = 0;
        }
        try {
            mileage = Double.parseDouble(tMileage.getText().toString());
        } catch  (final NumberFormatException e) {
            mileage = 0;
        }

        vehicleInfo.setCarName(String.valueOf(tCarName));
        vehicleInfo.setYear(year);
        vehicleInfo.setMake(String.valueOf(tMake));
        vehicleInfo.setModel(String.valueOf(tVehModel));
        vehicleInfo.setEngine(engine);
        vehicleInfo.setTransmission(String.valueOf(tTransmission));
        vehicleInfo.setUserMileage(mileage);
    }



    /*

        public void settings(View v) {

            startActivity(new Intent(this, SettingsActivity.class));
        }
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        this.finish();
        startActivity(new Intent(this, fuelfinder.mann.Activity.MapsActivity.class));
        return;
    }


    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

}

