package fuelfinder.mann.Activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import fuelfinder.mann.R;

/**

 */
public class LocationFragment2 extends Fragment {
    static String Num;
    static double DLat;
    static double DLng;
    static int ResID;
    private MapFragment getMapFragment() {
        FragmentManager fm = null;


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            fm = getFragmentManager();
        } else {

            fm = getChildFragmentManager();
        }
        return (MapFragment) fm.findFragmentById(R.id.location_map2);

    }



    private static View view;
    /**
     * Note that this may be null if the Google Play services APK is not
     * available.
     */

    private static GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String lat=getArguments().getString("Lat");
        String lng=getArguments().getString("Lng");
        String rid=getArguments().getString("RID");
        Num=getArguments().getString("NUM");

        int ResID = Integer.parseInt(rid);
        DLat = Double.parseDouble(lat);
        DLng = Double.parseDouble(lng);
        if (container == null) {
            return null;
        }
        view = (RelativeLayout) inflater.inflate(R.layout.location_fragment, container, false);
        // Passing harcoded values for latitude & longitude. Please change as per your need. This is just used to drop a Marker on the Map

        setUpMapIfNeeded(); // For setting up the MapFragment

        return view;
    }

    /***** Sets up the map if it is possible to do so *****/
    public void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = getMapFragment().getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null)
                setUpMap();
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the
     * camera.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap}
     * is not null.
     */
    private static void setUpMap() {
        // For showing a move to my loction button
        mMap.setMyLocationEnabled(true);
        // For dropping a marker at a point on the Map
        mMap.addMarker(new MarkerOptions().position(new LatLng(DLat, DLng)));
        // For zooming automatically to the Dropped PIN Location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(DLat,
                DLng), 15));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        // TODO Auto-generated method stub
        if (mMap != null)
            setUpMap();


        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((MapFragment) StationPickerActivity.fragmentManager
                    .findFragmentById(ResID)).getMap(); // getMap is deprecated
            // Check if we were successful in obtaining the map.
            if (mMap != null)
                setUpMap();
        }
    }

    /**** The mapfragment's id must be removed from the FragmentManager
     **** or else if the same it is passed on the next time then
     **** app will crash ****/
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        Fragment fragment = (getFragmentManager().findFragmentById(ResID));
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }
}