package com.barlea.blockchain.entities;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contract extends Entity {

    private String leoId;

    private String courtId;

    private String custodianId;

    private String warrantId;

    private String tokenId;

    @NotNull
    private String status;

}
