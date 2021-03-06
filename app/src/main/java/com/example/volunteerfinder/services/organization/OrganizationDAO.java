package com.example.volunteerfinder.services.organization;

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
public class OrganizationDAO {
    private String organizationId;
    private String name;
    private String webPage;
    private String address;
    private String password;
    private String email;
}
