package com.barlea.blockchain.service;

import com.barlea.blockchain.domain.Block;
import com.barlea.blockchain.domain.Transaction;
import com.barlea.blockchain.entities.Entity;
import com.barlea.blockchain.util.BlockProofOfWorkGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * This class is the backbone of our Blockchain system.
 * 
 * @author Praveendra Singh, L Rick Jones
 *
 */
@Data
@Slf4j
@Service
public class Blockchain {

	private List<Block> chain;
	private List<Transaction> currentTransactions;

	@Autowired
	private ObjectMapper mapper;

	public Blockchain() throws JsonProcessingException {

		chain = new ArrayList<>();
		currentTransactions = new ArrayList<>();

		// Create the genesis block
		createBlock(Block.GENESIS_BLOCK_PROOF, Block.GENESIS_BLOCK_PREV_HASH);
	}

	public int addTransaction(String sender, String recipient, Entity entity) {

		Transaction transaction = Transaction.builder().sender(sender).recipient(recipient).entity(entity).build();

		currentTransactions.add(transaction);

		return lastBlock().getIndex();
	}

	public Block createBlock(Long proof, String previusHash) throws JsonProcessingException {

		Block block = Block.builder().index(chain.size() + 1)
				.previousHash((previusHash != null) ? previusHash : lastBlock().hash(mapper)).proof(proof)
				.timestamp(new Date().getTime()).transactions(currentTransactions).build();

		// add new block to the chain.
		this.chain.add(block);

		// start accepting new transactions.
		this.currentTransactions = new ArrayList<>();

		return block;
	}

	public Block lastBlock() {
		return chain.get(this.chain.size() - 1);
	}

	public Block blockAt(int index) {
		Optional<Block> result = chain.stream().filter(o -> o.getIndex() == index).findFirst();
		return result.orElse(null);
	}

	public static boolean validChain(List<Block> chain, ObjectMapper mapper) throws JsonProcessingException {

		if (chain == null || chain.isEmpty())
			return false;

		Block lastBlock = chain.get(0);

		for (int currentIndex = 1; currentIndex < chain.size(); currentIndex++) {

			Block currentBlock = chain.get(currentIndex);

			log.debug("lastBlock={}", lastBlock);
			log.debug("currentBlock={}", currentBlock);

			if (!currentBlock.getPreviousHash().equals(lastBlock.hash(mapper))) {
				return false;
			}

			if (!BlockProofOfWorkGenerator.validProof(lastBlock.getProof(), currentBlock.getProof())) {
				return false;
			}

			lastBlock = currentBlock;
		}

		return true;
	}

	public boolean validChain() throws JsonProcessingException {
		return validChain(chain, mapper);
	}
}
