package com.barlea.blockchain.entities;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authority extends Entity{

    @NotEmpty
    private String authorityId;

    @NotEmpty
    private String authorityType;

    private String description;

    @NotEmpty
    private PublicPerson subject;

    private String proxyId;

}
