package fuelfinder.mann.Activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import fuelfinder.mann.Models.StationInfoModel;
import fuelfinder.mann.R;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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


public class StationPickerActivity extends FragmentActivity {

    static FragmentManager fragmentManager;
    static FragmentManager fragmentManager2;
    static FragmentManager fragmentManager3;
    static FragmentManager fragmentManager4;
    static FragmentTransaction ft1;

    private MileageModelDataSource datasource;
    String ID = "";
    int IntID = 0;
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

    private ImageView StationLogo1;
    private ImageView StationLogo2;
    private ImageView StationLogo3;
    private ImageView StationLogo4;

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

        //startActivityFromFragment();


        Location mCurrentLocation = new Location("Here!");
        myLat = getIntent().getExtras().getString("mLat");
        myLng = getIntent().getExtras().getString("mLng");
        ID = getIntent().getExtras().getString("ID");
        IntID = Integer.parseInt(ID);
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
        double MileageValue = datasource.getAllVehicles().get(IntID).getUserMileage();
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

        if (TotalCost1.length() == 4)
            TotalCost1 += "0";
        if (TotalCost2.length() == 4)
            TotalCost2 += "0";

        if (TotalCost3.length() == 4)
            TotalCost3 += "0";

        if (TotalCost4.length() == 4)
            TotalCost4 += "0";



        TotalCostView1 = (TextView) findViewById(R.id.textView17);
        TotalCostView1.setText(TotalCost1);

        TotalCostView2 = (TextView) findViewById(R.id.textView13);
        TotalCostView2.setText(TotalCost2);

        TotalCostView3 = (TextView) findViewById(R.id.textView16);
        TotalCostView3.setText(TotalCost3);

        TotalCostView4 = (TextView) findViewById(R.id.textView15);
        TotalCostView4.setText(TotalCost4);

        firstChoice = (Button) findViewById(R.id.checkBox);
        firstChoice.setText(bestStations.get(0).FPM.stationID+Html.fromHtml("<br /><small>Best Cost</small>"));

        secondChoice = (Button) findViewById(R.id.checkBox2);
        secondChoice.setText(bestStations.get(1).FPM.stationID+Html.fromHtml("<br /><small>2nd Cost</small>"));


        thirdChoice = (Button) findViewById(R.id.checkBox3);
        thirdChoice.setText(bestStations.get(2).FPM.stationID+Html.fromHtml("<br /><small>3rd Cost</small>"));

        fourthChoice = (Button) findViewById(R.id.checkBox4);
        fourthChoice.setText(bestStations.get(3).FPM.stationID+ Html.fromHtml("<br /><small>4th Cost</small>"));

        //http://api.mygasfeed.com/stations/radius/38.3356/-122.679/4/reg/Price/z7lq1tt911.json

        int varval = 0;
        StationLogo1 = (ImageView) findViewById(R.id.imageView);
        if(bestStations.get(0).FPM.stationID.equals("Shell")){
            StationLogo1.setImageResource(R.mipmap.shell);}
        else if (bestStations.get(0).FPM.stationID.equals("Valero")){
            StationLogo1.setImageResource(R.mipmap.valero);}
        else if (bestStations.get(0).FPM.stationID.equals("Exxon")){
            StationLogo1.setImageResource(R.mipmap.exxon);}
        else if (bestStations.get(0).FPM.stationID.equals("Chevron")){
            StationLogo1.setImageResource(R.mipmap.chevron);}
        else if (bestStations.get(0).FPM.stationID.equals("7-Eleven")){
            StationLogo1.setImageResource(R.mipmap.seveneleven);}
        else if (bestStations.get(0).FPM.stationID.equals("Gulf")){
            StationLogo1.setImageResource(R.mipmap.gulf);}
        else if (bestStations.get(0).FPM.stationID.equals("76")){
            StationLogo1.setImageResource(R.mipmap.seventysix);}
        else if (bestStations.get(0).FPM.stationID.equals("Circle K")){
            StationLogo1.setImageResource(R.mipmap.circlek);}
        else if (bestStations.get(varval).FPM.stationID.equals("Texaco")){
            StationLogo1.setImageResource(R.mipmap.texaco);}
        else if (bestStations.get(varval).FPM.stationID.equals("Mobil")){
            StationLogo1.setImageResource(R.mipmap.mobil);}
        else if (bestStations.get(varval).FPM.stationID.equals("Sunoco")){
            StationLogo1.setImageResource(R.mipmap.sunoco);}
        else{
            StationLogo1.setImageResource(R.mipmap.ic_launcher);}

