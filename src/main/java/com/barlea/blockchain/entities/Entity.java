package com.barlea.blockchain.entities;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

/**
 * Represents a law enforcement officer
 *
 * @author Praveendra Singh
 *
 */
@Data
@AllArgsConstructor
public class Entity {

    public Entity() {
        this.uuid = UUID.randomUUID().toString();
    }

    @NotEmpty
    private String uuid;
}
