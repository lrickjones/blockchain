package com.barlea.blockchain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
public class PublicPerson {

    private Date birthDate;

    private Address placeOfBirth;

    private Address homeDomicile;

    private String homePhone;

    private String workPhone;

}
