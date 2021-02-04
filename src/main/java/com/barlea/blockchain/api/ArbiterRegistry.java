package com.barlea.blockchain.api;

import com.barlea.blockchain.entities.Arbiter;
import com.barlea.blockchain.entities.Name;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Arbiter Registry API
 *
 * @author L Rick Jones
 *
 */
@RestController
@RequestMapping("/")
public class ArbiterRegistry {

    List<Arbiter> arbiterList = new ArrayList<>();

    @PostMapping("/arbiter/add")
    public Arbiter addArbiter(@RequestParam String jurisdiction, String validationId,
                            Name arbiterName) {
        Arbiter arbiter = Arbiter.builder()
                .jurisdiction(jurisdiction)
                .validationId(validationId)
                .personalInfo(arbiterName)
                .build();
        arbiterList.add(arbiter);
        return arbiter;
    }

    @GetMapping("/arbiter/list")
    public List<Arbiter> listArbiters() {
        return arbiterList;
    }

    @GetMapping("/arbiter/find")
    public Arbiter findArbiter(String uuid, String validationId) {
        Optional<Arbiter> arbiter = Optional.empty();
        if (uuid != null && !uuid.isEmpty()) {
            arbiter = arbiterList.stream().filter(o -> o.getUuid().equals(uuid)).findFirst();
        } else if (validationId != null && !validationId.isEmpty()) {
            arbiter = arbiterList.stream().filter(o -> o.getValidationId().equals(validationId)).findFirst();
        }
        return arbiter.orElse(null);
    }
}

