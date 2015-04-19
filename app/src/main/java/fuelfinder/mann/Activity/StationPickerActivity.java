package fuelfinder.mann.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fuelfinder.mann.R;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import fuelfinder.mann.Models.FuelPriceModel;
import fuelfinder.mann.Parser.FuelSourceParserV2;
import fuelfinder.mann.R;
import fuelfinder.mann.Utility.GasStationHandler;
import fuelfinder.mann.Utility.MileageModelDataSource;

public class StationPickerActivity extends Activity {

    private MileageModelDataSource datasource;
    String myLat;
    String myLng;

    String Choice1Cost;
    String Choice2Cost;
    String Choice3Cost;
    String Choice4Cost;

    private Button firstChoice;
    private Button secondChoice;
    private Button thirdChoice;
    private Button fourthChoice;

    private TextView priceView1;
    private TextView priceView2;
    private TextView priceView3;
    private TextView priceView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_picker);
        datasource = new MileageModelDataSource(this);
        datasource.open();

        firstChoice = (Button)findViewById(R.id.checkBox);
        secondChoice = (Button)findViewById(R.id.checkBox2);
        thirdChoice = (Button)findViewById(R.id.checkBox3);
        fourthChoice = (Button)findViewById(R.id.checkBox4);

        Location mCurrentLocation = new Location("Here!");
        myLat = getIntent().getExtras().getString("mLat");
        myLng = getIntent().getExtras().getString("mLng");
        double mLatD = Double.parseDouble(myLat);
        double mLngD = Double.parseDouble(myLng);
        mCurrentLocation.setLatitude(mLatD);
        mCurrentLocation.setLongitude(mLngD);



        FuelSourceParserV2 FSP = new FuelSourceParserV2();
        ArrayList<FuelPriceModel> FPLoc = new ArrayList();
        try{
            FPLoc = FSP.JSONtoModel(mCurrentLocation);
        }
        catch(JSONException e){

        }
        GasStationHandler Handle = new GasStationHandler();
        ArrayList<FuelPriceModel> bestStations = new ArrayList<>();
        bestStations = Handle.getBestStations(FPLoc, 10);

        Choice1Cost = Double.toString(bestStations.get(0).pricePerGallon);
        Choice2Cost = Double.toString(bestStations.get(1).pricePerGallon);
        Choice3Cost = Double.toString(bestStations.get(2).pricePerGallon);
        Choice4Cost = Double.toString(bestStations.get(3).pricePerGallon);

        priceView1 = (TextView) findViewById(R.id.textView10);
        priceView1.setText(Choice1Cost);

        priceView2 = (TextView) findViewById(R.id.textView12);
        priceView2.setText(Choice2Cost);

        priceView3 = (TextView) findViewById(R.id.textView11);
        priceView3.setText(Choice3Cost);

        priceView4 = (TextView) findViewById(R.id.textView14);
        priceView4.setText(Choice4Cost);



        //Resources res = getResources();
        //String Price1Text = String.format(res.getString(R.string.Price1), Choice1Cost);

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


    //////////////////////////////////////////////


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
