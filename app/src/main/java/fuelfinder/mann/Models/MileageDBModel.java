package fuelfinder.mann.Models;

/**
 * Created by Action Johnny on 4/16/2015.
 */
public class MileageDBModel {
    private long id;
    private String mileage;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String Mileage) {
        this.mileage = Mileage;
    }

    @Override
    public String toString() {
        return mileage;
    }
}