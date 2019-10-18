package com.nrupachitley.cafesearch;

import android.location.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.util.Log;

import androidx.annotation.Nullable;

public class DataService {

    private static final String TAG = "DataService";

    private static final DataService instance = new DataService();

    public static DataService getInstance() {
        return instance;
    }

    public ArrayList<DataModel> getNearByCafes(double latitude, double longitude) {
        ArrayList<DataModel> cafeList = new ArrayList<>();

        // Cafes near house
        cafeList.add(new DataModel("China Live", "644 Broadway", "San Francisco", "CA", "94133", 4.7, 37.79775d, 	-122.40779d));
        cafeList.add(new DataModel("Chaat Corner", "320 3rd St", "San Francisco", "CA", "94107", 3.5, 37.783623d, 	-122.39876d));
        cafeList.add(new DataModel("Amber", "25 Yerba Buena Ln", "San Francisco", "CA", "94103", 4.5, 37.78546d, 	-122.40391d));
        cafeList.add(new DataModel("Osha Thai", "4 Embarcadero Center", "San Francisco", "CA", "94111", 4.3,37.7953d,-122.396179d));
        cafeList.add(new DataModel("Hakkasan", "1 Kearny St", "San Francisco", "CA", "94108", 4.4,	37.78775d,-122.40347d));

        // Cafes near SJSU
        cafeList.add(new DataModel("Phil's Cafe", "118 Paseo De San Antonio Walk", "San Jose", "CA", "95112", 4.1, 37.33362d, -121.88556d));
        cafeList.add(new DataModel("La Victoria Taqueria", "140 E San Carlos St", "San Jose", "CA", "95112", 3.9, 	37.3327d, -121.88443d));
        cafeList.add(new DataModel("Back a Yard", "80 N Market St", "San Jose", "CA", "95113", 4.4, 	37.33658d, -121.8929d));
        cafeList.add(new DataModel("City Fish", "30 E Santa Clara St", "San Jose", "CA", "95113", 4.2, 	37.3365d, 	-121.88994d));
        cafeList.add(new DataModel("San Carlos Italian Pizza", "484 E San Carlos St", "San Jose", "CA", "95112", 4.7, 37.33635d, -121.8768d));
        cafeList.add(new DataModel("On Fourth", "150 E San Fernando St", "San Jose", "CA", "95112", 3.7, 	37.336116d, -121.885358d));

        return cafeList;
    }

    // Gets nearby cafes
    public ArrayList<DataModel> shortListLocations(double latitude, double longitude, ArrayList<DataModel> cafeList) {
        Location targetLocation = new Location("");
        targetLocation.setLatitude(latitude);
        targetLocation.setLongitude(longitude);
        ArrayList<DataModel> nearbyCafes = new ArrayList<>();
        for(int i = 0; i < cafeList.size(); i++) {
            double lat = cafeList.get(i).getLatitude();
            double lng = cafeList.get(i).getLongitude();
            float[] results = new float[1];
            Location.distanceBetween(latitude, longitude, lat, lng, results);
            float result = 0;
            if(results != null){
                result = results[0];
            }
            double distance=(double)result;
//            Log.i(TAG, "distance: " + String.valueOf(distance));
            if(distance < 16093.4) {
                nearbyCafes.add(cafeList.get(i));
            }
        }
        return nearbyCafes;
    }

}
