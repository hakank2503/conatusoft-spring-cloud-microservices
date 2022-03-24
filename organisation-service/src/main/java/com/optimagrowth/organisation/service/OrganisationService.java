package com.optimagrowth.organisation.service;

import com.optimagrowth.organisation.model.Organisation;
import com.optimagrowth.organisation.repository.OrganisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrganisationService {
	
    @Autowired
    private OrganisationRepository repository;

    public Organisation findById(String organisationId) {
    	Optional<Organisation> opt = repository.findById(organisationId);
        return (opt.isPresent()) ? opt.get() : null;
    }

    public Organisation create(Organisation organisation){
    	organisation.setId( UUID.randomUUID().toString());
        organisation = repository.save(organisation);
        return organisation;

    }

    public void update(Organisation organisation){
    	repository.save(organisation);
    }

    public void delete(Organisation organisation){
    	repository.deleteById(organisation.getId());
    }
}