package com.barlea.blockchain.entities;

import lombok.*;

import javax.validation.constraints.NotEmpty;

/**
 * For use with the public block chain
 *
 * @author L Rick Jones
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Communal extends Entity{

    @Builder.Default private final String type = "applicant";

    /*
    Hasher from applicants authentication credentials, will be tested against applicant registry
     */
    @NotEmpty
    private String contractId;
    /*
    Type of data request
     */
    private String contractStatus;

    private String authorityDescription;

    private String subjectZIP;

    private String applicantName;

    private String applicantZIP;

    private String arbiterName;

    private String arbiterJurisdiction;

    private String custodianName;

}
