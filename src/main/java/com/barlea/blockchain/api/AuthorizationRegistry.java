package com.barlea.blockchain.api;

import com.barlea.blockchain.entities.Authority;
import com.barlea.blockchain.entities.PublicPerson;
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
    public Authority addAuthority(@RequestParam String arbiterId,
                                @RequestParam String validationId,
                                @RequestParam String authorityType,
                                String description,
                                PublicPerson subject) {
        Authority authority = Authority.builder()
                .arbiterId(arbiterId)
                .validationId(validationId)
                .authorityType(authorityType)
                .description(description)
                .subject(subject)
                .build();
        authorityList.add(authority);
        return authority;
    }

    @GetMapping("/authority/list")
    public List<Authority> listAuthority() {
        return authorityList;
    }

    @GetMapping("/authority/find")
    public Authority findAuthority(String authorityId, String uuid) {
        Optional<Authority> authority = Optional.empty();
        if (uuid != null && !uuid.isEmpty()) {
            authority = authorityList.stream().filter(o -> o.getUuid().equals(uuid)).findFirst();
        } else if (authorityId != null && !authorityId.isEmpty()) {
            authority = authorityList.stream().filter(o -> o.getValidationId().equals(authorityId)).findFirst();
        }
        return authority.orElse(null);
    }
}

