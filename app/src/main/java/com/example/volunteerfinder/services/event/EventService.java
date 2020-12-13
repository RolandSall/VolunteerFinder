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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public void getOrganizationEvents(Organization organization, Consumer<List<Event>> callback) {
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Stream<Event> eventsStream = buildListOfEvents(new EventServiceResponseMapper().getEventList(snapshot)).stream();
                callback.accept(eventsStream.filter(event -> event.getOrganization().getOrganizationId().equals(organization.getOrganizationId())).collect(Collectors.toList()));
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


    @Override
    public void updateEventById(UpdateEventRequest request, Consumer<Event> eventConsumer) {
        dbReference.child(request.getEventId()).setValue(request).addOnCompleteListener(task ->
                eventConsumer.accept(getSingleEventFromUpdate(request)));
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


    private Event getSingleEventFromUpdate(UpdateEventRequest updateEventRequest) {
        return new Event().builder()
                .eventId(updateEventRequest.getEventId())
                .location(updateEventRequest.getLocation())
                .postedDate(updateEventRequest.getPostedDate())
                .eventDate(updateEventRequest.getEventDate())
                .capacity(updateEventRequest.getCapacity())
                .description(updateEventRequest.getDescription())
                .title(updateEventRequest.getTitle())
                .participants(updateEventRequest.getParticipants())
                .organization(updateEventRequest.getOrganization())
                .image(updateEventRequest.getImage())
                .build();
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
                .participants(eventsServiceResponse.getParticipants())
                .organization(eventsServiceResponse.getOrganization())
                .image(eventsServiceResponse.getImage())
                .build();
    }
}