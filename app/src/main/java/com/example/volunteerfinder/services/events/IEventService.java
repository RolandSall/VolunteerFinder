package com.example.volunteerfinder.services.events;

import com.example.volunteerfinder.models.Events;

public interface IEventService {

    void getEvents();

    void saveEvent(Events event);

}
