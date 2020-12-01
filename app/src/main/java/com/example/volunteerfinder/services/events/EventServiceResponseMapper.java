package com.example.volunteerfinder.services.events;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;


public class EventServiceResponseMapper {

       public ArrayList<EventsServiceResponse> getEventList(DataSnapshot snapshot) {
        ArrayList<EventsServiceResponse> eventsArrayList = new ArrayList<>();
        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

            HashMap eventHashMap = (HashMap) dataSnapshot.getValue();
            EventsServiceResponse response = getBuild(eventHashMap);
            eventsArrayList.add(response);
        }

        return eventsArrayList;
    }

    private EventsServiceResponse getBuild(HashMap eventHashMap) {
        return new EventsServiceResponse().builder()
                .eventId(eventHashMap.get("eventId").toString())
                .description(eventHashMap.get("description").toString())
                .eventDate(eventHashMap.get("eventDate").toString())
                .eventDate(eventHashMap.get("eventDate").toString())
                .location(eventHashMap.get("location").toString())
                .postedDate(eventHashMap.get("postedDate").toString())
                .organization(eventHashMap.get("organization").toString())
                .capacity(Integer.parseInt(eventHashMap.get("capacity").toString()))
                .build();
    }
}
