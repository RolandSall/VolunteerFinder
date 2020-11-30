package com.example.volunteerfinder.Services.User;

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
public class UserServiceResponse {
    private String userId;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
}
