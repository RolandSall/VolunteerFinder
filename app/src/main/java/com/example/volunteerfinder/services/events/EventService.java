package com.example.volunteerfinder.services.events;

import androidx.annotation.NonNull;

import com.example.volunteerfinder.models.Events;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class EventService implements IEventService {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference().child("Events");
    private ArrayList<EventsServiceResponse> eventsServiceResponses;

    public EventService() {
    }


    public ArrayList<EventsServiceResponse> getEventsServiceResponses() {
        return eventsServiceResponses;
    }

    public void setEventsServiceResponses(ArrayList<EventsServiceResponse> eventsServiceResponses) {
        this.eventsServiceResponses = eventsServiceResponses;
    }

    @Override

    public void getEvents() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventsServiceResponses = new EventServiceResponseMapper().getEventList(snapshot);
                setEventsServiceResponses(eventsServiceResponses);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public void saveEvent(Events event) {
        UUID uuid = UUID.randomUUID();
        // Creating a dummy Event
        Events dummyEvent = createDummyEvent(uuid);
        reference.child(uuid.toString()).setValue(dummyEvent);
    }

    private Events createDummyEvent(UUID uuid) {
        ArrayList participants = new ArrayList();
        participants.add("1");
        participants.add("2");
        participants.add("3");
        return Events.builder()
                .eventId(uuid.toString())
                .organization("LAU")
                .capacity(10)
                .description("Help Students")
                .location("Hamra")
                .title("Event Title")
                .eventDate("12-25-2020")
                .postedDate("11-30-2020")
                .participants(participants)
                .build();
    }
}
