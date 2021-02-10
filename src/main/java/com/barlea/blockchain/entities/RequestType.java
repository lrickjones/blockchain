package com.barlea.blockchain.entities;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestType {
    static final public String IMMUNIZATION_RECORDS = "immunization records";
    static final public String PRIMARY_PHYSICIAN = "primary physician";
    static final public String HEALTH_HISTORY = "health history";

    private String type;
}
