package com.barlea.blockchain.entities;

import lombok.*;

import javax.validation.constraints.NotEmpty;

/**
 * Represents a law enforcement officer
 *
 * @author L Rick Jones
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Applicant extends Entity{

    @Builder.Default private final String type = "applicant";

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

    private String organization;

}
