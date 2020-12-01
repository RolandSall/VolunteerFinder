package com.example.volunteerfinder.services.event;

import androidx.annotation.NonNull;

import com.example.volunteerfinder.models.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventService implements IEventService {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference().child("Events");
    private List<Event> eventList;

    public EventService() {
        eventList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventList = buildListOfEvents(new EventServiceResponseMapper().getEventList(snapshot));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public List<Event> getEvents() {
        return eventList;
    }

    @Override
    public Event getSingleEvent(String id) {
        return eventList.stream().filter(e-> e.getEventId().equals(id)).findFirst().get();
    }

    @Override
    public void saveEvent(Event event) {
        UUID uuid = UUID.randomUUID();
        // Creating a dummy Event
        Event dummyEvent = createDummyEvent(uuid);
        reference.child(uuid.toString()).setValue(dummyEvent);
    }

    private Event createDummyEvent(UUID uuid) {
        ArrayList participants = new ArrayList();
        participants.add("1");
        participants.add("2");
        participants.add("3");
        return Event.builder()
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
                .build();
    }
}
