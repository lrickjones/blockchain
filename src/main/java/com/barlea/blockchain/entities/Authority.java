package com.barlea.blockchain.entities;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authority extends Entity{

    @Builder.Default private final String type = "authority";

    @NotEmpty
    private String validationId;

    @NotEmpty
    private String authorityType;

    private String documentId;

    private String description;

    @NotEmpty
    private String arbiterId;

    private String custodianId;

    @NotEmpty
    private PublicPerson subject;

    private String proxyId;

}
