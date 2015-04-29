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

import java.util.List;

import fuelfinder.mann.Models.MileageModel;
import fuelfinder.mann.R;
import fuelfinder.mann.Utility.MileageModelDataSource;
import fuelfinder.mann.Utility.MySQLiteHelper;


public class SettingsActivity extends Activity {

    String myLat;
    String myLng;
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
    private MileageModelDataSource datasource;
    MileageModel vehicleInfo = new MileageModel();
    public int counter = 0;
    MySQLiteHelper Helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        myLat = getIntent().getStringExtra("mLat");
        myLng = getIntent().getStringExtra("mLng");

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
        //Helper = new MySQLiteHelper(this);


        datasource = new MileageModelDataSource(this);
        datasource.open();



        moreInputsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setModel(vehicleInfo);

                double currMileage = vehicleInfo.getUserMileage();
                if(currMileage == 0)
                {
                    finish();
                    startActivity(new Intent(SettingsActivity.this, MapsActivity.class));
                }
                else if (counter < 8){

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
        final Intent mIntent = new Intent(this, MapsActivity.class);

        pickTheBestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mIntent.putExtra("station", "0");

                finish();
                startActivity(mIntent);
            }
        });

        final Intent StationIntent = new Intent(this, StationPickerActivity.class);

        pickFourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StationIntent.putExtra("mLat", myLat);
                StationIntent.putExtra("mLng", myLng);
                finish();
                startActivity(StationIntent);
                setContentView(R.layout.activity_station_picker);

            }
        });


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
