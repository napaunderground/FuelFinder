package fuelfinder.mann.Activity;

/**
 * Created by nathan on 4/28/15.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import fuelfinder.mann.Models.MileageModel;
import fuelfinder.mann.R;
import fuelfinder.mann.Utility.MySQLiteHelper;


public class SelectFromDatabase extends Activity implements AdapterView.OnItemSelectedListener {

//comment to test
    // Spinner element
    Spinner spinner;

    // Add button
    Button btnAdd;

    // Input text
    EditText inputLabel;

    String myLat;
    String myLng;

    private Button moreInputsButton;
    Button pickTheBestButton;
    Button pickFourButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_from_database);


        myLat = getIntent().getStringExtra("mLat");
        myLng = getIntent().getStringExtra("mLng");

        setFields();
        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);

        // add button
        btnAdd = (Button) findViewById(R.id.btn_add);

        // new label input field
        inputLabel = (EditText) findViewById(R.id.input_label);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Loading spinner data from database
        loadSpinnerData();

        /**
         * Add new label button click listener
         * */
        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                MileageModel label = new MileageModel();

                if (label.getUserMileage() > 0) {
                    // database handler
                    MySQLiteHelper db = new MySQLiteHelper(
                            getApplicationContext());

                    // inserting new label into database
                    db.insertLabel(label);

                    // making input filed text to blank
                    inputLabel.setHint("Enter vehicle name here");

                    // Hiding the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputLabel.getWindowToken(), 0);

                    // loading spinner with newly added data
                    loadSpinnerData();
                } else {
                    Toast.makeText(getApplicationContext(), "Vehicle entered into database " + label,
                            Toast.LENGTH_LONG).show();
                }

            }
        });


        final Intent StationIntent = new Intent(this, StationPickerActivity.class);
        final Intent mIntent = new Intent(this, MapsActivity.class);


        pickTheBestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent.putExtra("station", "0");

                SelectFromDatabase.this.finish();
                SelectFromDatabase.this.startActivity(mIntent);
            }
        });



        pickTheBestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mIntent.putExtra("station", "0");

                finish();
                startActivity(mIntent);
            }
        });


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
/*
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }
*/







    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setFields() {
        moreInputsButton = (Button) findViewById(R.id.moreInputsButton);
        pickTheBestButton = (Button) findViewById(R.id.pickTheBestButton);
        pickFourButton = (Button) findViewById(R.id.pickFourButton);
        myLat = getIntent().getStringExtra("mLat");
        myLng = getIntent().getStringExtra("mLng");
    }



    @Override
    public void onBackPressed() {
        this.finish();
        return;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadSpinnerData() {
        // database handler
        MySQLiteHelper db = new MySQLiteHelper(getApplicationContext());

        // Spinner Drop down elements
        List<String> labels = db.getAllLabels();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "You selected: " + label,
                Toast.LENGTH_LONG).show();

    }


}


