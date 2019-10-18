package com.nrupachitley.cafesearch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import android.util.Log;

public class LocationRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final String TAG = "LocationRecyclerViewAda";

    private ArrayList<DataModel> nearbyCafes;

    public LocationRecyclerViewAdapter(ArrayList<DataModel> nearbyCafes) {
        this.nearbyCafes = nearbyCafes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: called.");

        final DataModel cafe = nearbyCafes.get(position);
        holder.updateUI(cafe);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Make Toast Notification here
            }
        });
    }

    @Override
    public int getItemCount() {
        return nearbyCafes.size();
    }
}
