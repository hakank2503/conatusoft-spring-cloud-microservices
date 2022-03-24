package com.optimagrowth.license.controller;

import com.optimagrowth.license.model.License;
import com.optimagrowth.license.service.LicenseService;
import com.optimagrowth.license.utils.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value="v1/organisation/{organisationId}/license")
public class LicenseController {

	private static final Logger logger = LoggerFactory.getLogger(LicenseController.class);

	@Autowired
	private LicenseService licenseService;

	@RequestMapping(value="/",method = RequestMethod.GET)
	public List<License> getLicenses( @PathVariable("organisationId") String organisationId) throws TimeoutException {
		logger.debug("LicenseServiceController Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
		return licenseService.getLicensesByOrganisation(organisationId);
	}

	@RequestMapping(value="/{licenseId}",method = RequestMethod.GET)
	public ResponseEntity<License> getLicense( @PathVariable("organisationId") String organisationId,
			@PathVariable("licenseId") String licenseId) {
		
		License license = licenseService.getLicense(licenseId, organisationId, "");
		license.add( 
				linkTo(methodOn(LicenseController.class).getLicense(organisationId, license.getLicenseId())).withSelfRel(),
				linkTo(methodOn(LicenseController.class).createLicense(license)).withRel("createLicense"),
				linkTo(methodOn(LicenseController.class).updateLicense(license)).withRel("updateLicense"),
				linkTo(methodOn(LicenseController.class).deleteLicense(license.getLicenseId())).withRel("deleteLicense")
		);
		
		return ResponseEntity.ok(license);
	}

	@RequestMapping(value="/{licenseId}/{clientType}",method = RequestMethod.GET)
	public License getLicensesWithClient( @PathVariable("organisationId") String organisationId,
										  @PathVariable("licenseId") String licenseId,
										  @PathVariable("clientType") String clientType) {

		return licenseService.getLicense(licenseId, organisationId, clientType);
	}

	@PutMapping
	public ResponseEntity<License> updateLicense(@RequestBody License request) {
		return ResponseEntity.ok(licenseService.updateLicense(request));
	}
	
	@PostMapping
	public ResponseEntity<License> createLicense(@RequestBody License request) {
		return ResponseEntity.ok(licenseService.createLicense(request));
	}

	@DeleteMapping(value="/{licenseId}")
	public ResponseEntity<String> deleteLicense(@PathVariable("licenseId") String licenseId) {
		return ResponseEntity.ok(licenseService.deleteLicense(licenseId));
	}
}
