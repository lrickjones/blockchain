package com.barlea.blockchain.entities;
import lombok.*;

import javax.validation.constraints.NotEmpty;

/**
 * Represents a device manufacturer, service provider or other data custodian
 *
 * @author L Rick Jones
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Custodian extends Entity {

    @Builder.Default private final String type = "custodian";

    @NotEmpty
    private String validationId;

    @NotEmpty
    private String name;

    @NotEmpty
    private Address address;
}
