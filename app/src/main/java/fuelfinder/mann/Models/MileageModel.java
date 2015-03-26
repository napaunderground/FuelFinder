package fuelfinder.mann.Models;

import android.net.Uri;

/**
 * Created by nathan on 3/24/15.
 */
public class MileageModel {
    // An application model to drive the MileageModel view
        private String year;
        private String make;
        private String carModel;
        private String options;
        private Uri mileageUri;
        private int mileage;

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

    public String getMake()
    {
        return make;
    }

    public void setMake(String make)
    {
        this.make = make;
    }

    public String getModel()
    {
        return carModel;
    }

    public void setModel(String carModel)
    {
        this.carModel = carModel;
    }

    public String getOptions()
    {
        return options;
    }

    public void setOptions(String options)
    {
        this.options = options;
    }

    public void setUserMileage(int mileage)
    {
        this.mileage = mileage;
    }
}
