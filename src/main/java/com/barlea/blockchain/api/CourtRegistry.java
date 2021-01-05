package com.barlea.blockchain.api;

import com.barlea.blockchain.entities.CourtRepresentative;
import com.barlea.blockchain.entities.Person;
import com.barlea.blockchain.entities.Warrant;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class CourtRegistry {

    List<CourtRepresentative> courtList = new ArrayList<>();

    @PostMapping("/court/add")
    public CourtRepresentative addCourt(@RequestParam String jurisdiction,
                              Person judge) {
        CourtRepresentative court = CourtRepresentative.builder()
                .jurisdiction(jurisdiction)
                .personalInfo(judge)
                .build();
        courtList.add(court);
        return court;
    }

    @GetMapping("/court/list")
    public List<CourtRepresentative> listCourts() {
        return courtList;
    }

    @GetMapping("/court/find")
    public CourtRepresentative findCourt(String uuid) {
        Optional<CourtRepresentative> court = courtList.stream().filter(o -> o.getUuid().equals(uuid)).findFirst();
        return court.orElse(null);
    }
}

