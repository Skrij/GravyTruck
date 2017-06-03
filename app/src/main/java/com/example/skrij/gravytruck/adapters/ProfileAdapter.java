package com.example.skrij.gravytruck.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skrij.gravytruck.AddFoodtruckActivity;
import com.example.skrij.gravytruck.FavoriteFoodtruckActivity;
import com.example.skrij.gravytruck.R;
import com.example.skrij.gravytruck.StartActivity;
import com.example.skrij.gravytruck.helpers.SystemPrefsHelper;
import com.example.skrij.gravytruck.informations.ProfileInformation;

import java.util.Collections;
import java.util.List;

/**
 * Created by skrij on 01/06/2017.
 */

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private OnItemClickListener listener;
    public List<ProfileInformation> data = Collections.emptyList();

    public ProfileAdapter(Context context, List<ProfileInformation> data, OnItemClickListener listener) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ProfileInformation item);
    }

    // Provide a reference to the views for each data item
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title_profile_menu;
        public final Context context;
        Intent intent;

        ViewHolder(View itemView) {
            super(itemView);
            title_profile_menu = (TextView) itemView.findViewById(R.id.item_profile_menu_textview);
            context = itemView.getContext();
        }

        void bind(final ProfileInformation item, final OnItemClickListener listener) {
            title_profile_menu.setText(item.title_profile_menu);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position == 0) {
                        intent = new Intent(context, AddFoodtruckActivity.class);
                        context.startActivity(intent);
                    } else if (position == 1) {
                        intent = new Intent(context, FavoriteFoodtruckActivity.class);
                        context.startActivity(intent);
                    } else if (position == 2) {
                        Toast.makeText(context, "Soon on the google store ! o/", Toast.LENGTH_SHORT).show();
                    } else {
                        SystemPrefsHelper.getInstance(context).clearData();
                        intent = new Intent(context, StartActivity.class);
                        Toast.makeText(context, "See you soon o/", Toast.LENGTH_SHORT).show();
                        context.startActivity(intent);
                    }
                }

            });

        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        View view = inflater.inflate(R.layout.row_profile, parent, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ProfileAdapter.ViewHolder holder, int position) {
        holder.bind(data.get(position), listener);
    }

    // Return the size of the dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }
}
