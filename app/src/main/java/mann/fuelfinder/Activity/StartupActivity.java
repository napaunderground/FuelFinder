package mann.fuelfinder.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import Model.MileageModel;
import mann.fuelfinder.R;

public class StartupActivity extends ActionBarActivity {

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
            startActivity(new Intent(this, mann.fuelfinder.Activity.SettingsActivity.class));
        }

        else
        {
            setContentView(R.layout.activity_start);

            startActivity(new Intent(this, mann.fuelfinder.Activity.MapsActivity.class));

        }

    }


}
