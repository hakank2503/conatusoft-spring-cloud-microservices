package com.optimagrowth.license.service.client;

import com.optimagrowth.license.model.Organisation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class OrganisationDiscoveryClient {

    @Autowired
    private DiscoveryClient discoveryClient;

    public Organisation getOrganisation(String organisationId) {
        RestTemplate restTemplate = new RestTemplate();
        List<ServiceInstance> instances = discoveryClient.getInstances("organisation-service");

        if (instances.size()==0) return null;
        String serviceUri = String.format("%s/v1/organisation/%s",instances.get(0).getUri().toString(), organisationId);
    
        ResponseEntity< Organisation > restExchange =
                restTemplate.exchange(
                        serviceUri,
                        HttpMethod.GET,
                        null, Organisation.class, organisationId);

        return restExchange.getBody();
    }
}
