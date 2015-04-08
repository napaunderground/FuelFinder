package fuelfinder.mann.Parser;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import fuelfinder.mann.Models.FuelPriceModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Created by Action Johnny on 4/1/2015.
 */
public class FuelSourceParser {
    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }



    static public ArrayList<FuelPriceModel> JSONtoModel(Location myLoc){
        HttpClient client = new DefaultHttpClient();

        HttpGet request = new HttpGet("http://api.mygasfeed.com/stations/radius/"+myLoc.getLatitude()+"/"+myLoc.getLongitude()+"/3/reg/Price/wr6ntggmbw.json");
        HttpResponse response;
        String result = null;
        try {
            response = client.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {

                // A Simple JSON Response Read
                InputStream instream = entity.getContent();
                result = convertStreamToString(instream);
                // now you have the string representation of the HTML request
                instream.close();

            }
            // Headers
            org.apache.http.Header[] headers = response.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                System.out.println(headers[i]);
            }
        } catch (ClientProtocolException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


            ArrayList<FuelPriceModel> Models = new ArrayList();
        FuelPriceModel Model = new FuelPriceModel();
        JSONObject JSON;
        try{
            JSON = (JSONObject) new JSONTokener(result).nextValue();
            JSONArray Array = JSON.getJSONArray("stations");
            for (int i =0 ; i < 8; i++)
            {
                if (Array.get(i) != null ) {
                    JSONObject Station = Array.getJSONObject(i);
                    double lat;
                    double lng;
                    String SLat = (String) Station.get("lat");
                    String SLng = (String) Station.get("lng");
                    String Name = (String) Station.get("station");
                    lat = Double.parseDouble(SLat);
                    lng = Double.parseDouble(SLng);
                    // LatLng Loc = new LatLng(lat,lng);
                    Location location = new Location("StationLocation");
                    location.setLatitude(lat);
                    location.setLongitude(lng);
                    Model.setStationID(Name);
                    Model.setLocation(location);
                    Models.add(Model);
                }
            }


            /*            model.setName((String)recipe.get("name"));
            model.setCuisine((String) recipe.get("cuisine"));
            model.setRecipeUri(Uri.parse(((String)recipe.get("thumb"))));*/

        }
        catch(JSONException exception){
            exception.getCause();
        }


        return Models;
    }
}
// http://api.mygasfeed.com /stations/radius/(Latitude)/(Longitude)/(distance)/(fuel type)/(sort by)/z7lq1tt911.json
//                                                                                                   api key        ?callback=?