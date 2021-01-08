package com.barlea.blockchain.api;

import com.barlea.blockchain.domain.Block;
import com.barlea.blockchain.entities.Applicant;
import com.barlea.blockchain.entities.Credentials;
import com.barlea.blockchain.entities.Authority;
import com.barlea.blockchain.model.ChainResponse;
import com.barlea.blockchain.model.RecordResponse;
import com.barlea.blockchain.model.TransactionResponse;
import com.barlea.blockchain.service.Blockchain;
import com.barlea.blockchain.service.Hasher;
import com.barlea.blockchain.service.Rest;
import com.barlea.blockchain.util.BlockProofOfWorkGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Private blockchain API
 * 
 * @author L Rick Jones
 *
 */
@RestController
@RequestMapping("/")
public class BlockchainController {

	@Autowired
	private Blockchain register;

	@Autowired
	private Blockchain requests;

	@Autowired
	private Blockchain verifications;

	@Autowired
	private ObjectMapper mapper;

	public static final String NODE_ACCOUNT_ADDRESS = "0";

	@GetMapping("/register/record")
	public RecordResponse registerRecord() throws JsonProcessingException {

		// (1) - Calculate the Proof of Work
		Block lastBlock = register.lastBlock();

		Long lastProof = lastBlock.getProof();

		Long proof = BlockProofOfWorkGenerator.proofOfWork(lastProof);

		// (2) - Add pending transactions to new block
		Block newBlock = register.createBlock(proof, lastBlock.hash(mapper));

		return RecordResponse.builder().message("New Block Forged").index(newBlock.getIndex())
				.transactions(newBlock.getTransactions()).proof(newBlock.getProof())
				.previousHsh(newBlock.getPreviousHash()).build();
	}

	@GetMapping("/register/chain")
	public ChainResponse registerFullChain() {
		return ChainResponse.builder().chain(register.getChain()).length(register.getChain().size()).build();
	}

	@PostMapping("/register/transaction")
	public TransactionResponse registerNewTransaction(@RequestBody @Valid Applicant applicant) {

		int index = register.addTransaction("me", "you", applicant);
		return TransactionResponse.builder().index(index).build();
	}

	@GetMapping("/verification/record")
	public RecordResponse verificationRecord() throws JsonProcessingException {

		// (1) - Calculate the Proof of Work
		Block lastBlock = verifications.lastBlock();

		Long lastProof = lastBlock.getProof();

		Long proof = BlockProofOfWorkGenerator.proofOfWork(lastProof);

		// (2) - Add pending transactions to new block
		Block newBlock = verifications.createBlock(proof, lastBlock.hash(mapper));

		return RecordResponse.builder().message("New Block Forged").index(newBlock.getIndex())
				.transactions(newBlock.getTransactions()).proof(newBlock.getProof())
				.previousHsh(newBlock.getPreviousHash()).build();
	}

	@GetMapping("/verification/chain")
	public ChainResponse verificationFullChain() {
		return ChainResponse.builder().chain(verifications.getChain()).length(verifications.getChain().size()).build();
	}

	@PostMapping("/verification/transaction")
	public TransactionResponse verificationNewTransaction(@RequestBody @Valid Applicant applicant) {

		int index = verifications.addTransaction("me", "you", applicant);
		return TransactionResponse.builder().index(index).build();
	}

	@GetMapping("/request/record")
	public RecordResponse requestRecord() throws JsonProcessingException {

		// (1) - Calculate the Proof of Work
		Block lastBlock = requests.lastBlock();

		Long lastProof = lastBlock.getProof();

		Long proof = BlockProofOfWorkGenerator.proofOfWork(lastProof);

		// (2) - Add pending transactions to new block
		Block newBlock = requests.createBlock(proof, lastBlock.hash(mapper));

		return RecordResponse.builder().message("New Block Forged").index(newBlock.getIndex())
				.transactions(newBlock.getTransactions()).proof(newBlock.getProof())
				.previousHsh(newBlock.getPreviousHash()).build();
	}

	@GetMapping("/request/chain")
	public ChainResponse requestFullChain() {
		return ChainResponse.builder().chain(requests.getChain()).length(requests.getChain().size()).build();
	}

	@PostMapping("/request/transaction")
	public TransactionResponse requestNewTransaction(@RequestBody @Valid Applicant applicant) {

		int index = requests.addTransaction("me", "you", applicant);
		return TransactionResponse.builder().index(index).build();
	}

	@PostMapping("/contract/applicant/request_token")
	public RecordResponse requestAccessToken(String userName, String password, String applicantUuId,String authName, String authPassword, String authorityUuId) throws JsonProcessingException {
		// Find applicant
		Applicant applicant = Rest.get("http://localhost:8080/applicant/find",Applicant.class,"uuid",applicantUuId);
		if (applicant == null) return null;
		// Validate applicant
		String applicantId = Hasher.hash(Credentials.builder().userName(userName).password(password).build().toString());
		if (!applicantId.equals(applicant.getValidationId())) return null;
		// Find authority
		Authority authority = Rest.get("http://localhost:8080/authority/find",Authority.class,"uuid",authorityUuId);
		if (authority == null) return null;
		// validate authority
		String validationId = Hasher.hash(Credentials.builder().userName(authName).password(authPassword).build().toString());
		if (!validationId.equals(authority.getAuthorityId())) return null;
		// record both transactions in the verification record
		verifications.addTransaction(applicant.getUuid(), authority.getUuid(), applicant);
		verifications.addTransaction(applicant.getUuid(), authority.getUuid(), authority);
		// create block and add to verification block chain
		return this.verificationRecord();
	}

	@GetMapping("/contract/applicant/verify")
	public boolean verifyApplicant(Applicant applicant) {
		boolean result = false;
		Applicant registeredApplicant = Rest.get("http://localhost:8080/applicant/find",Applicant.class,
													"validationId",applicant.getValidationId());
		if (registeredApplicant != null) {
			result = registeredApplicant.getName().equals(applicant.getName());
		}
		return result;
	}

}
