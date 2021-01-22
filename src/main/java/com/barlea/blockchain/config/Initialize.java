package com.barlea.blockchain.config;

import com.barlea.blockchain.entities.*;
import com.barlea.blockchain.service.Hasher;
import com.barlea.blockchain.service.Rest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Initialize entities used for demonstrating workflow
 * @author L Rick Jones
 */
@Component
public class Initialize implements ApplicationListener<ApplicationStartedEvent> {

    static private String generateValidationFromCredentials(String userName, String password) {
        Credentials creds = Credentials.builder().userName(userName).password(password).build();
        try {
            return Hasher.hash(creds.toString());
        } catch (JsonProcessingException e) {
            return "Invalid";
        }
    }

    @Override
    public void onApplicationEvent(@NotNull ApplicationStartedEvent event) {

        /*Applicant applicant =*/ Rest.post("http://localhost:8080/applicant/add",Applicant.class,
                "requestType","test",
                "validationId",generateValidationFromCredentials("johndoe","myPass123"),
                "firstName","John",
                "middleName","J",
                "lastName","Doe");

        Arbiter arbiter = Rest.post("http://localhost:8080/arbiter/add",Arbiter.class,
                "jurisdiction", "West Side",
                "validationId", generateValidationFromCredentials("thejudge","tjPass123"),
                "firstName","Wyatt",
                "middleName","P",
                "lastName","Earp");

        Custodian custodian = Rest.post("http://localhost:8080/custodian/add",Custodian.class,
                "name","MyFace",
                "validationId", generateValidationFromCredentials("myface","mfPass123"),
                "address1","123 This Place",
                "state","NW",
                "city","Erehwon",
                "zip","12345");

        Authority authority = Rest.post("http://localhost:8080/authority/add",Authority.class,
                "arbiterId", arbiter.getUuid(),
                "custodianId", custodian.getUuid(),
                "validationId",generateValidationFromCredentials("federalcourt","fedPass123"),
                "authorityType","Warrant",
                "documentId","Case-12345678-RNM",
                "description","Access to contacts made over last 6 months",
                "homeDomicile.address1","123 East W Street",
                "homeDomicile.state", "NM",
                "homeDomicile.city", "Roswell",
                "homeDomicile.zip", "88201",
                "name.firstName","Joe",
                "name.middleName","D",
                "name.lastName","Smith",
                "birthDate","07/04/1960",
                "homePhone","555-667-8309");

        /* Contract contract = */ Rest.post("http://localhost:8080/contract/create",Authority.class,
                "contractId", UUID.randomUUID().toString(),
                "authorityId", authority.getUuid());



    }
}
