package com.barlea.blockchain.api;

import com.barlea.blockchain.entities.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Arbiter Registry API
 *
 * @author L Rick Jones
 *
 */
@RestController
@RequestMapping("/")
public class PatientRegistry {

    List<Patient> patientList = new ArrayList<>();

    @PostMapping("/patient/add")
    public Patient addPatient(String validationId, Name arbiterName, Birthdate birthdate) {
        Patient patient = Patient.builder()
                .validationId(validationId)
                .name(arbiterName)
                .birthdate(birthdate)
                .build();
        patientList.add(patient);
        return patient;
    }

    @PostMapping("/patient/authorization")
    public Patient addAuthorization(@RequestParam String uuid, String applicantUuId, String requestType){
        Patient patient = findPatient(uuid,"");
        if (patient == null) return null;
        patient.addApproval(applicantUuId,requestType);
        return patient;
    }

    @GetMapping("/patient/list")
    public List<Patient> listPatients() {
        return patientList;
    }

    @GetMapping("/patient/find")
    public Patient findPatient(String uuid, String validationId) {
        Optional<Patient> patient = Optional.empty();
        if (uuid != null && !uuid.isEmpty()) {
            patient = patientList.stream().filter(o -> o.getUuid().equals(uuid)).findFirst();
        } else if (validationId != null && !validationId.isEmpty()) {
            patient = patientList.stream().filter(o -> o.getValidationId().equals(validationId)).findFirst();
        }
        return patient.orElse(null);
    }

    @GetMapping("/patient/find-by-name")
    public Patient findPatientByName(Name name) {
        Optional<Patient> patient;
        patient = patientList.stream().filter(o -> o.getName().getLastName().toLowerCase(Locale.ROOT).equals(name.getLastName().toLowerCase(Locale.ROOT))
                                        && o.getName().getFirstName().toLowerCase(Locale.ROOT).equals(name.getFirstName().toLowerCase(Locale.ROOT))).findFirst();
        return patient.orElse(null);
    }

    @GetMapping("/patient/authorizes")
    public boolean authorizes(@RequestParam String applicantUuId, String requestType, String organization) {
        Patient patient = findPatient(applicantUuId,"");
        if (patient == null) return false;
        return patient.isApproved(applicantUuId,requestType);
    }

    @GetMapping("/patient/clear")
    public void clearPatients() {
        patientList.clear();
    }
}

