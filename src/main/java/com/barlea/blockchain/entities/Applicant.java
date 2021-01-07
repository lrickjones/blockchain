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
public class Applicant extends Entity{

    @NotEmpty
    private String rank;

    @NotEmpty
    private String badgeNumber;

    @NotEmpty
    private Name personalInfo;


}
