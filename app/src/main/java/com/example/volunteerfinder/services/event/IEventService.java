package com.example.volunteerfinder.services.event;

import com.example.volunteerfinder.models.Event;
import com.example.volunteerfinder.models.Organization;

import java.util.List;
import java.util.function.Consumer;

public interface IEventService {

    void getEvents(Consumer<List<Event>> callback);

    void getOrganizationEvents(Organization organization, Consumer<List<Event>> callback);

    void saveEvent(Event event);

    void deleteEvent(String eventId);

    void updateEventById(UpdateEventRequest request, Consumer<Event> eventConsumer);
}