        varval = 1;
        StationLogo2 = (ImageView) findViewById(R.id.imageView2);
        if(bestStations.get(1).FPM.stationID.equals("Shell")){
            StationLogo2.setImageResource(R.mipmap.shell);}
        else if (bestStations.get(1).FPM.stationID.equals("Valero")){
            StationLogo2.setImageResource(R.mipmap.valero);}
        else if (bestStations.get(1).FPM.stationID.equals("Exxon")){
            StationLogo2.setImageResource(R.mipmap.exxon);}
        else if (bestStations.get(1).FPM.stationID.equals("Chevron")){
            StationLogo2.setImageResource(R.mipmap.chevron);}
        else if (bestStations.get(1).FPM.stationID.equals("7-Eleven")){
            StationLogo2.setImageResource(R.mipmap.seveneleven);}
        else if (bestStations.get(1).FPM.stationID.equals("Gulf")){
            StationLogo2.setImageResource(R.mipmap.gulf);}
        else if (bestStations.get(1).FPM.stationID.equals("76")){
            StationLogo2.setImageResource(R.mipmap.seventysix);}
        else if (bestStations.get(varval).FPM.stationID.equals("Circle K")){
            StationLogo2.setImageResource(R.mipmap.circlek);}
        else if (bestStations.get(varval).FPM.stationID.equals("Texaco")){
            StationLogo2.setImageResource(R.mipmap.texaco);}
        else if (bestStations.get(varval).FPM.stationID.equals("Mobil")){
            StationLogo2.setImageResource(R.mipmap.mobil);}
        else if (bestStations.get(varval).FPM.stationID.equals("Sunoco")){
            StationLogo2.setImageResource(R.mipmap.sunoco);}
        else{
            StationLogo2.setImageResource(R.mipmap.ic_launcher);}

        varval = 2;
        StationLogo3 = (ImageView) findViewById(R.id.imageView3);
        if(bestStations.get(2).FPM.stationID.equals("Shell")){
            StationLogo3.setImageResource(R.mipmap.shell);}
        else if (bestStations.get(2).FPM.stationID.equals("Valero")){
            StationLogo3.setImageResource(R.mipmap.valero);}
        else if (bestStations.get(2).FPM.stationID.equals("Exxon")){
            StationLogo3.setImageResource(R.mipmap.exxon);}
        else if (bestStations.get(2).FPM.stationID.equals("Chevron")){
            StationLogo3.setImageResource(R.mipmap.chevron);}
        else if (bestStations.get(2).FPM.stationID.equals("7-Eleven")){
            StationLogo3.setImageResource(R.mipmap.seveneleven);}
        else if (bestStations.get(varval).FPM.stationID.equals("Gulf")){
            StationLogo3.setImageResource(R.mipmap.gulf);}
        else if (bestStations.get(varval).FPM.stationID.equals("76")){
            StationLogo3.setImageResource(R.mipmap.seventysix);}
        else if (bestStations.get(varval).FPM.stationID.equals("Circle K")){
            StationLogo3.setImageResource(R.mipmap.circlek);}
        else if (bestStations.get(varval).FPM.stationID.equals("Texaco")){
            StationLogo3.setImageResource(R.mipmap.texaco);}
        else if (bestStations.get(varval).FPM.stationID.equals("Mobil")){
            StationLogo3.setImageResource(R.mipmap.mobil);}
        else if (bestStations.get(varval).FPM.stationID.equals("Sunoco")){
            StationLogo3.setImageResource(R.mipmap.sunoco);}
        else{
            StationLogo3.setImageResource(R.mipmap.ic_launcher);}

