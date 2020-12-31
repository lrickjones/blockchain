package com.barlea.blockchain.domain;

import com.barlea.blockchain.entities.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * Represents a transaction event in the Block.
 * 
 * @author Praveendra Singh
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
	@NotEmpty
	private String sender;

	@NotEmpty
	private String recipient;

	@NotEmpty
	private Entity entity;

}