package com.example.volunteerfinder.services.user;

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
public class RegisterUserResponse {
    private String userId;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
}
