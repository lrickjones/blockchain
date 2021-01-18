package com.barlea.blockchain.entities;
import lombok.*;

import javax.validation.constraints.NotEmpty;

/**
 * Represents a court representative
 *
 * @author L Rick Jones
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Arbiter extends Entity {

    @Builder.Default private final String type = "arbiter";

    @NotEmpty
    private String jurisdiction;

    @NotEmpty
    private Name personalInfo;
}
