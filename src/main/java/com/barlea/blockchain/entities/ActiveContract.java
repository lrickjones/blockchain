package com.barlea.blockchain.entities;

import lombok.*;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActiveContract extends Entity {

    @Builder.Default private final String type = "active-contract";

    @NotNull
    private int contractIndex;

    @NotNull
    private String contractId;

}
