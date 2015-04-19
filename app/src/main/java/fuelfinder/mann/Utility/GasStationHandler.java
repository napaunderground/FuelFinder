package fuelfinder.mann.Utility;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.os.Bundle;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import fuelfinder.mann.Models.FuelPriceModel;
import fuelfinder.mann.Parser.GMapV2Direction;
import fuelfinder.mann.Service.CostCalculator;

/**
 * Created by Action Johnny on 4/12/2015.
 */



public class GasStationHandler{
    private MileageModelDataSource datasource;
    public class IndexInfo{
        int index;
        double priceInfo;

    }


    public void onCreate(Bundle savedInstanceState) {


        datasource.open();

    }



    public ArrayList<FuelPriceModel> getBestStations(ArrayList<FuelPriceModel> Stations, double Mileage, Location myLoc){

        double CheapVal1 = 1000;
        double CheapVal2 = 1000;
        double CheapVal3 = 1000;
        double CheapVal4 = 1000;
        int Index1 = 0;
        int Index2 = 0;
        int Index3 = 0;
        int Index4 = 0;
        GMapV2Direction GMV2D = new GMapV2Direction();
        ArrayList<FuelPriceModel> BestStations = new ArrayList();
        ArrayList<IndexInfo> Prices = new ArrayList();
        CostCalculator C = new CostCalculator();
        for (int i = 0; i < Stations.size(); i++){
            double Price = C.findCost(Mileage,StringToDouble(Stations.get(i).kmDistance)*0.621371,Stations.get(i).pricePerGallon) + Stations.get(i).pricePerGallon;
            IndexInfo II = new IndexInfo();
            II.index = i; II.priceInfo = Price;
            Prices.add(II);
        }
        for (int p = 0; p < Prices.size(); p++){
            if (Prices.get(p).priceInfo < CheapVal1){
                CheapVal1 = Prices.get(p).priceInfo;
                Index1 = Prices.get(p).index;
            }
            else if(Prices.get(p).priceInfo < CheapVal2){
                CheapVal2 = Prices.get(p).priceInfo;
                Index2 = Prices.get(p).index;
            }
            else if(Prices.get(p).priceInfo < CheapVal3){
                CheapVal3 = Prices.get(p).priceInfo;
                Index3 = Prices.get(p).index;
            }
            else if(Prices.get(p).priceInfo < CheapVal4){
                CheapVal4 = Prices.get(p).priceInfo;
                Index4 = Prices.get(p).index;
            }
        }
        BestStations.add(Stations.get(Index1));
        BestStations.add(Stations.get(Index2));
        BestStations.add(Stations.get(Index3));
        BestStations.add(Stations.get(Index4));
        return BestStations;

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


}