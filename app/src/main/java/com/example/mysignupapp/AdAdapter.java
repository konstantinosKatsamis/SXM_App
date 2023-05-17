package com.example.mysignupapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdAdapter extends RecyclerView.Adapter<AdAdapter.ViewHolder>
{
    private List<HashMap<String, Object>> ads;
    private Context mcontext;

    public AdAdapter(Context context, List<HashMap<String, Object>> ads)
    {
        this.ads = ads;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.home_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        HashMap<String, Object> ad = ads.get(position);

        ArrayList<String> images_views = (ArrayList<String>) ad.get("Images");

        ArrayList<String> switches = (ArrayList<String>) ad.get("Switch");

        if(images_views != null && images_views.size() > 0)
        {
            String preview = images_views.get(0);
            Picasso.get().load(preview).into(holder.image);
        }
        else
        {
            holder.image.setImageResource(R.drawable.no_image_input);
        }

        if(ad.get("Title") != null)
        {
            holder.title.setText(ad.get("Title").toString());
        }
        else
        {
            holder.title.setText("No title");
        }

        if(ad.get("Category") != null)
        {
            holder.category.setText(ad.get("Category").toString());
        }
        else
        {
            holder.category.setText("No category");
        }

        if(ad.get("Price") != null)
        {
            holder.price.setText(ad.get("Price").toString());
        }
        else
        {
            holder.price.setText("No price");
        }

        if(switches != null && switches.size() > 0)
        {
            holder.switchable.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return ads.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView image;
        TextView title;
        TextView category;
        TextView price;

        ImageView switchable;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            image = itemView.findViewById(R.id.ad_image);
            title = itemView.findViewById(R.id.ad_title_1);
            category = itemView.findViewById(R.id.ad_category);
            price = itemView.findViewById(R.id.ad_price);
            switchable = itemView.findViewById(R.id.ad_switch);
        }
    }
}

