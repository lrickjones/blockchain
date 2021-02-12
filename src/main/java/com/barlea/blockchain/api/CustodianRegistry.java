package com.barlea.blockchain.api;

import com.barlea.blockchain.entities.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * Custodian Registry API
 *
 * @author L Rick Jones
 *
 */
@RestController
@RequestMapping("/")
public class CustodianRegistry {
    List<Custodian> custodianList = new ArrayList<>();

    @PostMapping("/custodian/add")
    public Custodian addCustodian(@RequestParam String validationId,
                                  @RequestParam String name,
                                  Address address) {
        Custodian custodian = Custodian.builder()
                .validationId(validationId)
                .name(name)
                .address(address)
                .build();
        custodianList.add(custodian);
        return custodian;
    }

    @GetMapping("/custodian/list")
    public List<Custodian> listCustodians() {
        return custodianList;
    }

    @GetMapping("/custodian/find")
    public Custodian findAuthority(String uuid, String validationId) {
        Optional<Custodian> custodian = Optional.empty();
        if (uuid != null && !uuid.isEmpty()) {
            custodian = custodianList.stream().filter(o -> o.getUuid().equals(uuid)).findFirst();
        } else if (validationId != null && !validationId.isEmpty()) {
            custodian = custodianList.stream().filter(o -> o.getValidationId().equals(validationId)).findFirst();
        }
        return custodian.orElse(null);
    }

    @GetMapping("/custodian/clear")
    public void clearCustodians() {
        custodianList.clear();
    }
}
