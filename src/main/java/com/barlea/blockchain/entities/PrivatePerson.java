package com.barlea.blockchain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Information associated with a person
 *
 * @author L Rick Jones
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrivatePerson {
    private String socialSecurity;
    private String driversLicenseNumber;
    private String driversLicenseState;
    private String passportNumber;
    private String mobilePhone;
}
