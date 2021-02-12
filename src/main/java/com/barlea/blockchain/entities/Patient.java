package com.barlea.blockchain.entities;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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

    @Builder.Default private final String type = "arbiter";

    @NotEmpty
    private String validationId;

    @NotEmpty
    private Name name;

    private Address address;

    private Birthdate birthdate;

    private List<Approval> approvals;

    public void addApproval(String applicantUuId,String requestType) {
        if (approvals == null) approvals = new ArrayList<>();
        Optional<Approval> approval = approvals.stream().filter(o -> o.getApplicant_id().equals(applicantUuId)).findFirst();
        if (approval.isPresent()) {
            // add to existing approval
            approval.get().addRequestType(new RequestType(requestType));
        } else {
            // add a new approval
            Approval a = Approval.builder().applicant_id(applicantUuId).build();
            a.addRequestType(new RequestType(requestType));
            approvals.add(a);
        }
    }

    public boolean isApproved(String applicantUuId,String requestType) {
        if (approvals == null) return false;
        boolean result = false;
        Optional<Approval> approval = approvals.stream().filter(o -> o.getApplicant_id().equals(applicantUuId)).findFirst();
        if (!approval.isPresent()) return false;
        for (RequestType r: approval.get().getRequestTypes()) {
            if (r.getType().toLowerCase(Locale.ROOT).equals(requestType.toLowerCase(Locale.ROOT))) result = true;
        }
        return result;
    }
}
