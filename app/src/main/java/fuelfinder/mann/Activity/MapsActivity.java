package fuelfinder.mann.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import fuelfinder.mann.Models.FuelPriceModel;
import fuelfinder.mann.Parser.FuelSourceParserV2;
import fuelfinder.mann.Parser.GMapV2Direction;
import fuelfinder.mann.R;
import fuelfinder.mann.Utility.Dijkstra;
import fuelfinder.mann.Utility.GasStationHandler;
import fuelfinder.mann.Utility.MileageModelDataSource;

public class MapsActivity extends FragmentActivity implements
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private MileageModelDataSource datasource;
    private static final double MILEAGE_VALUE = 10.0;
    private static final long ONE_MIN = 1000 * 60;
    private static final long TWO_MIN = ONE_MIN * 2;
    private static final long FIVE_MIN = ONE_MIN * 5;
    private static final long POLLING_FREQ = 1000 * 30;
    private static final long FASTEST_UPDATE_FREQ = 1000 * 5;
    private static final float MIN_ACCURACY = 25.0f;
    private static final float MIN_LAST_READ_ACCURACY = 500.0f;
    private final static int REQUEST_RESOLVE_ERROR = 1001;
    private LocationRequest mLocationRequest;
    private Location mBestReading;
    private GoogleApiClient mGoogleApiClient;
    String StationNum;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(POLLING_FREQ);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_FREQ);
        buildGoogleApiClient();
        StationNum = getIntent().getExtras().getString("station");
        datasource = new MileageModelDataSource(this);
        datasource.open();
        /*if (StationNum != null){
        StationNum = getIntent().getExtras().getString("station");
        }
        else{
            StationNum = "0";
        }*/



    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
