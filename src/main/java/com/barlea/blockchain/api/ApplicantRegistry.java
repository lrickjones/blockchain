package com.barlea.blockchain.api;

import com.barlea.blockchain.entities.Applicant;
import com.barlea.blockchain.entities.Name;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class ApplicantRegistry {

    List<Applicant> applicantList = new ArrayList<>();

    @PostMapping("/applicant/add")
    public Applicant addLeo(@RequestParam String badgeNumber,
                            @RequestParam String rank,
                            Name personalInfo) {
        Applicant applicant = Applicant.builder()
                .badgeNumber(badgeNumber)
                .rank(rank)
                .personalInfo(personalInfo)
                .build();
       applicantList.add(applicant);
        return applicant;
    }

    @GetMapping("/applicant/list")
    public List<Applicant> listLeos() {
        return applicantList;
    }

    @GetMapping("/applicant/find")
    public Applicant findLeo(String badgeNumber, String uuid) {
        Optional<Applicant> warrant = Optional.empty();
        if (uuid != null && !uuid.isEmpty()) {
            warrant = applicantList.stream().filter(o -> o.getUuid().equals(uuid)).findFirst();
        } else if (badgeNumber != null && !badgeNumber.isEmpty()) {
            warrant = applicantList.stream().filter(o -> o.getBadgeNumber().equals(badgeNumber)).findFirst();
        }
        return warrant.orElse(null);
    }
}

