package fuelfinder.mann.Activity;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;

import fuelfinder.mann.R;

public class StartActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String savedMileage;
        int intMileage;

        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        savedMileage = prefs.getString("FuelMileage", "0");
        intMileage = Integer.parseInt(savedMileage);

        if(intMileage == 0)

        {
            startActivity(new Intent(this, fuelfinder.mann.Activity.SettingsActivity.class));
        }

        else
        {
            setContentView(R.layout.activity_start);

            startActivity(new Intent(this, fuelfinder.mann.Activity.MapsActivity.class));

        }

    }


}
