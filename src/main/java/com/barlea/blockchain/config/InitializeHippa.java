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
 * InitializeExceptionalAccess entities used for demonstrating workflow
 * @author L Rick Jones
 */
@Component
public class InitializeHippa implements ApplicationListener<ApplicationStartedEvent> {

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
                "validationId",generateValidationFromCredentials("dfrench","dfPass123"),
                "firstName","D",
                "middleName","",
                "lastName","French",
                "organization", "InsCo");

        Arbiter arbiter = Rest.post("http://localhost:8080/arbiter/add",Arbiter.class,
                "jurisdiction", "self",
                "validationId", generateValidationFromCredentials("juanita","jbPass123"),
                "firstName","Juanita",
                "middleName","T",
                "lastName","Buck");

        Custodian custodian = Rest.post("http://localhost:8080/custodian/add",Custodian.class,
                "name","HealthCo",
                "validationId", generateValidationFromCredentials("healthco","hcPass123"),
                "address1","123 This Place",
                "state","NW",
                "city","Erehwon",
                "zip","12345");

    }
}
