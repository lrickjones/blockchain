package com.barlea.blockchain.entities;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Information associated with a person
 *
 * @author L Rick Jones
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Name {

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String middleName;

    @NotEmpty
    private String lastName;

}