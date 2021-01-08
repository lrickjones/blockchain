package com.barlea.blockchain.api;

import com.barlea.blockchain.entities.Name;
import com.barlea.blockchain.entities.Authority;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * Authorization Registry API
 *
 * @author L Rick Jones
 *
 */
@RestController
@RequestMapping("/")
public class AuthorizationRegistry {

    List<Authority> authorityList = new ArrayList<>();

    @PostMapping("/authority/add")
    public Authority addAuthority(@RequestParam String warrantId,
                                @RequestParam String targetId,
                                String description,
                                Name owner) {
        Authority authority = Authority.builder()
                .authorityId(warrantId)
                .targetId(targetId)
                .description(description)
                .owner(owner)
                .build();
        authorityList.add(authority);
        return authority;
    }

    @GetMapping("/authority/list")
    public List<Authority> listAuthority() {
        return authorityList;
    }

    @GetMapping("/authority/find")
    public Authority findAuthority(String warrantId, String uuid) {
        Optional<Authority> authority = Optional.empty();
        if (uuid != null && !uuid.isEmpty()) {
            authority = authorityList.stream().filter(o -> o.getUuid().equals(uuid)).findFirst();
        } else if (warrantId != null && !warrantId.isEmpty()) {
            authority = authorityList.stream().filter(o -> o.getAuthorityId().equals(warrantId)).findFirst();
        }
        return authority.orElse(null);
    }
}

