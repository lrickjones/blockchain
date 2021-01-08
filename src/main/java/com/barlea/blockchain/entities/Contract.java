package com.barlea.blockchain.entities;

import javax.validation.constraints.NotNull;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contract extends Entity {
    static public String[] status = {"account request","waiting review","passed review","failed review","token generated","access denied"};

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
