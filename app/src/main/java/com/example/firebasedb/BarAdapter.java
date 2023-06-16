package com.example.firebasedb;

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BarAdapter extends RecyclerView.Adapter<BarAdapter.BarViewHolder>{


    Context context;
    ArrayList<Reviews> list;

    public BarAdapter(Context context, ArrayList<Reviews> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new BarViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BarViewHolder holder, int position) {

        Reviews bar = list.get(position);
        holder.barname.setText(bar.getBeer_name());
        holder.barrating.setText(String.valueOf(bar.getBeer_rating()));

        // Load the image from string using Glide
        String image = bar.getBeer_photo();
        // Convert the base64 string to a byte array
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        // Load the byte array into Glide
        Glide.with(context).asBitmap().load(decodedString).into(holder.barphoto);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class BarViewHolder extends RecyclerView.ViewHolder{

        TextView barname, barrating;
        ImageView barphoto;

        public BarViewHolder(@NonNull View itemView) {
            super(itemView);

            barname = itemView.findViewById(R.id.it_barname);
            barrating = itemView.findViewById(R.id.it_barrating);
            barphoto = itemView.findViewById(R.id.it_barphoto);

        }
    }
}
