package com.example.skrij.gravytruck.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.skrij.gravytruck.R;
import com.example.skrij.gravytruck.informations.CommentInformation;

import java.util.Collections;
import java.util.List;

/**
 * Created by skrij on 28/05/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private LayoutInflater inflater;

    public List<CommentInformation> data = Collections.emptyList();

    public CommentAdapter(Context context, List<CommentInformation> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    // Provide a reference to the views for each data item
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView user_name;
        TextView user_comment;

        ViewHolder(View itemView) {
            super(itemView);
            user_name = (TextView) itemView.findViewById(R.id.user_name_textview);
            user_comment = (TextView) itemView.findViewById(R.id.user_comment_textview);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        View view = inflater.inflate(R.layout.row_recommandation, parent, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CommentAdapter.ViewHolder holder, int position) {

        CommentInformation current = data.get(position);

        holder.user_name.setText(current.user_name);
        holder.user_comment.setText(current.user_comment);
    }

    // Return the size of the dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }
}
