package fuelfinder.mann.Models;

import android.net.Uri;

/**
 * Created by nathan on 3/24/15.
 */
public class MileageModel {
    // An application model to drive the MileageModel view
        private String carName;
        private String year;
        private String make;
        private String carModel;
        private String engine;
        private String transmission;
        private Uri mileageUri;
        private String mileage;
        public static final String temp2 = "temp2";

    public void setCarName(String carName) { this.carName = carName; }

    public String getCarName() { return carName; }

    public MileageModel() {

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

    public String getYear()
    {
        return year;
    }

    public void setYear(String year)
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

    public String getEngine() { return engine; }

    public void setEngine(String  engine)
    {
        this.engine = engine;
    }

    public String  getTransmission() { return transmission; }

    public void setTransmission(String transmission) { this.transmission = transmission; }

    public String  getUserMileage() { return mileage; }

    public void setUserMileage(String mileage)
    {
        this.mileage = mileage;
    }


}
