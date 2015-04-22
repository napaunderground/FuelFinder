package fuelfinder.mann.Parser;

import android.location.Location;
import android.os.StrictMode;

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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import fuelfinder.mann.Models.DirectionsMatrixModel;
import fuelfinder.mann.Models.FuelPriceModel;

/**
 * Created by Action Johnny on 4/21/2015.
 */
public class DirectionsMatrixParser {

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



    static public ArrayList<DirectionsMatrixModel> JSONtoModel(Location myLoc, ArrayList<FuelPriceModel> GasLoc) throws JSONException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        String url ="https://maps.googleapis.com/maps/api/distancematrix/json?origins="+myLoc.getLatitude()+","+myLoc.getLongitude()+"&destinations=";
        for (int start = 0; start < GasLoc.size(); start++){
        //int start = 0;
            try {
                url += GasLoc.get(start).Lat + "," + GasLoc.get(start).Lng + URLEncoder.encode("|", "UTF-8");
            }
            catch(UnsupportedEncodingException e){

            }
        }
        url+= "&mode=driving&sensor=false&key=AIzaSyDI1tZcNC5sAyOFh7nbnWxF3M65d5-XhOk";
        HttpClient client = new DefaultHttpClient();

        HttpGet request = new HttpGet(url);
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

        } catch (ClientProtocolException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

       // https://maps.googleapis.com/maps/api/distancematrix/json?origins=38.33555333333334,-122.67930166666665&destinations=38.336029,-122.707947|38.336029,-122.707367|38.335442,-122.712448|38.348927,-122.724945|38.348469,-122.717957|38.336079,-122.676323|38.331551,-122.714912|38.34803,-122.709557|38.331211,-122.710571|38.331562,-122.715004|38.36351,-122.712051|38.362179,-122.714813|38.328854,-122.708412|38.33041,-122.695183|38.335892,-122.685402|38.36351,-122.714989|38.348431,-122.716988|&mode=driving&sensor=false&key=AIzaSyDI1tZcNC5sAyOFh7nbnWxF3M65d5-XhOk

        ArrayList<DirectionsMatrixModel> Models = new ArrayList();


        JSONObject json;

        //creating the instance of the Gson parser
        Gson gson = new Gson();
        json = (JSONObject) new JSONTokener(result).nextValue();
        JSONArray RowArray = json.getJSONArray("rows");
        JSONObject RowObj = RowArray.getJSONObject(0);
        JSONArray EleArray = RowObj.getJSONArray("elements");
        JSONObject EleObj = EleArray.getJSONObject(0);

        int lp = 9;
        int dd = 28 + 49;
        for (int i = 0; i < EleArray.length(); i++) {
            if (EleArray.get(i) != null) {


                JSONObject S = EleArray.getJSONObject(i);
                JSONObject DistVal = S.getJSONObject("distance");





                int dsaf = 0;
                int dddd = 2313;
                dsaf+=dddd;
                //invoking the parser and converting JSON string to FuelPriceModel
                DirectionsMatrixModel model = gson.fromJson(DistVal.toString(), DirectionsMatrixModel.class);
                Models.add(model);
            }
        }
        return Models;

    }
}
