package com.barlea.blockchain.entities;

import javax.validation.constraints.NotNull;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Contract extends Entity {

    @Builder.Default private final String type = "contract";
    // status strings
    static public final String ACCOUNT_REQUEST = "account request";
    static public final String GET_ACCOUNT_INFO = "get account info";
    static public final String ACCOUNT_FOUND = "account found";
    static public final String MULTIPLE_ACCOUNTS = "multiple accounts";
    static public final String ACCOUNT_NOT_FOUND = "account not found";
    static public final String WAITING_REVIEW = "waiting review";
    static public final String PASSED_REVIEW = "passed review";
    static public final String FAILED_REVIEW = "failed review";
    static public final String TOKEN_GENERATED = "token generated";
    static public final String ACCESS_DENIED = "access denied";
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

    private String authorityId;

    private String tokenId;

    private int lastVerification;

    private String explanation;

}
