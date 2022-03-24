package com.optimagrowth.license.service.client;

import com.optimagrowth.license.model.Organisation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrganisationRestTemplateClient {
    @Autowired
    RestTemplate restTemplate;

    public Organisation getOrganisation(String organisationId){
        ResponseEntity<Organisation> restExchange =
                restTemplate.exchange(
                        "http://organisation-service/v1/organisation/{organisationId}",
                        HttpMethod.GET,
                        null, Organisation.class, organisationId);

        return restExchange.getBody();
    }
}
