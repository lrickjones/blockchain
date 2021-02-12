package com.barlea.blockchain.config;

import com.barlea.blockchain.entities.*;
import com.barlea.blockchain.service.Hasher;
import com.barlea.blockchain.service.Rest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * InitializeExceptionalAccess entities used for demonstrating workflow
 * @author L Rick Jones
 */
@RestController
public class InitializeHippa {

    static private String generateValidationFromCredentials(String userName, String password) {
        Credentials creds = Credentials.builder().userName(userName).password(password).build();
        try {
            return Hasher.hash(creds.toString());
        } catch (JsonProcessingException e) {
            return "Invalid";
        }
    }

    @GetMapping("/hipaa/init")
    public void hipaaInit(){

        Rest.get("http://localhost:8080/blockchain/reset",Void.class);
        Rest.get("http://localhost:8080/applicant/clear",Void.class);
        Rest.get("http://localhost:8080/custodian/clear",Void.class);
        Rest.get("http://localhost:8080/patient/clear",Void.class);

        /*Applicant applicant =*/ Rest.post("http://localhost:8080/applicant/add",Applicant.class,
                "requestType","records",
                "validationId",generateValidationFromCredentials("galli","igPass123"),
                "firstName","Intinge",
                "middleName","",
                "lastName","Galli",
                "organization", "InsQuery");

        Patient patient = Rest.post("http://localhost:8080/patient/add",Patient.class,
                "jurisdiction", "self",
                "validationId", generateValidationFromCredentials("juanita","jbPass123"),
                "firstName","Juanita",
                "middleName","T",
                "lastName","Buck",
                "address1","123 Test Ave",
                "state", "BD",
                "city", "Teston",
                "zip", "12345",
                "month", "6",
                "day", "15",
                "year","1972");

        Custodian custodian = Rest.post("http://localhost:8080/custodian/add",Custodian.class,
                "name","HealthClinic",
                "validationId", generateValidationFromCredentials("healthclinic","hcPass123"),
                "address1","123 This Place",
                "state","NW",
                "city","Erehwon",
                "zip","12345");

    }
}
