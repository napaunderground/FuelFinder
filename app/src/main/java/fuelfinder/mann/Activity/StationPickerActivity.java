package fuelfinder.mann.Activity;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import fuelfinder.mann.R;

public class StationPickerActivity extends Activity{

    private Button firstChoice;
    private Button secondChoice;
    private Button thirdChoice;
    private Button fourthChoice;
    GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_picker);

        firstChoice = (Button)findViewById(R.id.checkBox);
        secondChoice = (Button)findViewById(R.id.checkBox2);
        thirdChoice = (Button)findViewById(R.id.checkBox3);
        fourthChoice = (Button)findViewById(R.id.checkBox4);


       // CurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        final Intent mIntent = new Intent(this, MapsActivity.class);

        firstChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mIntent.putExtra("station", "0");
                finish();
                startActivity(mIntent);

            }
        });

        secondChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mIntent.putExtra("station", "1");
                finish();
                startActivity(mIntent);
            }
        });

        thirdChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mIntent.putExtra("station", "2");
                finish();
                startActivity(mIntent);
            }
        });

        fourthChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mIntent.putExtra("station", "3");
                finish();
                startActivity(mIntent);
            }
        });
    }


    /*protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }*/


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
