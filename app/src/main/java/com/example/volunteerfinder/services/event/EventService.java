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
        // Creating a dummy Event
        Event dummyEvent = createDummyEvent(uuid);
        dbReference.child(uuid.toString()).setValue(dummyEvent);
    }

    @Override
    public void deleteEvent(String eventId) {
        dbReference.child(eventId).removeValue();
    }

    private Event createDummyEvent(UUID uuid) {
        ArrayList participants = new ArrayList();
        participants.add("1");
        participants.add("2");
        participants.add("3");
        return Event.builder()
                .eventId(uuid.toString())
                .capacity(10)
                .organization(new Organization().builder()
                        .organizationId(UUID.randomUUID().toString())
                        .webPage("https://www.lau.edu.lb/")
                        .name("Dummy Organization")
                        .build())
                .description("Help Students")
                .location("Hamra")
                .title("Event Title")
                .image("https://firebasestorage.googleapis.com/v0/b/volunteerfinder-8df78.appspot.com/o/Events%2F6ccdec15-2343-4666-af38-8fd664e39171%2Fmlops-1.png?alt=media&token=ab1513f4-e874-4b01-92d3-4e71b361e86e")
                .eventDate("12-25-2020")
                .postedDate("11-30-2020")
                .participants(participants)
                .build();
    }

    public Event createDummyEvent(Organization organization) {
        UUID uuid = UUID.randomUUID();
        ArrayList participants = new ArrayList();
        participants.add("1");
        participants.add("2");
        participants.add("3");
        return Event.builder()
                .eventId(uuid.toString())
                .capacity(10)
                .organization(organization)
                .description("Help Students")
                .location("Hamra")
                .title("Event Title")
                .image("https://firebasestorage.googleapis.com/v0/b/volunteerfinder-8df78.appspot.com/o/Events%2F6ccdec15-2343-4666-af38-8fd664e39171%2Fmlops-1.png?alt=media&token=ab1513f4-e874-4b01-92d3-4e71b361e86e")
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
                .image(eventsServiceResponse.getImage())
                .build();
    }
}