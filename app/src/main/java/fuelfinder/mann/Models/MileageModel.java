package fuelfinder.mann.Models;

import android.net.Uri;

/**
 * Created by nathan on 3/24/15.
 */
public class MileageModel {
    // An application model to drive the MileageModel view
        private String carName;
        private int year;
        private String make;
        private String carModel;
        private double engine;
        private String transmission;
        private Uri mileageUri;
        private double mileage;
        private int vehicleID;
        public static final String temp2 = "temp2";
    // private function to store the data to the device

    public void setCarName(String carName) { this.carName = carName; }

    public String getCarName() { return carName; }

    public MileageModel() {
        vehicleID = 0;
        carName = "Name";
        year = 0;
        make = "none";
        carModel = "none";
        engine = 0;
        transmission = "none";
        mileage = 0;
    }

    // Need to make a data input for vehicleInfo that holds make/model/year/options
    public Uri getMileageUri()
    {
        return mileageUri;
    }

    public void setMileageUri(Uri mileageUri)
    {
        this.mileageUri = mileageUri;
    }

    public int getYear()
    {
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    public  String getMake()
    {
        return make;
    }

    public void setMake(String  make)
    {
        this.make = make;
    }

    public String  getModel()
    {
        return carModel;
    }

    public void setModel(String carModel)
    {
        this.carModel = carModel;
    }

    public double getEngine() { return engine; }

    public void setEngine(double  engine)
    {
        this.engine = engine;
    }

    public String  getTransmission() { return transmission; }

    public void setTransmission(String transmission) { this.transmission = transmission; }

    public double  getUserMileage() { return mileage; }

    public void setUserMileage(double mileage)
    {
        this.mileage = mileage;
    }

    public MileageModel getMileageModel() { return this; }

    public void setVehicleID(int vehicleName) {this.vehicleID = vehicleID; }

    public int getVehicleID() { return vehicleID; }

}
