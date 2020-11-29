package com.example.volunteerfinder.model;


import java.util.UUID;

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
public class User {
    private String userId;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String password;
}
