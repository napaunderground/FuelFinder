package fuelfinder.mann.Utility;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import fuelfinder.mann.Models.FuelPriceModel;
import fuelfinder.mann.Models.MileageModel;
import fuelfinder.mann.Parser.GMapV2Direction;
import fuelfinder.mann.Service.CostCalculator;

/**
 * Created by Action Johnny on 4/2/2015.
 */


class Vertex implements Comparable<Vertex>
{
    public final String name;
    public ArrayList<Edge> adjacencies;
    public double minDistance = Double.POSITIVE_INFINITY;
    public Vertex previous;
    public Vertex(String argName) { name = argName; }
    public String toString() { return name; }
    public int compareTo(Vertex other)
    {
        return Double.compare(minDistance, other.minDistance);
    }
}

class Edge
{
    public final Vertex target;
    public final double weight;
    public Edge(Vertex argTarget, double argWeight)
    { target = argTarget; weight = argWeight; }
}

public class Dijkstra
{
    double StringToDouble(String s1) {
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


    public static void computePaths(Vertex source)
    {
        source.minDistance = 0.;
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
        vertexQueue.add(source);

        while (!vertexQueue.isEmpty()) {
            Vertex u = vertexQueue.poll();

            // Visit each edge exiting u
            for (Edge e : u.adjacencies)
            {
                Vertex v = e.target;
                double weight = e.weight;
                double distanceThroughU = u.minDistance + weight;
                if (distanceThroughU < v.minDistance) {
                    vertexQueue.remove(v);
                    v.minDistance = distanceThroughU ;
                    v.previous = u;
                    vertexQueue.add(v);
                }
            }
        }
    }


    public static Vertex getShortestPathTo(Vertex target)
    {
        ArrayList<Vertex> path = new ArrayList<Vertex>();
        for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
            path.add(vertex);
        Collections.reverse(path);
        return path.get(1);
    }


    public static ArrayList<FuelPriceModel> GetBestStation(ArrayList<FuelPriceModel> Models, LatLng myLocation){
        double Mileage = 10;
        CostCalculator C = new CostCalculator();

        Vertex start = new Vertex("start");
        Vertex end = new Vertex("end");
        double mileage = 10;
        //ArrayList<Double> Distances = new ArrayList();
        //GMapV2Direction DistanceParser = new GMapV2Direction();
        ArrayList<Vertex> Nodes = new ArrayList();
        Nodes.add(start);
        Nodes.add(end);
        Dijkstra d = new Dijkstra();

        for (int p = 0; p < Models.size(); p++){

            Vertex V = new Vertex(Integer.toString(p));
            V.adjacencies.add(new Edge(end, Models.get(p).pricePerGallon));
            Nodes.add(V);
            V.adjacencies.clear();

        }


        ArrayList<FuelPriceModel> StationOrder = new ArrayList();
        for(int z = 0; z < Nodes.size();z++){
            start.adjacencies.add(
                    new Edge(Nodes.get(z),
                    C.findCost(Mileage, (d.StringToDouble(Models.get(z).kmDistance))/1.60934,
                    Models.get(z).pricePerGallon)
                    )
            );
        }

                /*new Edge(Nodes.get(1), C.findCost(Mileage, d.StringToDouble(Models.get(1).kmDistance),Models.get(1).pricePerGallon)),
                new Edge(Nodes.get(2), C.findCost(Mileage, d.StringToDouble(Models.get(2).kmDistance),Models.get(2).pricePerGallon)),
                new Edge(Nodes.get(3), C.findCost(Mileage, d.StringToDouble(Models.get(3).kmDistance),Models.get(3).pricePerGallon)),
                new Edge(Nodes.get(4), C.findCost(Mileage, d.StringToDouble(Models.get(4).kmDistance),Models.get(4).pricePerGallon)),
                new Edge(Nodes.get(5), C.findCost(Mileage, d.StringToDouble(Models.get(5).kmDistance),Models.get(5).pricePerGallon)),
                new Edge(Nodes.get(6), C.findCost(Mileage, d.StringToDouble(Models.get(6).kmDistance),Models.get(6).pricePerGallon)),
                new Edge(Nodes.get(7), C.findCost(Mileage, d.StringToDouble(Models.get(7).kmDistance),Models.get(7).pricePerGallon))*/




        computePaths(start);
        Vertex OptimalStation = new Vertex("");
        for (int i = 0; i < Nodes.size(); i++){
            OptimalStation = getShortestPathTo(Nodes.get(i));
        }
        StationOrder.add(Models.get(Integer.parseInt(OptimalStation.name)));


        return StationOrder;
    }
/*  SYNTAX FOR CREATION

        Vertex v0 = new Vertex("Redvile");
        Vertex v1 = new Vertex("Blueville");
        Vertex v2 = new Vertex("Greenville");
        Vertex v3 = new Vertex("Orangeville");
        Vertex v4 = new Vertex("Purpleville");

        v0.adjacencies = new Edge[]{ new Edge(v1, 5),
                new Edge(v2, 10),
                new Edge(v3, 8) };
        v1.adjacencies = new Edge[]{ new Edge(v0, 5),
                new Edge(v2, 3),
                new Edge(v4, 7) };
        v2.adjacencies = new Edge[]{ new Edge(v0, 10),
                new Edge(v1, 3) };
        v3.adjacencies = new Edge[]{ new Edge(v0, 8),
                new Edge(v4, 2) };
        v4.adjacencies = new Edge[]{ new Edge(v1, 7),
                new Edge(v3, 2) };
        Vertex[] vertices = { v0, v1, v2, v3, v4 };
        computePaths(v0);
        for (Vertex v : vertices)
        {
            System.out.println("Distance to " + v + ": " + v.minDistance);
            List<Vertex> path = getShortestPathTo(v);
            System.out.println("Path: " + path);
        }*/

}