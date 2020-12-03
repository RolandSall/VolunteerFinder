package com.example.volunteerfinder.services.event;

import androidx.annotation.NonNull;

import com.example.volunteerfinder.models.Event;
import com.example.volunteerfinder.models.Organization;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class EventService implements IEventService {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbReference = database.getReference().child("Events");

    @Override
    public void getEvents(Consumer<List<Event>> callback) {
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.accept(buildListOfEvents(new EventServiceResponseMapper().getEventList(snapshot)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void saveEvent(Event event) {
        UUID uuid = UUID.randomUUID();
        // create Id
        event.setEventId(uuid.toString());
        // initially no participants
        event.setParticipants(new ArrayList<>());
        // Save event
        dbReference.child(uuid.toString()).setValue(event);
    }


    @Override
    public void deleteEvent(String eventId) {
        dbReference.child(eventId).removeValue();
    }


    private List<Event> buildListOfEvents(List<EventsServiceResponse> eventsServiceResponses) {
        while (eventsServiceResponses.equals(null)){
            System.out.println("No Data To Be Shown");
        }
        List<Event> responseList = new ArrayList<>();
        for (EventsServiceResponse eventsServiceResponse : eventsServiceResponses) {
            responseList.add(getSingleEvent(eventsServiceResponse));
        }
        return responseList;
    }

    private Event getSingleEvent(EventsServiceResponse eventsServiceResponse) {
        return new Event().builder()
                .eventId(eventsServiceResponse.getEventId())
                .location(eventsServiceResponse.getLocation())
                .postedDate(eventsServiceResponse.getPostedDate())
                .eventDate(eventsServiceResponse.getEventDate())
                .capacity(eventsServiceResponse.getCapacity())
                .description(eventsServiceResponse.getDescription())
                .title(eventsServiceResponse.getTitle())
                .organization(eventsServiceResponse.getOrganization())
                .image(eventsServiceResponse.getImage())
                .build();
    }
}