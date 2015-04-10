package fuelfinder.mann.Models;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nathan on 3/31/15.
 */
public class FuelPriceModel {

    @SerializedName("station")
    public String stationID;
    @SerializedName("reg_price")
    public double pricePerGallon;
    @SerializedName("lat")
    public double Lat;
    @SerializedName("lng")
    public double Lng;
    @SerializedName("distance")
    public String kmDistance;
    @SerializedName("address")
    public String address;

}
