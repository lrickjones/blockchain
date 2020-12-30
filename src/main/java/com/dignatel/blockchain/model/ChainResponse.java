package com.dignatel.blockchain.model;

import java.util.List;

import com.dignatel.blockchain.domain.Block;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Holds Chain details.
 * 
 * @author Praveendra Singh
 *
 */
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChainResponse {
	private Integer length;
	private List<Block> chain;
}
