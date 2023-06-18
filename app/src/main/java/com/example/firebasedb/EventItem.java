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

public class EventItem extends RecyclerView.Adapter<EventItem.EventHolder>{


    Context context;
    ArrayList<Events> list;

    public EventItem(Context context, ArrayList<Events> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_item, parent, false);
        return new EventHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventHolder holder, int position) {

        Events events = list.get(position);
        holder.eventname.setText(events.getEvent_name());
        holder.eventdescription.setText(String.valueOf(events.getEvent_description()));

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
                intent.putExtra("EVENT_TYPE", events.getEvent_type());


                v.getContext().startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class EventHolder extends RecyclerView.ViewHolder{

        TextView eventname, eventdescription;
        ImageView eventinamge;

        public EventHolder(@NonNull View itemView) {
            super(itemView);

            eventname = itemView.findViewById(R.id.event_name);
            eventdescription = itemView.findViewById(R.id.event_description);
            eventinamge = itemView.findViewById(R.id.eventImage);

        }
    }
}
