package com.optimagrowth.organisation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@RefreshScope
public class OrganisationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrganisationServiceApplication.class, args);
    }

}
