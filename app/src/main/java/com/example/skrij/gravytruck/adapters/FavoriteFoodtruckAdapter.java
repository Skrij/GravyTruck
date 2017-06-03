package com.example.skrij.gravytruck.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.skrij.gravytruck.R;
import com.example.skrij.gravytruck.informations.FavoriteFoodtruckInformation;

import java.util.Collections;
import java.util.List;

/**
 * Created by skrij on 02/06/2017.
 */

public class FavoriteFoodtruckAdapter extends RecyclerView.Adapter<FavoriteFoodtruckAdapter.ViewHolder> {

    private LayoutInflater inflater;

    public List<FavoriteFoodtruckInformation> data = Collections.emptyList();

    public FavoriteFoodtruckAdapter(Context context, List<FavoriteFoodtruckInformation> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    // Provide a reference to the views for each data item
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView foodtruck_favorite_name;
        //TextView foodtruck_favorite_distance;

        ViewHolder(View itemView) {
            super(itemView);
            foodtruck_favorite_name = (TextView) itemView.findViewById(R.id.favorite_truck_name_textview);
            //foodtruck_favorite_distance = (TextView) itemView.findViewById(R.id.favorite_truck_distance_textview);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FavoriteFoodtruckAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        View view = inflater.inflate(R.layout.row_favorite_foodtruck, parent, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(FavoriteFoodtruckAdapter.ViewHolder holder, int position) {

        FavoriteFoodtruckInformation current = data.get(position);

        holder.foodtruck_favorite_name.setText(current.foodtruck_favorite_name);
        //holder.foodtruck_favorite_distance.setText(current.foodtruck_favorite_distance);
    }

    // Return the size of the dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }
}
