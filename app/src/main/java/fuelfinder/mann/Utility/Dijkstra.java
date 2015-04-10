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
    public Edge[] adjacencies;
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
        List<Vertex> path = new ArrayList<Vertex>();
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
        ArrayList<Double> Distances = new ArrayList();
        GMapV2Direction DistanceParser = new GMapV2Direction();
        ArrayList<Vertex> Nodes = new ArrayList();
        for (int p = 0; p < Models.size(); p++){

            LatLng stationLoc = new LatLng(Models.get(p).Lat, Models.get(p).Lng);
            Document Doc = DistanceParser.getDocument(myLocation, stationLoc);
            double Distance = DistanceParser.getDistanceValue(Doc);
            Distances.add(Distance);
            Vertex V = new Vertex(Models.get(p).stationID);
            V.adjacencies = new Edge[]{
                    new Edge(start, C.findCost(Mileage, Distances.get(p),Models.get(p).pricePerGallon)),
                    new Edge(end, Models.get(p).pricePerGallon)
            };
            Nodes.add(V);

        }



        ArrayList<FuelPriceModel> StationOrder = new ArrayList();
/*
        FuelPriceModel M1 = Models.get(1);
        Location ML1 = M1.getLocation();
        LatLng StationLoc1 = new LatLng(ML1.getLatitude(), ML1.getLongitude());

        FuelPriceModel M2 = Models.get(2);
        Location ML2 = M2.getLocation();
        LatLng StationLoc2 = new LatLng(ML2.getLatitude(), ML2.getLongitude());

        FuelPriceModel M3 = Models.get(3);
        Location ML3 = M0.getLocation();
        LatLng StationLoc3 = new LatLng(ML3.getLatitude(), ML3.getLongitude());

        GMapV2Direction DistanceParser = new GMapV2Direction();

        Document Doc0 = DistanceParser.getDocument(myLocation, StationLoc0);
        double Distance0 = DistanceParser.getDistanceValue(Doc0);

        Doc0 = DistanceParser.getDocument(myLocation, StationLoc1);
        double Distance1 = DistanceParser.getDistanceValue(Doc0);

        Doc0 = DistanceParser.getDocument(myLocation, StationLoc2);
        double Distance2 = DistanceParser.getDistanceValue(Doc0);

        Doc0 = DistanceParser.getDocument(myLocation, StationLoc3);
        double Distance13 = DistanceParser.getDistanceValue(Doc0);

        Vertex start = new Vertex("start");
        Vertex v0 = new Vertex(M0.getStationID());
        Vertex v1 = new Vertex(M1.getStationID());
        Vertex v2 = new Vertex(M2.getStationID());
        Vertex v3 = new Vertex(M3.getStationID());
        Vertex end = new Vertex("end");

        //Replace numbers with cost of gas to get to station and cost of gas @ station.
        start.adjacencies = new Edge[]{
                new Edge(v0, 3),
                new Edge(v1, 2),
                new Edge(v2, 1),
                new Edge(v3, 4)
        };
        v0.adjacencies = new Edge[]{
                new Edge(end, 10)
        };
        v1.adjacencies = new Edge[]{
                new Edge(end, 10)
        };
        v2.adjacencies = new Edge[]{
                new Edge(end, 10)
        };
        v3.adjacencies = new Edge[]{
                new Edge(end, 10)
        };
        Vertex[] vertices = {start, v0, v1, v2, v3, end};
        computePaths(start);
        for (Vertex v : vertices){
            List<Vertex> path = getShortestPathTo(v);
        }
*/


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