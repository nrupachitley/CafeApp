package com.nrupachitley.cafesearch;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    private TextView cafeName;
    private TextView cafeRating;
    private TextView cafeAddress;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        cafeName = (TextView) itemView.findViewById(R.id.cafeName);
        cafeRating = (TextView) itemView.findViewById(R.id.cafeRating);
        cafeAddress = (TextView) itemView.findViewById(R.id.cafeAddress);
    }

    public void updateUI(DataModel cafe) {
        cafeName.setText(cafe.getCafeName());
        cafeRating.setText(String.valueOf(cafe.getRating()));
        cafeAddress.setText(cafe.fullAddress());
    }
}
