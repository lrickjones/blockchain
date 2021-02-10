package com.barlea.blockchain.entities;

import lombok.*;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class HipaaContract extends Entity {

    @Builder.Default private final String type = "contract";
    // status strings
    static public final String RECORD_REQUEST = "record request";
    static public final String RECORD_FOUND = "record found";
    static public final String RECORD_NOT_FOUND = "record not authorized";
    static public final String APPLICANT_AUTHORIZED = "applicant authorized";
    static public final String APPLICANT_NOT_AUTHORIZED = "applicant not authorized";
    static public final String RECORD_DELIVERED = "record delivered";

    // owner strings
    public static final String APPLICANT = "applicant";
    public static final String CUSTODIAN = "custodian";
    public static final String ARBITER = "arbiter";

    @NotNull
    private String contractId;

    @NotNull
    private String currentStatus;

    @NotNull
    private String owner;

    private String applicantId;

    private String arbiterId;

    private String custodianId;

    private String tokenId;

    private int lastVerification;

    private String explanation;

    private String application;

}
