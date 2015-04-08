package fuelfinder.mann.Service;

/**
 * Created by nathan on 3/31/15.
 */
public class CostCalculator {
    private double tripCost;
    public double findCost(double Mileage, double Distance, double CostPerGallon){
       tripCost = Distance * (1/Mileage) * CostPerGallon;
        return tripCost;
    }
}