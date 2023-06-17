package com.example.firebasedb;

import android.content.Context;
import android.content.Intent;
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

public class EventFunctions extends RecyclerView.Adapter<EventFunctions.BarViewHolder>{


    Context context;
    ArrayList<Events> list;

    public EventFunctions(Context context, ArrayList<Events> list) {
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

        Events events = list.get(position);
        holder.eventname.setText(events.getEvent_name());
        holder.eventage.setText(String.valueOf(events.getEvent_age()));

        // Load the image from string using Glide
        String image = events.getEvent_image();
        // Convert the base64 string to a byte array
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        // Load the byte array into Glide
        Glide.with(context).asBitmap().load(decodedString).into(holder.eventinamge);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EventDetail.class);

                // Pass the data to BarDetailActivity using intent extras
                intent.putExtra("EVENT_NAME", events.getEvent_name());
                intent.putExtra("EVENT_DESCRIPTION", events.getEvent_description());
                intent.putExtra("EVENT_AGE", events.getEvent_age());

                v.getContext().startActivity(intent);

//                // Move to new activity
//                v.getContext().startActivity(new Intent(v.getContext(), BarDetailActivity.class));
//                Intent intent = new Intent();
//                intent.putExtra("BAR_NAME", bar.getBeer_name());
//                intent.putExtra("BAR_RATING", bar.getBeer_rating());

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class BarViewHolder extends RecyclerView.ViewHolder{

        TextView eventname, eventage;
        ImageView eventinamge;

        public BarViewHolder(@NonNull View itemView) {
            super(itemView);

            eventname = itemView.findViewById(R.id.it_barname);
            eventage = itemView.findViewById(R.id.it_barrating);
            eventinamge = itemView.findViewById(R.id.it_barphoto);

        }
    }
}
