package com.example.volunteerfinder.services.organization;

import com.example.volunteerfinder.models.Organization;
import com.example.volunteerfinder.services.event.EventsServiceResponse;

import java.util.HashMap;

public class OrganizationMapper {

    public Organization buildOrganization(HashMap organizationHashMap) {
        return new Organization().builder()
                .name(organizationHashMap.get("name").toString())
                .webPage(organizationHashMap.get("webPage").toString())
                .organizationId(organizationHashMap.get("organizationId").toString())
                /*.organizationId(organizationHashMap.get("address").toString())*/
                .build();
    }
}
