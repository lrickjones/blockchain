package com.barlea.blockchain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * Represents a law enforcement officer
 *
 * @author Praveendra Singh
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Leo {
    @NotEmpty
    private String rank;

    @NotEmpty
    private String badgeNumber;

    @NotEmpty
    private Person personalInfo;

    @NotEmpty
    private String sender;

    @NotEmpty
    private String recipient;

}
