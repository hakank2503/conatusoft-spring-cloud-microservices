package com.optimagrowth.license.service.client;


import com.optimagrowth.license.model.Organisation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("organisation-service")
public interface OrganisationFeignClient {
    @RequestMapping(
            method= RequestMethod.GET,
            value="/v1/organisation/{organisationId}",
            consumes="application/json")
    Organisation getOrganisation(@PathVariable("organisationId") String organisationId);
}
