package com.barlea.blockchain.api;

import com.barlea.blockchain.entities.Arbiter;
import com.barlea.blockchain.entities.Name;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class ArbiterRegistry {

    List<Arbiter> arbiterList = new ArrayList<>();

    @PostMapping("/arbiter/add")
    public Arbiter addCourt(@RequestParam String jurisdiction,
                            Name judge) {
        Arbiter court = Arbiter.builder()
                .jurisdiction(jurisdiction)
                .personalInfo(judge)
                .build();
        arbiterList.add(court);
        return court;
    }

    @GetMapping("/arbiter/list")
    public List<Arbiter> listCourts() {
        return arbiterList;
    }

    @GetMapping("/arbiter/find")
    public Arbiter findCourt(String uuid) {
        Optional<Arbiter> court = arbiterList.stream().filter(o -> o.getUuid().equals(uuid)).findFirst();
        return court.orElse(null);
    }
}

