package com.barlea.blockchain.api;

import com.barlea.blockchain.entities.Applicant;
import com.barlea.blockchain.entities.Name;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Applicant Registry API
 *
 * @author L Rick Jones
 *
 */
@RestController
@RequestMapping("/")
public class ApplicantRegistry {

    List<Applicant> applicantList = new ArrayList<>();

    @PostMapping("/applicant/add")
    public Applicant addApplicant(@RequestParam String validationId,
                            @RequestParam String requestType,
                            Name personalInfo, String organization) {
        Applicant applicant = Applicant.builder()
                .requestType(requestType)
                .validationId(validationId)
                .name(personalInfo)
                .organization(organization)
                .build();
        applicantList.add(applicant);
        return applicant;
    }

    @GetMapping("/applicant/list")
    public List<Applicant> listApplicants() {
        return applicantList;
    }

    @GetMapping("/applicant/find")
    public Applicant findApplicant(String validationId, String uuid) {
        Optional<Applicant> warrant = Optional.empty();
        if (uuid != null && !uuid.isEmpty()) {
            warrant = applicantList.stream().filter(o -> o.getUuid().equals(uuid)).findFirst();
        } else if (validationId != null && !validationId.isEmpty()) {
            warrant = applicantList.stream().filter(o -> o.getValidationId().equals(validationId)).findFirst();
        }
        return warrant.orElse(null);
    }
}

