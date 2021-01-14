package com.barlea.blockchain.entities;

import javax.validation.constraints.NotNull;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contract extends Entity {
    static public String ACCOUNT_REQUEST = "account request";
    static public String WAITING_REVIEW = "waiting review";
    static public String PASSED_REVIEW = "passed review";
    static public String FAILED_REVIEW = "failed review";
    static public String TOKEN_GENERATED = "token generated";
    static public String ACCESS_DENIED = "access denied";

    private String applicantId;

    private String arbiterId;

    private String custodianId;

    private String authorityId;

    private String tokenId;

    @NotNull
    private String currentStatus;

    private int lastVerification;

    private String explanation;

}
