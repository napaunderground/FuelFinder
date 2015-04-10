package fuelfinder.mann.Parser;

/**
 * Created by Action Johnny on 4/9/2015.
 */
import android.location.Location;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import fuelfinder.mann.Models.FuelPriceModel;

//
public class FuelSourceParserV2 {


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


    // Converts a JSON string to a RecipeModel
    static public ArrayList<FuelPriceModel> JSONtoModel(Location myLoc) throws JSONException {

        HttpClient client = new DefaultHttpClient();

        HttpGet request = new HttpGet("http://api.mygasfeed.com/stations/radius/" + myLoc.getLatitude() + "/" + myLoc.getLongitude() + "/3/reg/Price/wr6ntggmbw.json");
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


        JSONObject json;

        //creating the instance of the Gson parser
        Gson gson = new Gson();
        json = (JSONObject) new JSONTokener(result).nextValue();
        JSONArray array = json.getJSONArray("stations");
        for (int i = 0; i < array.length(); i++) {
            if (array.get(i) != null) {
                JSONObject S = array.getJSONObject(i);

                //invoking the parser and converting JSON string to FuelPriceModel
                FuelPriceModel model = gson.fromJson(S.toString(), FuelPriceModel.class);
                Models.add(model);
            }
        }
        return Models;

    }
}
