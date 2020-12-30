package com.barlea.blockchain.model;

import java.util.List;

import com.barlea.blockchain.domain.Transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Holds the mined block details.
 * 
 * @author Praveendra Singh
 *
 */
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MineResponse {
	private String message;
	private int index;
	private List<Transaction> transactions;
	private Long proof;
	private String previousHsh;
}
