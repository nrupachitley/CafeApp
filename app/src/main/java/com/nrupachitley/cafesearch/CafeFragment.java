package com.nrupachitley.cafesearch;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

import java.util.ArrayList;
import java.util.Comparator;


/**
 * A simple {@link Fragment} subclass.
 */
public class CafeFragment extends Fragment {

    private static final String TAG = "CafeFragment";

    private double latitude;
    private double longitude;
    private  RecyclerView recyclerView;
    private LocationRecyclerViewAdapter locationRecyclerViewAdapter;
    private LinearLayoutManager layoutManager;

    private ArrayList<DataModel> allCafes;

    public CafeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(TAG, "onCreateView: called");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cafe, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        allCafes = DataService.getInstance().getNearByCafes(0.0, 0.0);
        locationRecyclerViewAdapter = new LocationRecyclerViewAdapter(allCafes);
        recyclerView.setAdapter(locationRecyclerViewAdapter);

        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    protected void displayReceivedData(String arg1, String arg2) {
        Log.i(TAG, "displayReceivedData: called");
//        Log.i(TAG, "latitude: " + arg1);

        latitude = Double.valueOf(arg1);
        longitude = Double.valueOf(arg2);

        ArrayList<DataModel> nearbyCafes = DataService.getInstance().shortListLocations(latitude, longitude, allCafes);
        nearbyCafes.sort(new Comparator<DataModel>() {
            @Override
            public int compare(DataModel dataModel, DataModel t1) {
                return -Double.compare(dataModel.getRating(), t1.getRating());
            }
        });
        allCafes.clear();
        allCafes.addAll(nearbyCafes);
        locationRecyclerViewAdapter.notifyDataSetChanged();
    }

}
