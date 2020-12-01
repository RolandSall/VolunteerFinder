package com.example.volunteerfinder.services.event;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class EventsServiceResponse {
    private String eventId;
    private String organization;
    private String title;
    private String description;
    private String location;
    private String postedDate;
    private String eventDate;
    private int capacity;
    private ArrayList<String> participants;
}