/*
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }*/

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.setMyLocationEnabled(true);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap()
    {

    }

    //------------//------------//---------//----

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub


        super.onResume();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        setUpMapIfNeeded();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_RESOLVE_ERROR) {

            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }

    }

    private boolean servicesAvailable() {

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0).show();
            return false;
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // TODO Auto-generated method stub
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this,
                        REQUEST_RESOLVE_ERROR);
                                /*
                                 * Thrown if Google Play services canceled the original
                                 * PendingIntent
                                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                        /*
                         * If no resolution is available, display a dialog to the user with
                         * the error.
                         */
            Log.d("Connection Failed:", "" + connectionResult.getErrorCode()
                    + " " + connectionResult.toString());
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // TODO Auto-generated method stub
        // Get first reading. Get additional location updates if necessary
        if (servicesAvailable()) {
            // Get best last location measurement meeting criteria
            mBestReading = bestLastKnownLocation(MIN_LAST_READ_ACCURACY, FIVE_MIN);
            if (null == mBestReading
                    || mBestReading.getAccuracy() > MIN_LAST_READ_ACCURACY
                    || mBestReading.getTime() < System.currentTimeMillis() - TWO_MIN) {

                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

                Executors.newScheduledThreadPool(1).schedule(new Runnable() {

                    @Override
                    public void run() {
                        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, MapsActivity.this);
                    }

                }, ONE_MIN, TimeUnit.MILLISECONDS);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        // Determine whether new location is better than current best
        // estimate
        if (null == mBestReading || location.getAccuracy() < mBestReading.getAccuracy()) {
            mBestReading = location;


            if (mBestReading.getAccuracy() < MIN_ACCURACY) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
        }
    }


    private Location bestLastKnownLocation(float minAccuracy, long minTime) {
        Location bestResult = null;
        float bestAccuracy = Float.MAX_VALUE;
        long bestTime = Long.MIN_VALUE;

        // Get the best most recent location currently available
        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mCurrentLocation != null) {
            float accuracy = mCurrentLocation.getAccuracy();
            long time = mCurrentLocation.getTime();

            if (accuracy < bestAccuracy) {
                bestResult = mCurrentLocation;
                bestAccuracy = accuracy;
                bestTime = time;
            }

            Location CurrentLocation;
            CurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            LatLng MyPosition;

            LatLng sourcePosition = new LatLng(CurrentLocation.getLatitude(), CurrentLocation.getLongitude());


            MyPosition = new LatLng(CurrentLocation.getLatitude(), CurrentLocation.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(MyPosition, 15));
            FuelSourceParserV2 FSP = new FuelSourceParserV2();
            ArrayList<FuelPriceModel> FPLoc = new ArrayList();
            try{
                FPLoc = FSP.JSONtoModel(CurrentLocation);
            }
            catch(JSONException e){

            }
            GasStationHandler Handle = new GasStationHandler();
            ArrayList<FuelPriceModel> bestStations = new ArrayList<>();
            double ML;
            ML = datasource.getAllVehicles().get(0).getUserMileage();
            bestStations = Handle.getBestStations(FPLoc, ML);
            int SN = Integer.parseInt(StationNum);
            FuelPriceModel FPM = bestStations.get(SN);
            LatLng gasLoc = new LatLng(FPM.Lat, FPM.Lng);
            mMap.addMarker(new MarkerOptions().position(gasLoc).title(FPM.stationID).snippet("Price Per Gallon: " + Double.toString(FPM.pricePerGallon) + "| Distance: " + FPM.kmDistance));


            /////////////////////////////////////////////////////////

            GMapV2Direction md = new GMapV2Direction();
            mMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            Document doc = md.getDocument(sourcePosition, gasLoc);

            ArrayList<LatLng> directionPoint = md.getDirection(doc);
            PolylineOptions rectLine = new PolylineOptions().width(3).color(
                    Color.RED);

            for (int i = 0; i < directionPoint.size(); i++) {
                rectLine.add(directionPoint.get(i));
            }
            Polyline polylin = mMap.addPolyline(rectLine);

            String DistanceInfo = getDistanceOnRoad(CurrentLocation.getLatitude(), CurrentLocation.getLongitude(), gasLoc.latitude, gasLoc.longitude);
        }

        // Return best reading or null
        if (bestAccuracy > minAccuracy || bestTime < minTime) {
            return null;
        } else {
            return bestResult;
        }
    }


    private void showErrorDialog(int errorCode) {

        // Get the error dialog from Google Play services
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                errorCode,
                this,
                REQUEST_RESOLVE_ERROR);


        // If Google Play services can provide an error dialog
        if (errorDialog != null) {

            errorDialog.show();
        }
    }



    private String getDistanceOnRoad(double latitude, double longitude,
                                     double prelatitute, double prelongitude) {
        /*
        * returns the distance by road between two lat/longs
         */
        String result_in_mi = "";
        String url = "http://maps.google.com/maps/api/directions/xml?origin="
                + latitude + "," + longitude + "&destination=" + prelatitute
                + "," + prelongitude + "&sensor=false&units=imperial";
        String tag[] = {"text"};
        HttpResponse response = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            response = httpClient.execute(httpPost, localContext);
            InputStream is = response.getEntity().getContent();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document doc = builder.parse(is);
            if (doc != null) {
                NodeList nl;
                ArrayList args = new ArrayList();
                for (String s : tag) {
                    nl = doc.getElementsByTagName(s);
                    if (nl.getLength() > 0) {
                        Node node = nl.item(nl.getLength() - 1);
                        args.add(node.getTextContent());
                    } else {
                        args.add(" - ");
                    }
                }
                result_in_mi = String.format("%s", args.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result_in_mi;
    }

    double StringToDouble(String s1) {
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
        startActivity(new Intent(this, fuelfinder.mann.Activity.SettingsActivity.class));
        return;
    }
}
// http://api.mygasfeed.com /stations/radius/(Latitude)/(Longitude)/(distance)/(fuel type)/(sort by)/z7lq1tt911.json
//                                                                                                   api key        ?callback=?