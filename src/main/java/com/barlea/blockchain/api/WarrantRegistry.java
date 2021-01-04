package com.barlea.blockchain.api;

import com.barlea.blockchain.entities.Warrant;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class WarrantRegistry {

    List<Warrant> warrantList = new ArrayList<>();

    @PostMapping("/warrant/add")
    public Warrant addWarrant(@RequestBody String targetId, String warrantId) {
        Warrant warrant = Warrant.builder().warrantId(warrantId).targetId(targetId).build();
        warrantList.add(warrant);
        return warrant;
    }

    @GetMapping("/warrant/list")
    public List<Warrant> listWarrants() {
        return warrantList;
    }

    @GetMapping("/warrant/find")
    public Warrant findWarrant(String warrantId) {
        Optional<Warrant> warrant = warrantList.stream().filter(o -> o.getWarrantId().equals(warrantId)).findFirst();
        return warrant.orElse(null);
    }
}

