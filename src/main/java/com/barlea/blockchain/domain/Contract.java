package com.barlea.blockchain.domain;

import javax.validation.constraints.NotNull;

import com.barlea.blockchain.entities.CourtRepresentative;
import com.barlea.blockchain.entities.Custodian;
import com.barlea.blockchain.entities.Leo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contract {

    private Leo leo;

    private CourtRepresentative court;

    private Custodian custodian;

    private String token;

    @NotNull
    private String status;

}
