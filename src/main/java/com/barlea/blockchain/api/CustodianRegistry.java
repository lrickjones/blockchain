package com.barlea.blockchain.api;

import com.barlea.blockchain.entities.Address;
import com.barlea.blockchain.entities.Authority;
import com.barlea.blockchain.entities.Custodian;
import com.barlea.blockchain.entities.PublicPerson;
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
    public Custodian addCustodian(@RequestParam String name,
                                  Address address) {
        Custodian custodian = Custodian.builder()
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
    public Custodian findAuthority(String uuid) {
        Optional<Custodian> custodian = Optional.empty();
        if (uuid != null && !uuid.isEmpty()) {
            custodian = custodianList.stream().filter(o -> o.getUuid().equals(uuid)).findFirst();
        }
        return custodian.orElse(null);
    }
}
