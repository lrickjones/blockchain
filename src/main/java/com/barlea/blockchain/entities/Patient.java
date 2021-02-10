package com.barlea.blockchain.entities;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

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
public class Patient extends Entity {

    @Builder.Default private final String type = "custodian";

    @NotEmpty
    private String validationId;

    @NotEmpty
    private String name;

    private Address address;

    private Birthdate birthdate;

    private List<Approval> approvals;
}
