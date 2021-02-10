package com.barlea.blockchain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HipaaApplication {
    private String requestType;

    private String patientName;

    private Address patientAddress;

    private Birthdate patientBirthdate;
}
