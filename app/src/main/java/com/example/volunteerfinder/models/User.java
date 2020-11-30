package com.example.volunteerfinder.models;


import java.io.Serializable;

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
public class User implements Serializable {
    private String userId;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String password;
}
