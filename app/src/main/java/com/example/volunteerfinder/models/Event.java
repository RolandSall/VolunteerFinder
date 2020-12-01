package com.example.volunteerfinder.models;

import java.io.Serializable;
import java.sql.Date;
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
public class Event implements Serializable {
    private String eventId;
    private String organization;
    private String description;
    private String title;
    private String location;
    private String postedDate;
    private String eventDate;
    private int capacity;
    private ArrayList<String> participants;
}
