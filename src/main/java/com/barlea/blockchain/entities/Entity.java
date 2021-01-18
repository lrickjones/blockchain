package com.barlea.blockchain.entities;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

/**
 * Root class for entities
 *
 * @author L Rick Jones
 *
 */
@Data
public class Entity {

    public Entity() {
        this.uuid = UUID.randomUUID().toString();
    }

    @NotEmpty
    private String uuid;

}
