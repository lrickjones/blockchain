package com.barlea.blockchain.service;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class Rest {
    static public <T> T post(String uri, Class<T> responseType,String ... args) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

        for(int i = 0; i < args.length - 1;i+=2) {
            map.add(args[i],args[i+1]);
        }

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(uri, map, responseType);
    }

    static public <T> T get(String uri, Class<T> responseType,String ... args) {
        UriComponentsBuilder ucb = UriComponentsBuilder.fromHttpUrl(uri);

        for(int i = 0; i < args.length - 1;i+=2) {
            ucb.queryParam(args[i],args[i + 1]);
        }

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(ucb.toUriString(),  responseType);
    }
}
