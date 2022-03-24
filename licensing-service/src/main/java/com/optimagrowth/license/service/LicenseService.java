package com.optimagrowth.license.service;

import com.optimagrowth.license.config.ServiceConfig;
import com.optimagrowth.license.model.License;
import com.optimagrowth.license.model.Organisation;
import com.optimagrowth.license.repository.LicenseRepository;
import com.optimagrowth.license.service.client.OrganisationDiscoveryClient;
import com.optimagrowth.license.service.client.OrganisationFeignClient;
import com.optimagrowth.license.service.client.OrganisationRestTemplateClient;
import com.optimagrowth.license.utils.UserContextHolder;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import static io.github.resilience4j.bulkhead.annotation.Bulkhead.Type.THREADPOOL;

@Service
public class LicenseService {

	@Autowired
	MessageSource messages;

	@Autowired
	private LicenseRepository licenseRepository;

	@Autowired
	ServiceConfig config;

	@Autowired
	OrganisationFeignClient organisationFeignClient;

	@Autowired
	OrganisationRestTemplateClient organisationRestClient;

	@Autowired
	OrganisationDiscoveryClient organisationDiscoveryClient;

	private static final Logger logger = LoggerFactory.getLogger(LicenseService.class);

	public License getLicense(String licenseId, String organisationId, String clientType){
		License license = licenseRepository.findByOrganisationIdAndLicenseId(organisationId, licenseId);
		if (null == license) {
			throw new IllegalArgumentException(String.format(messages.getMessage("license.search.error.message", null, null),licenseId, organisationId));
		}
		Organisation organisation = retrieveOrganisationInfo(organisationId, clientType);
		if (null != organisation) {
			license.setOrganisationName(organisation.getName());
			license.setContactName(organisation.getContactName());
			license.setContactEmail(organisation.getContactEmail());
			license.setContactPhone(organisation.getContactPhone());
		}

		return license.withComment(config.getProperty());
	}

	public License createLicense(License license){
		license.setLicenseId(UUID.randomUUID().toString());
		licenseRepository.save(license);

		return license.withComment(config.getProperty());
	}

	public License updateLicense(License license){
		licenseRepository.save(license);

		return license.withComment(config.getProperty());
	}

	public String deleteLicense(String licenseId){
		String responseMessage = null;
		License license = new License();
		license.setLicenseId(licenseId);
		licenseRepository.delete(license);
		responseMessage = String.format(messages.getMessage("license.delete.message", null, null),licenseId);
		return responseMessage;

	}

	@CircuitBreaker(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
	@RateLimiter(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
	@Retry(name = "retryLicenseService", fallbackMethod = "buildFallbackLicenseList")
	@Bulkhead(name = "bulkheadLicenseService", type= THREADPOOL, fallbackMethod = "buildFallbackLicenseList")
	public List<License> getLicensesByOrganisation(String organisationId) throws TimeoutException {
		logger.debug("getLicensesByOrganisation Correlation id: {}",
				UserContextHolder.getContext().getCorrelationId());
		randomlyRunLong();
		return licenseRepository.findByOrganisationId(organisationId);
	}

	@SuppressWarnings("unused")
	private List<License> buildFallbackLicenseList(String organisationId, Throwable t){
		List<License> fallbackList = new ArrayList<>();
		License license = new License();
		license.setLicenseId("0000000-00-00000");
		license.setOrganisationId(organisationId);
		license.setProductName("Sorry no licensing information currently available");
		fallbackList.add(license);
		return fallbackList;
	}

	private void randomlyRunLong() throws TimeoutException{
		Random rand = new Random();
		int randomNum = rand.nextInt((3 - 1) + 1) + 1;
		if (randomNum==3) sleep();
	}
	private void sleep() throws TimeoutException{
		try {
			System.out.println("Sleep");
			Thread.sleep(5000);
			throw new java.util.concurrent.TimeoutException();
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}
	}

	private Organisation retrieveOrganisationInfo(String organisationId, String clientType) {
		Organisation organisation = null;

		switch (clientType) {
			case "feign":
				System.out.println("I am using the feign client");
				organisation = organisationFeignClient.getOrganisation(organisationId);
				break;
			case "rest":
				System.out.println("I am using the rest client");
				organisation = organisationRestClient.getOrganisation(organisationId);
				break;
			case "discovery":
				System.out.println("I am using the discovery client");
				organisation = organisationDiscoveryClient.getOrganisation(organisationId);
				break;
			default:
				organisation = organisationRestClient.getOrganisation(organisationId);
				break;
		}

		return organisation;
	}
}
