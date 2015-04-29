package fuelfinder.mann.Activity;

/**
 * Created by nathan on 4/28/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import fuelfinder.mann.R;

/*
// TODO ANSWER
Nathan: when you created the new activity called from Settings, you need to pass whats inside
Settings' bundle to the new activity. The mLat and mLng values stored in Settings were not being put
into SelectFromDatabase, preventing StationPicker from having a saved lat/lng. I have fixed the problem.
 */

public class SelectFromDatabase extends Activity implements AdapterView.OnItemSelectedListener {


    String myLat;
    String myLng;

    private Button moreInputsButton;
    private Button pickTheBestButton;
    private Button pickFourButton;

//TODO make the spinner actually pull from the database...
    // answer: look how I do it in StationPickerActivity. It's
    // all done with the datasource variable. Very easy!

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_from_database);


        myLat = getIntent().getStringExtra("mLat");
        myLng = getIntent().getStringExtra("mLng");

        setFields();
        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);


        // Spinner Drop down elements
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("Automobile");
        categories.add("Business Services");
        categories.add("Computers");
        categories.add("Education");
        categories.add("Personal");
        categories.add("Travel");

        // Creating adapter for spinner
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(adapter);


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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }


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
}


