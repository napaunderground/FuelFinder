package fuelfinder.mann.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fuelfinder.mann.Models.StationInfoModel;
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
import fuelfinder.mann.Service.CostCalculator;
import fuelfinder.mann.Utility.GasStationHandler;
import fuelfinder.mann.Utility.MileageModelDataSource;

public class StationPickerActivity extends Activity {

    private MileageModelDataSource datasource;
    String myLat = "";
    String myLng = "";

    String Choice1Cost;
    String Choice2Cost;
    String Choice3Cost;
    String Choice4Cost;

    String TotalCost1;
    String TotalCost2;
    String TotalCost3;
    String TotalCost4;

    View mapView;

    private Button firstChoice;
    private Button secondChoice;
    private Button thirdChoice;
    private Button fourthChoice;

    private TextView priceView1;
    private TextView priceView2;
    private TextView priceView3;
    private TextView priceView4;

    private TextView TotalCostView1;
    private TextView TotalCostView2;
    private TextView TotalCostView3;
    private TextView TotalCostView4;

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
        if (getIntent().getExtras().getString("mLat") == null) {
            return;
        }
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
        ArrayList<StationInfoModel> bestStations = new ArrayList<>();
        double MileageValue = datasource.getAllVehicles().get(0).getUserMileage();
        bestStations = Handle.getBestStations(FPLoc, MileageValue, mCurrentLocation);

        Choice1Cost ="$" +  Double.toString(bestStations.get(0).FPM.pricePerGallon);
        Choice2Cost ="$" +  Double.toString(bestStations.get(1).FPM.pricePerGallon);
        Choice3Cost ="$" +  Double.toString(bestStations.get(2).FPM.pricePerGallon);
        Choice4Cost ="$" +  Double.toString(bestStations.get(3).FPM.pricePerGallon);

        priceView1 = (TextView) findViewById(R.id.textView10);
        priceView1.setText(Choice1Cost);

        priceView2 = (TextView) findViewById(R.id.textView12);
        priceView2.setText(Choice2Cost);

        priceView3 = (TextView) findViewById(R.id.textView11);
        priceView3.setText(Choice3Cost);

        priceView4 = (TextView) findViewById(R.id.textView14);
        priceView4.setText(Choice4Cost);

        CostCalculator C = new CostCalculator();

        double TC1 = C.findCost(MileageValue, (bestStations.get(0).distance), bestStations.get(0).FPM.pricePerGallon);//+ bestStations.get(0).FPM.pricePerGallon;
        double TC2 = C.findCost(MileageValue, (bestStations.get(1).distance), bestStations.get(1).FPM.pricePerGallon);//+ bestStations.get(1).FPM.pricePerGallon;
        double TC3 = C.findCost(MileageValue, (bestStations.get(2).distance), bestStations.get(2).FPM.pricePerGallon);// + bestStations.get(2).FPM.pricePerGallon;
        double TC4 = C.findCost(MileageValue, (bestStations.get(3).distance), bestStations.get(3).FPM.pricePerGallon);// + bestStations.get(3).FPM.pricePerGallon;


        TC1 = Math.round(TC1*100);
        TC1=TC1/100;
        TC2 = Math.round(TC2*100);
        TC2=TC2/100;
        TC3 = Math.round(TC3*100);
        TC3=TC3/100;
        TC4 = Math.round(TC4*100);
        TC4=TC4/100;



        TotalCost1 ="$" +  Double.toString(TC1);
        TotalCost2 ="$" +  Double.toString(TC2);
        TotalCost3 ="$" +  Double.toString(TC3);
        TotalCost4 ="$" + Double.toString(TC4);

        TotalCostView1 = (TextView) findViewById(R.id.textView17);
        TotalCostView1.setText(TotalCost1);

        TotalCostView2 = (TextView) findViewById(R.id.textView13);
        TotalCostView2.setText(TotalCost2);

        TotalCostView3 = (TextView) findViewById(R.id.textView16);
        TotalCostView3.setText(TotalCost3);

        TotalCostView4 = (TextView) findViewById(R.id.textView15);
        TotalCostView4.setText(TotalCost4);

        firstChoice = (Button) findViewById(R.id.checkBox);
        firstChoice.setText(bestStations.get(0).FPM.stationID+Html.fromHtml("<br /><small>Best Total Cost</small>"));

        secondChoice = (Button) findViewById(R.id.checkBox2);
        secondChoice.setText(bestStations.get(1).FPM.stationID+Html.fromHtml("<br /><small>2nd Best Total Cost</small>"));


        thirdChoice = (Button) findViewById(R.id.checkBox3);
        thirdChoice.setText(bestStations.get(2).FPM.stationID+Html.fromHtml("<br /><small>3rd Best Total Cost</small>"));

        fourthChoice = (Button) findViewById(R.id.checkBox4);
        fourthChoice.setText(bestStations.get(3).FPM.stationID+ Html.fromHtml("<br /><small>4th Best Total Cost</small>"));

        mapView = (View) findViewById(R.id.mapView);
        //mapView.

        final Intent mIntent = new Intent(this, MapsActivity.class);

        firstChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mIntent.putExtra("station", "0");
                //finish();
                startActivity(mIntent);

            }
        });

        secondChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mIntent.putExtra("station", "1");
                //finish();
                startActivity(mIntent);
            }
        });

        thirdChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mIntent.putExtra("station", "2");
               // finish();
                startActivity(mIntent);
            }
        });

        fourthChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mIntent.putExtra("station", "3");
                //finish();
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

    public double StringToDouble(String s1) {
            /* Parses a string and returns the number in it, returned as a double! */
        String ParsedString = "";
        int DotCount = 0;
        char[] str = s1.toCharArray();

        for (int i = 0; i < str.length;i++) {
            if (str[i] == '0' || str[i] == '1' || str[i] == '2' || str[i] == '3' || str[i] == '4' || str[i] == '5' || str[i] == '6' || str[i] == '7' || str[i] == '8' || str[i] == '9') {
                ParsedString += str[i];
            } else if (str[i] == '.' && DotCount == 0) {
                ParsedString += '.';
                DotCount = DotCount + 1;
            }
        }
        double result = 0;
        result = Double.parseDouble(ParsedString);
        return result;
    }

}
