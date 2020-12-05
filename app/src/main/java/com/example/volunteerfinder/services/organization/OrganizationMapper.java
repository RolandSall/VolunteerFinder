package com.example.volunteerfinder.services.organization;

import com.example.volunteerfinder.models.Organization;
import com.example.volunteerfinder.models.User;
import com.example.volunteerfinder.services.event.EventsServiceResponse;

import java.util.HashMap;

public class OrganizationMapper {

    public Organization getOrganization(HashMap organizationHashMap) {
        return new Organization().builder()
                .name(organizationHashMap.get("name").toString())
                .webPage(organizationHashMap.get("webPage").toString())
                .organizationId(organizationHashMap.get("organizationId").toString())
                /*.organizationId(organizationHashMap.get("address").toString())*/
                .build();
    }

    public Organization getOrganizationById(String organizationId, HashMap organizationDAO) {
        return new Organization().builder()




                .build();
    }

    public OrganizationDAO getOrganizationDAOById(String organizationId, HashMap organizationHashMap) {
        return new OrganizationDAO().builder()
                .name(organizationHashMap.get("name").toString())
                .webPage(organizationHashMap.get("webPage").toString())
                .organizationId(organizationId)
                .email(organizationHashMap.get("email").toString())
                .address(organizationHashMap.get("address").toString())
                .password(organizationHashMap.get("password").toString())
                .build();

    }
}
