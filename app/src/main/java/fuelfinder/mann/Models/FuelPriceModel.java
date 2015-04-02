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
        return 3.14;//pricePerGallon;
    }

    public void setPricePerGallon(double pricePerGallon)
    {
        this.pricePerGallon = pricePerGallon;
    }
    public String getStationID() {
        return "SHELL";//stationID;
    }
    public void setStationID() {
        this.stationID = stationID;
    }
    public LatLng getLocation() {
        LatLng stationPosition = new LatLng(-122.679303, 38.335555);
        return stationPosition;//latLong;
    }
    public void setLocation(Location latLong)
    {
        this.latLong = latLong;
    }
}
