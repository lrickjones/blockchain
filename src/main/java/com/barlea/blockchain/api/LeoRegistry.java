package com.barlea.blockchain.api;

import com.barlea.blockchain.entities.Leo;
import com.barlea.blockchain.entities.Person;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class LeoRegistry {

    List<Leo> leoList = new ArrayList<>();

    @PostMapping("/leo/add")
    public Leo addLeo(@RequestParam String badgeNumber,
                      @RequestParam String rank,
                      Person personalInfo) {
        Leo leo = Leo.builder()
                .badgeNumber(badgeNumber)
                .rank(rank)
                .personalInfo(personalInfo)
                .build();
       leoList.add(leo);
        return leo;
    }

    @GetMapping("/leo/list")
    public List<Leo> listLeos() {
        return leoList;
    }

    @GetMapping("/leo/find")
    public Leo findLeo(String badgeNumber, String uuid) {
        Optional<Leo> warrant = Optional.empty();
        if (uuid != null && !uuid.isEmpty()) {
            warrant = leoList.stream().filter(o -> o.getUuid().equals(uuid)).findFirst();
        } else if (badgeNumber != null && !badgeNumber.isEmpty()) {
            warrant = leoList.stream().filter(o -> o.getBadgeNumber().equals(badgeNumber)).findFirst();
        }
        return warrant.orElse(null);
    }
}

