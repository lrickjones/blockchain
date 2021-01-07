package com.barlea.blockchain.api;

import com.barlea.blockchain.entities.Name;
import com.barlea.blockchain.entities.Warrant;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * Warrant API
 *
 * @author L Rick Jones
 *
 */
@RestController
@RequestMapping("/")
public class WarrantRegistry {

    List<Warrant> warrantList = new ArrayList<>();

    @PostMapping("/warrant/add")
    public Warrant addWarrant(@RequestParam String warrantId,
                              @RequestParam String targetId,
                              String description,
                              Name owner) {
        Warrant warrant = Warrant.builder()
                .warrantId(warrantId)
                .targetId(targetId)
                .description(description)
                .owner(owner)
                .build();
        warrantList.add(warrant);
        return warrant;
    }

    @GetMapping("/warrant/list")
    public List<Warrant> listWarrants() {
        return warrantList;
    }

    @GetMapping("/warrant/find")
    public Warrant findWarrant(String warrantId, String uuid) {
        Optional<Warrant> warrant = Optional.empty();
        if (uuid != null && !uuid.isEmpty()) {
            warrant = warrantList.stream().filter(o -> o.getUuid().equals(uuid)).findFirst();
        } else if (warrantId != null && !warrantId.isEmpty()) {
            warrant = warrantList.stream().filter(o -> o.getWarrantId().equals(warrantId)).findFirst();
        }
        return warrant.orElse(null);
    }
}

