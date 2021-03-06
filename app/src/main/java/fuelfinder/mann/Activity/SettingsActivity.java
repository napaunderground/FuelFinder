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

    String Lat;
    String Lng;
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
        final Intent mIntent = new Intent(SettingsActivity.this, SelectFromDatabase.class);
        Lat = getIntent().getStringExtra("mLat");
        Lng = getIntent().getStringExtra("mLng");
        mIntent.putExtra("mLat", Lat);
        mIntent.putExtra("mLng", Lng);

        selectVehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(mIntent);
            }

        });

        moreInputsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setModel(vehicleInfo);

                counter = datasource.getAllVehicles().size();

                if (counter < 7){

                    datasource.createMileageModel(vehicleInfo.getEngine(), vehicleInfo.getMake(),
                            vehicleInfo.getUserMileage(), vehicleInfo.getModel(),
                            vehicleInfo.getCarName(), vehicleInfo.getYear(),
                            vehicleInfo.getTransmission(), counter);
                    counter++;
                    finish();
                } else {
                    startActivity(mIntent);
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


        /*
           private EditText txtDescription = (EditText) layout.findViewById(R.id.txtDescription)

    String string = txtDescription.getText().toString(); */
        String CarName = tCarName.getText().toString();
        String MakeString = tMake.getText().toString();
        String ModelString = tVehModel.getText().toString();
        String TransString = tTransmission.getText().toString();
        vehicleInfo.setCarName(CarName);
        vehicleInfo.setYear(year);
        vehicleInfo.setMake(MakeString);
        vehicleInfo.setModel(ModelString);
        vehicleInfo.setEngine(engine);
        vehicleInfo.setTransmission(TransString);
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
        finish();
        startActivity(new Intent(this, StartActivity.class));
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
