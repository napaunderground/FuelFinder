package fuelfinder.mann.Activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;

import fuelfinder.mann.R;


public class StartActivity extends ActionBarActivity implements
        LocationListener {
    private int EndCond = 0;
    private static final long POLLING_FREQ = 1000 * 30;
    private static final long FASTEST_UPDATE_FREQ = 1000 * 5;
    private LocationRequest mLocationRequest;
    private LocationManager locationManager;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(POLLING_FREQ);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_FREQ);



        super.onCreate(savedInstanceState);

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            onLocationChanged(location);
        } else {
        }



    }


    /////////////////////////////////////////////////////////////////--------------/////////////////////////////////////////////////////////


    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        int lat = (int) (location.getLatitude());
        int lng = (int) (location.getLongitude());

        String savedMileage;
        int intMileage;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        savedMileage = prefs.getString("FuelMileage", "0");
        intMileage = Integer.parseInt(savedMileage);

        if(intMileage == 0 && EndCond != 0)
        {


            String MyLat = Double.toString(location.getLatitude());
            String MyLong = Double.toString(location.getLongitude());
            final Intent mIntent = new Intent(this, SettingsActivity.class);
            mIntent.putExtra("mLat", MyLat);
            mIntent.putExtra("mLng", MyLong);

            startActivity(mIntent);
        }

        else if (intMileage != 0)
        {
            setContentView(R.layout.activity_start);
            finish();
            startActivity(new Intent(this, fuelfinder.mann.Activity.MapsActivity.class));

        }
        else
            EndCond++;
    }


    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }


    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }


    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

}