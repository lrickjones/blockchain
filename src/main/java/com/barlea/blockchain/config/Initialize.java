package com.barlea.blockchain.config;

import com.barlea.blockchain.entities.Applicant;
import com.barlea.blockchain.entities.Authority;
import com.barlea.blockchain.entities.Credentials;
import com.barlea.blockchain.service.Hasher;
import com.barlea.blockchain.service.Rest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Initialize entities used for demonstrating workflow
 * @author L Rick Jones
 */
@Component
public class Initialize implements ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(@NotNull ApplicationStartedEvent event) {
        Credentials creds = Credentials.builder().userName("johndoe").password("myPass123").build();
        String validationId;
        try {
            validationId = Hasher.hash(creds.toString());
        } catch (JsonProcessingException e) {
            validationId = "Invalid";
        }
        /*Applicant applicant =*/ Rest.post("http://localhost:8080/applicant/add",Applicant.class,
                "requestType","test",
                "validationId",validationId,
                "firstName","john",
                "middleName","j",
                "lastName","doe");

        Credentials auths = Credentials.builder().userName("federalcourt").password("fedPass123").build();
        String authorityId;
        try {
            authorityId = Hasher.hash(auths.toString());
        } catch (JsonProcessingException e) {
            authorityId = "Invalid";
        }
        /*Authority authority =*/ Rest.post("http://localhost:8080/authority/add",Authority.class,
                "authorityId",authorityId,
                "authorityType","warrant",
                "description","access to contacts made over last 6 months",
                "homeDomicile.address1","123 East W Street",
                "homeDomicile.state", "NM",
                "homeDomicile.city", "Roswell",
                "homeDomicile.zip", "88201",
                "name.firstName","joe",
                "name.middleName","d",
                "name.lastName","bloe",
                "birthDate","07/04/1960",
                "homePhone","555-667-8309");

    }
}
