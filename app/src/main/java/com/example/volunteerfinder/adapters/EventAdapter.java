package com.example.volunteerfinder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.volunteerfinder.R;
import com.example.volunteerfinder.models.Event;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventCardViewHolder> {
    Context context;
    ArrayList<Event> list;
    public class EventCardViewHolder extends RecyclerView.ViewHolder {
        public TextView eventName;
        public TextView eventOrganization;
        public TextView eventDate;
        public EventCardViewHolder(View view) {
            super(view);
            eventName = view.findViewById(R.id.eventName);
            eventOrganization = view.findViewById(R.id.eventOrganization);
            eventDate = view.findViewById(R.id.eventDate);
        }
    }
    public EventAdapter(Context context, ArrayList<Event> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EventCardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_card, viewGroup, false);
        return new EventCardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventCardViewHolder myViewHolder, int i) {
        Event event = list.get(i);
        myViewHolder.eventName.setText(event.getTitle());
        myViewHolder.eventOrganization.setText(event.getOrganization());
        myViewHolder.eventDate.setText(event.getEventDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void update(ArrayList<Event> events){
        list.clear();
        list.addAll(events);
        notifyDataSetChanged();
    }
}
