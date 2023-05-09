package com.example.mysignupapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdAdapter extends RecyclerView.Adapter<AdAdapter.ViewHolder>
{
    List<String> titles;
    List<Integer> images;
    LayoutInflater inflater;

    public AdAdapter(Context context, List<String> titles, List<Integer> images)
    {
        this.titles = titles;
        this.images = images;
        this.inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.home_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.title.setText(titles.get(position));
        holder.image.setImageResource(images.get(position));

    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        ImageView image;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            title = itemView.findViewById(R.id.ad_title_1);
            image = itemView.findViewById(R.id.ad_button_1);
        }
    }
}

