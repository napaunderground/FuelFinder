package fuelfinder.mann.Activity;

import android.app.Activity;
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

    private Button selectVehicleButton;
    private Button moreInputsButton;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        setFields();

        datasource = new MileageModelDataSource(this);
        datasource.open();

        selectVehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(SettingsActivity.this, SelectFromDatabase.class));
            }

        });

        moreInputsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setModel(vehicleInfo);

                if (counter < 7){

                    datasource.createMileageModel(vehicleInfo.getEngine(), vehicleInfo.getMake(),
                            vehicleInfo.getUserMileage(), vehicleInfo.getModel(),
                            counter, vehicleInfo.getCarName(), vehicleInfo.getYear(),
                            vehicleInfo.getTransmission(), vehicleInfo.getVehicleID());
                    counter++;
                    finish();
                } else {
                    startActivity(new Intent(SettingsActivity.this, SelectFromDatabase.class));
                }

            }
        });
    }



    public void setModel(MileageModel currVeh)
    {
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

    public void setFields() {


        selectVehicleButton = (Button) findViewById(R.id.selectVehicle);
        moreInputsButton = (Button) findViewById(R.id.moreInputsButton);
        tCarName = (EditText) findViewById(R.id.editTextCarName);
        tYear = (EditText) findViewById(R.id.editTextYearOfMfr);
        tMake = (EditText) findViewById(R.id.editTextManufacturer);
        tVehModel = (EditText) findViewById(R.id.editTextVehModel);
        tMileage = (EditText) findViewById(R.id.editTextFuelMileage);
        tEngine = (EditText) findViewById(R.id.editTextEngineSize);
        tTransmission = (EditText) findViewById(R.id.editTextTransmission);
    }


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
