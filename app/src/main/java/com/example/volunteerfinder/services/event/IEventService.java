package com.example.volunteerfinder.services.event;

import com.example.volunteerfinder.models.Event;

import java.util.List;

public interface IEventService {

    List<Event> getEvents();

    void saveEvent(Event event);

}
