package com.example.volunteerfinder.services.event;

import com.example.volunteerfinder.models.Event;

import java.util.List;
import java.util.function.Consumer;

public interface IEventService {

    void getEvents(Consumer<List<Event>> callback);

    void saveEvent(Event event);

    void   deleteEvent(String eventId);

}
