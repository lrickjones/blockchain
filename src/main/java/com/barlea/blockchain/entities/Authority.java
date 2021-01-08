package com.barlea.blockchain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authority extends Entity{

    @NotEmpty
    private String authorityId;

    @NotEmpty
    private String targetId;

    private String description;

    private Name owner;

}
