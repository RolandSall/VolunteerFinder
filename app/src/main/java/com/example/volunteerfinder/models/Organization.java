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
public class Organization implements Serializable {
    private String organizationId;
    private String name;
    private String webPage;
    private String address;
    private String email;
}
