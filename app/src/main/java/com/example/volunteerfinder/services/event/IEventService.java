package com.example.volunteerfinder.services.event;

import com.example.volunteerfinder.models.Event;

import java.util.List;
import java.util.function.Consumer;

public interface IEventService {

    List<Event> getEvents();

    void getEventsAsync(Consumer<List<Event>> callback);

    Event getSingleEvent(String id);

    void saveEvent(Event event);

}
