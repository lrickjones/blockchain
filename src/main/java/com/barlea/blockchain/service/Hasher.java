package com.barlea.blockchain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class Hasher {
    static public String hash(String json) throws JsonProcessingException {
        return Hashing.sha256().hashString(json, StandardCharsets.UTF_8).toString();
    }
}
