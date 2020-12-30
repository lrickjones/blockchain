package com.barlea.blockchain.entities;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * Represents a transaction event in the Block.
 *
 * @author Praveendra Singh
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @NotEmpty
    private String address1;

    @NotEmpty
    private String address2;

    @NotEmpty
    private String state;

    @NotEmpty
    private String city;

    @NotEmpty
    private String zip;
}
