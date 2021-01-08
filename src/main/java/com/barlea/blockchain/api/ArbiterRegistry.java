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
    public Arbiter addArbiter(@RequestParam String jurisdiction,
                            Name arbiterName) {
        Arbiter arbiter = Arbiter.builder()
                .jurisdiction(jurisdiction)
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
    public Arbiter findArbiter(String uuid) {
        Optional<Arbiter> arbiter = arbiterList.stream().filter(o -> o.getUuid().equals(uuid)).findFirst();
        return arbiter.orElse(null);
    }
}

