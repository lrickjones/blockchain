package com.barlea.blockchain.config;

import com.barlea.blockchain.entities.*;
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
                "firstName","John",
                "middleName","J",
                "lastName","Doe");

        Arbiter arbiter = Rest.post("http://localhost:8080/arbiter/add",Arbiter.class,
                "jurisdiction", "West Side",
                "firstName","Wyatt",
                "middleName","P",
                "lastName","Earp");

        /*Custodian custodian =*/ Rest.post("http://localhost:8080/custodian/add",Custodian.class,
                "name","U-Face",
                "address1","123 This Place",
                "state","NW",
                "city","Erehwon",
                "zip","12345");

        Credentials auths = Credentials.builder().userName("federalcourt").password("fedPass123").build();
        String authorityId;
        try {
            authorityId = Hasher.hash(auths.toString());
        } catch (JsonProcessingException e) {
            authorityId = "Invalid";
        }
        /*Authority authority =*/ Rest.post("http://localhost:8080/authority/add",Authority.class,
                "arbiterId", arbiter.getUuid(),
                "validationId",authorityId,
                "authorityType","Warrant",
                "description","Access to contacts made over last 6 months, Case# 12345678",
                "homeDomicile.address1","123 East W Street",
                "homeDomicile.state", "NM",
                "homeDomicile.city", "Roswell",
                "homeDomicile.zip", "88201",
                "name.firstName","Joe",
                "name.middleName","D",
                "name.lastName","Smith",
                "birthDate","07/04/1960",
                "homePhone","555-667-8309");

    }
}
