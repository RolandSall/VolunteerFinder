package com.example.volunteerfinder.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.volunteerfinder.R;
import com.example.volunteerfinder.activities.FeedActivity;
import com.example.volunteerfinder.models.Event;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventCardViewHolder> {

    Context context;
    ArrayList<Event> list;
    private OnCardListener onCardListener;

    public class EventCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView card;
        public TextView eventName;
        public TextView eventOrganization;
        public TextView eventDate;
        public ImageView eventImage;
        OnCardListener onCardListener;

        public EventCardViewHolder(View view, OnCardListener onCardListener) {
            super(view);
            eventName = view.findViewById(R.id.eventName);
            eventOrganization = view.findViewById(R.id.eventOrganization);
            eventDate = view.findViewById(R.id.eventDate);
            eventImage = view.findViewById(R.id.eventImage);
            this.onCardListener = onCardListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCardListener.onCardClick(getAdapterPosition());
        }
    }


    public EventAdapter(Context context, ArrayList<Event> list, OnCardListener onCardListener) {
        this.context = context;
        this.list = list;
        this.onCardListener = onCardListener;
    }

    @NonNull
    @Override
    public EventCardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_card, viewGroup, false);
        return new EventCardViewHolder(itemView, onCardListener);
    }

    @Override
    public void onBindViewHolder(@NonNull EventCardViewHolder myViewHolder, int i) {
        Event event = list.get(i);
        myViewHolder.eventName.setText(event.getTitle());
        myViewHolder.eventOrganization.setText(event.getOrganization().getName());
        myViewHolder.eventDate.setText(event.getEventDate());
        Picasso.get().load(event.getImage()).into(myViewHolder.eventImage);
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

    public interface OnCardListener{
        void onCardClick(int position);
    }
}
