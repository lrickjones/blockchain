package com.barlea.blockchain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectRequest {
    private String requestType;

    private Name patientName;

    private Address patientAddress;

    private Birthdate patientBirthdate;
}
