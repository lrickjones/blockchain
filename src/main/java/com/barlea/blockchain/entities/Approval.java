package com.barlea.blockchain.entities;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Approval {
    private String applicant_id;

    private List<RequestType> requestTypes;
}
