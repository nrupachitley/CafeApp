package com.nrupachitley.cafesearch;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "MapFragment";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private MapView mapView;
    private View view;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private EditText searchBar;
    private String searchedLatitude;
    private String searchedLongitude;

    SendData sendData;

    public MapsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_maps, container, false);
        mapView = (MapView) view.findViewById(R.id.mapDisplay);
        searchBar = (EditText) view.findViewById(R.id.searchBar);

        try {
            getLocationPermission();
            if(mLocationPermissionsGranted) {
                initMap();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public void initMap() {
        mapView.onCreate(null);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    public void getLocationPermission() {
        Log.i(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
             && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
            }
        else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void search() {
        Log.i(TAG, "search: called");
        searchBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if((keyEvent.getAction() == keyEvent.ACTION_DOWN) && i == keyEvent.KEYCODE_ENTER){
                   displaySearchedCafe();
                }
                return false;
            }
        });
    }

    private void displaySearchedCafe() {
        Log.i(TAG, "displaySearchedCafe: called");
        String searchedCafe = searchBar.getText().toString();
        Log.i(TAG, "searchedCafe: " + searchedCafe);

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        try{
            List<Address> addressList = geocoder.getFromLocationName(searchedCafe, 1);
            Log.i(TAG, "List length: " + String.valueOf(addressList.size()));
            if(addressList.size() > 0) {
                Address searchedLocation = addressList.get(0);
                searchedLatitude = String.valueOf(searchedLocation.getLatitude());
                searchedLongitude = String.valueOf(searchedLocation.getLongitude());
                LatLng searchedLatlng = new LatLng(searchedLocation.getLatitude(), searchedLocation.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(searchedLatlng).title("Searched Cafe" + searchedCafe));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(searchedLatlng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Showing Searched Cafe", Snackbar.LENGTH_LONG).show();
                searchNearbyCafes((double) searchedLocation.getLatitude(), (double) searchedLocation.getLongitude());
            }
            else {
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Cannot find Location", Snackbar.LENGTH_SHORT).show();
            }
        }
        catch (IOException e) {

        }
    }

    private void searchNearbyCafes(double latitude, double longitude) {
        Log.i(TAG, "searchNearbyCafes: called");

        ArrayList<DataModel> allCafes = DataService.getInstance().getNearByCafes(latitude, longitude);
        ArrayList<DataModel> nearbyLocations = DataService.getInstance().shortListLocations(latitude, longitude, allCafes);
        for(int i = 0; i < nearbyLocations.size(); i++) {
            Log.i(TAG, nearbyLocations.get(i).getCafeName());
            DataModel cafe = nearbyLocations.get(i);
            MarkerOptions marker=new MarkerOptions().position(new LatLng(cafe.getLatitude(), cafe.getLongitude()));
            marker.title(cafe.getCafeName());
            marker.snippet(cafe.fullAddress());
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.dig10k_penguin));
            mMap.addMarker(marker);
        }
        sendData.sendData(searchedLatitude, searchedLongitude);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "onMapReady : called");

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if(mLocationPermissionsGranted) {

            MapsInitializer.initialize(getContext());

            if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            getCurrentUserLocation();
            Snackbar.make(getActivity().findViewById(android.R.id.content), "User's Current Location", Snackbar.LENGTH_SHORT).show();
            search();
        }
    }

    public void getCurrentUserLocation() {
        Log.i(TAG, "getCurrentUserLocation : called");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.i(TAG, "onComplete: called. Found location");
                            Location currentLocation = (Location) task.getResult();

                            mMap.getUiSettings().setZoomControlsEnabled(true);
                            mMap.setMyLocationEnabled(true);
                            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(latLng).title("Current User Location"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));

                        }else{
                            Log.i(TAG, "onComplete: called. Current location is null");
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Location not found!", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionsResult: called.");

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Log.i(TAG, "onRequestPermissionsResult: permission failed");
                            mLocationPermissionsGranted = false;
                            return;
                        }
                    }
                    Log.i(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    initMap();
                }
            }
        }
    }

    interface SendData {
        void sendData(String arg1, String arg2);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            sendData = (SendData) getActivity();
        }
        catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }
}
