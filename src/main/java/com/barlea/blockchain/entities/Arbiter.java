package com.barlea.blockchain.entities;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * Represents a court representative
 *
 * @author L Rick Jones
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Arbiter extends Entity {
    @NotEmpty
    private String jurisdiction;

    @NotEmpty
    private Name personalInfo;
}
