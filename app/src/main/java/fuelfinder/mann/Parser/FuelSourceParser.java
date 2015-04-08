package fuelfinder.mann.Parser;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import fuelfinder.mann.Models.FuelPriceModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;


/**
 * Created by Action Johnny on 4/1/2015.
 */
public class FuelSourceParser {
    static public ArrayList<FuelPriceModel> JSONtoModel(String Input){
        ArrayList<FuelPriceModel> Models = new ArrayList();
        FuelPriceModel Model = new FuelPriceModel();
        JSONObject JSON;
        try{
            JSON = (JSONObject) new JSONTokener(Input).nextValue();
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
                    Location location = null;
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
