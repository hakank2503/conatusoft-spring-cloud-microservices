package com.optimagrowth.organisation.repository;

import com.optimagrowth.organisation.model.Organisation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganisationRepository extends CrudRepository<Organisation,String>  {
    public Optional<Organisation> findById(String organisationId);
}
