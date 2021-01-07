package com.barlea.blockchain.config;

import com.barlea.blockchain.entities.Applicant;
import com.barlea.blockchain.entities.Credentials;
import com.barlea.blockchain.service.Hasher;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class Initialize implements ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        String uri = "http://localhost:8080/applicant/add";
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        Credentials creds = Credentials.builder().userName("johndoe").passWord("myPass123").build();
        String validationId;
        try {
            validationId = Hasher.hash(creds.toString());
        } catch (JsonProcessingException e) {
            validationId = "Invalid";
        }
        map.add("requestType", "test");
        map.add("validationId", validationId);
        map.add("firstName", "john");
        map.add("middleName", "j");
        map.add("lastName", "doe");
        RestTemplate restTemplate = new RestTemplate();
        Applicant result = restTemplate.postForObject(uri, map, Applicant.class);
    }
}
