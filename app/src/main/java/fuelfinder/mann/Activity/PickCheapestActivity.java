package fuelfinder.mann.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class PickCheapestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    // some data storage type declaration for Dijkstra's....array, etc
        for (int i = 0; i < 8; i++) {
            // something like inputStation = getClosestStation(currLocation)
            // returns Dijkstra?

        }
        startActivity(new Intent(PickCheapestActivity.this, MapsActivity.class));

        //   setContentView(R.layout.activity_pick_cheapest);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        startActivity(new Intent(this, fuelfinder.mann.Activity.SettingsActivity.class));
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
