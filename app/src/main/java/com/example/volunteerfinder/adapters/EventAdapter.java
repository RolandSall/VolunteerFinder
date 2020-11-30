package com.example.volunteerfinder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.volunteerfinder.R;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventCardViewHolder> {
    Context context;
    ArrayList<String> list;
    public class EventCardViewHolder extends RecyclerView.ViewHolder {
        public TextView eventName;
        public EventCardViewHolder(View view) {
            super(view);
            eventName = view.findViewById(R.id.eventName);
        }
    }
    public EventAdapter(Context context, ArrayList<String> list) {
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
        myViewHolder.eventName.setText(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
