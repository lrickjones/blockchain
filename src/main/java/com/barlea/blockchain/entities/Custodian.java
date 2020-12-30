package com.barlea.blockchain.entities;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * Represents a device manufacturer, service provider or other data custodian
 *
 * @author L Rick Jones
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Custodian {
    @NotEmpty
    private String id;

    @NotEmpty
    private Address address;
}
