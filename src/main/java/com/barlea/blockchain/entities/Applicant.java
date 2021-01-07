package com.barlea.blockchain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * Represents a law enforcement officer
 *
 * @author Praveendra Singh
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Applicant extends Entity{

    /*
    Hasher from applicants authentication credentials, will be tested against applicant registry
     */
    @NotEmpty
    private String validationId;

    /*
    Type of data request
     */
    @NotEmpty
    private String requestType;

    /*
    Applicants full name
     */
    @NotEmpty
    private Name name;

}