        varval=3;
        StationLogo4 = (ImageView) findViewById(R.id.imageView4);
        if(bestStations.get(3).FPM.stationID.equals("Shell")){
            StationLogo4.setImageResource(R.mipmap.shell);}
        else if (bestStations.get(3).FPM.stationID.equals("Valero")){
            StationLogo4.setImageResource(R.mipmap.valero);}
        else if (bestStations.get(3).FPM.stationID.equals("Exxon")){
            StationLogo4.setImageResource(R.mipmap.exxon);}
        else if (bestStations.get(3).FPM.stationID.equals("Chevron")){
            StationLogo4.setImageResource(R.mipmap.chevron);}
        else if (bestStations.get(3).FPM.stationID.equals("7-Eleven")){
            StationLogo4.setImageResource(R.mipmap.seveneleven);}
        else if (bestStations.get(varval).FPM.stationID.equals("Gulf")){
            StationLogo4.setImageResource(R.mipmap.gulf);}
        else if (bestStations.get(varval).FPM.stationID.equals("76")){
            StationLogo4.setImageResource(R.mipmap.seventysix);}
        else if (bestStations.get(varval).FPM.stationID.equals("Circle K")){
            StationLogo4.setImageResource(R.mipmap.circlek);}
        else if (bestStations.get(varval).FPM.stationID.equals("Texaco")){
            StationLogo4.setImageResource(R.mipmap.texaco);}
        else if (bestStations.get(varval).FPM.stationID.equals("Mobil")){
            StationLogo4.setImageResource(R.mipmap.mobil);}
        else if (bestStations.get(varval).FPM.stationID.equals("Sunoco")){
            StationLogo4.setImageResource(R.mipmap.sunoco);}
        else{
            StationLogo4.setImageResource(R.mipmap.ic_launcher);}
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


        Bundle m1 = new Bundle();
        m1.putString("Lat", ""+bestStations.get(0).FPM.Lat);
        m1.putString("Lng", ""+bestStations.get(0).FPM.Lng);
        m1.putString("RID", ""+R.id.map1);
        m1.putString("NUM", "1");
        Bundle m2 = new Bundle();
        m2.putString("Lat", ""+bestStations.get(1).FPM.Lat);
        m2.putString("Lng", ""+bestStations.get(1).FPM.Lng);
        m2.putString("RID", ""+R.id.map2);
        m2.putString("NUM", "2");
        Bundle m3 = new Bundle();
        m3.putString("Lat", ""+bestStations.get(2).FPM.Lat);
        m3.putString("Lng", "" + bestStations.get(2).FPM.Lng);
        m3.putString("RID", ""+R.id.map3);
        m3.putString("NUM", "3");
        Bundle m4 = new Bundle();
        m4.putString("Lat", ""+bestStations.get(3).FPM.Lat);
        m4.putString("Lng", ""+bestStations.get(3).FPM.Lng);
        m4.putString("RID", ""+R.id.map4);
        m4.putString("NUM", "4");

        LocationFragment Loc1=new LocationFragment();
        Loc1.setArguments(m1);

        LocationFragment2 Loc2=new LocationFragment2();
        Loc2.setArguments(m2);

        LocationFragment3 Loc3=new LocationFragment3();
        Loc3.setArguments(m3);

        LocationFragment4 Loc4=new LocationFragment4();
        Loc4.setArguments(m4);




        fragmentManager = getFragmentManager();


        fragmentManager.beginTransaction().replace(R.id.map1, Loc1).commit();
        fragmentManager.executePendingTransactions();
        fragmentManager2 = getFragmentManager();
        fragmentManager2.beginTransaction().replace(R.id.map2, Loc2).commit();
        fragmentManager2.executePendingTransactions();
        fragmentManager3 = getFragmentManager();
        fragmentManager3.beginTransaction().replace(R.id.map3, Loc3).commit();
        fragmentManager3.executePendingTransactions();
        fragmentManager4 = getFragmentManager();
        fragmentManager4.beginTransaction().replace(R.id.map4, Loc4).commit();
        fragmentManager4.executePendingTransactions();



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

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, StartActivity.class));
    }

}
