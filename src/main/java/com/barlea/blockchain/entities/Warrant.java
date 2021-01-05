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
public class Warrant extends Entity{

    @NotEmpty
    private String warrantId;

    @NotEmpty
    private String targetId;

    private String description;

    private Person owner;

}
