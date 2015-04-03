package fuelfinder.mann.Models;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by nathan on 3/31/15.
 */
public class FuelPriceModel {
    private String stationID;
    private double pricePerGallon;
    private Location latLong;

    public double getPricePerGallon() {
        return pricePerGallon;//pricePerGallon;
    }

    public void setPricePerGallon(double pricePerGallon)
    {
        this.pricePerGallon = pricePerGallon;
    }
    public String getStationID() {
        return stationID;//stationID;
    }
    public void setStationID(String Input) {
        this.stationID = Input;
    }
    public Location getLocation() {
        return latLong;//latLong;
    }
    public void setLocation(Location latLong)
    {
        this.latLong = latLong;
    }
}
