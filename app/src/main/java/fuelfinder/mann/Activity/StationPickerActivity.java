package fuelfinder.mann.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import fuelfinder.mann.R;

public class StationPickerActivity extends Activity {

    private Button firstChoice;
    private Button secondChoice;
    private Button thirdChoice;
    private Button fourthChoice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_picker);

        firstChoice = (Button)findViewById(R.id.checkBox);
        secondChoice = (Button)findViewById(R.id.checkBox2);
        thirdChoice = (Button)findViewById(R.id.checkBox3);
        fourthChoice = (Button)findViewById(R.id.checkBox4);

        firstChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(StationPickerActivity.this, PickCheapestActivity.class));
            }
        });

        secondChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(StationPickerActivity.this, PickCheapestActivity.class));
            }
        });

        thirdChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(StationPickerActivity.this, PickCheapestActivity.class));
            }
        });

        fourthChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(StationPickerActivity.this, PickCheapestActivity.class));
            }
        });
    }




// need to figure out how to set the destination based on the station
// number picked in the prior step


    public void station1(View v)
    {
        setContentView(R.layout.activity_start);

        startActivity(new Intent(this, MapsActivity.class));
    }
    public void station2(View v)
    {
        setContentView(R.layout.activity_start);

        startActivity(new Intent(this, MapsActivity.class));
    }
    public void station3(View v)
    {
        setContentView(R.layout.activity_start);

        startActivity(new Intent(this, MapsActivity.class));
    }
    public void station4(View v)
    {
        setContentView(R.layout.activity_start);

        startActivity(new Intent(this, MapsActivity.class));
    }

}
