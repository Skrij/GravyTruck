package com.example.skrij.gravytruck.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.skrij.gravytruck.R;
import com.example.skrij.gravytruck.informations.MenuInformation;

import java.util.Collections;
import java.util.List;

/**
 * Created by skrij on 27/05/2017.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private LayoutInflater inflater;

    public List<MenuInformation> data = Collections.emptyList();

    public MenuAdapter(Context context, List<MenuInformation> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    // Provide a reference to the views for each data item
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView product_name;
        TextView product_price;

        ViewHolder(View itemView) {
            super(itemView);
            product_name = (TextView) itemView.findViewById(R.id.product_name_textview);
            product_price = (TextView) itemView.findViewById(R.id.product_price_textview);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View view = inflater.inflate(R.layout.row_menu, parent, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        MenuInformation current = data.get(position);
        String upperString_name = current.product_name.substring(0, 1).toUpperCase() + current.product_name.substring(1);

        holder.product_name.setText(upperString_name);
        holder.product_price.setText(current.product_price + " €");
    }

    // Return the size of the dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }
}

