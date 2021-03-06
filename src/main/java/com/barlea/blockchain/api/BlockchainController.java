package com.barlea.blockchain.api;

import com.barlea.blockchain.domain.Block;
import com.barlea.blockchain.domain.Transaction;
import com.barlea.blockchain.entities.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Private blockchain API
 * 
 * @author L Rick Jones
 *
 */
@RestController
@RequestMapping("/")
public class BlockchainController {

	private final Blockchain register = new Blockchain();

	private final Blockchain requests = new Blockchain();

	private final Blockchain verifications = new Blockchain();

	private final Blockchain communal = new Blockchain();

	@Autowired
	private ObjectMapper mapper;

	public static final String NODE_ACCOUNT_ADDRESS = "0";

	public BlockchainController() throws JsonProcessingException {
	}

	@GetMapping("/blockchain/reset")
	public void resetBlockchain() {
		try {
			register.clear();
			requests.clear();
			verifications.clear();
			communal.clear();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

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

	@GetMapping("/register/active")
	public List<ActiveContract> getActiveContractList() {
		List<ActiveContract> result = new ArrayList<>();

		Block block = register.lastBlock();
		List<Transaction> transactions = block.getTransactions();
		for (Transaction t: transactions) {
			Entity e = t.getEntity();
			if (e instanceof ActiveContract) {
				result.add((ActiveContract) e);
			}
		}

		return result;
	}

	@GetMapping("/register/validate")
	public boolean validateRegister() throws JsonProcessingException {
		return register.validChain();
	}

	@PostMapping("/register/transaction")
	public TransactionResponse registerNewTransaction(@RequestBody @Valid Applicant applicant) {

		int index = register.addTransaction("me", "you", applicant);
		return TransactionResponse.builder().index(index).build();
	}

	private RecordResponse updateRegister(String sender, String receiver, String contractId, int index) {
		Block block = register.lastBlock();
		if (block.getIndex() > 1) {
			for (Transaction t:block.getTransactions()) {
				// copy over every contract except one being updated
				if (!((ActiveContract)t.getEntity()).getContractId().equals(contractId)) {
					register.addTransaction(t.getSender(),t.getRecipient(),t.getEntity());
				}
			}
		}

		ActiveContract activeContract = ActiveContract.builder().contractId(contractId).contractIndex(index).build();
		register.addTransaction(sender,receiver,activeContract);

		//TODO: Does an exception lead to orphaned transactions?
		try {
			return registerRecord();
		} catch (JsonProcessingException e) {
			return null;
		}
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

	@GetMapping("/verification/entities")
	public List<String> getVerifiedEntitiesByIndex(@RequestParam int index) {
		List<String> result = new ArrayList<>();
		Block block = verifications.blockAt(index);
		if (block != null) {
			for (Transaction t: block.getTransactions()) {
				Entity e = t.getEntity();
				if (e instanceof Applicant) {
					result.add("Applicant: " + ((Applicant)e).getName().getLastName() + ", " + ((Applicant)e).getName().getFirstName());
				} else if (e instanceof Authority) {
					result.add("Authority: " + ((Authority)e).getAuthorityType() + ": " + ((Authority)e).getDocumentId());
				} else if (e instanceof Custodian) {
					result.add("Custodian: " + ((Custodian)e).getName());
				} else if (e instanceof Arbiter) {
					result.add("Arbiter: " + ((Arbiter)e).getJurisdiction());
				} else if (e instanceof Patient) {
					result.add("Patient: " + ((Patient)e).getName().getLastName() + ", " + ((Patient)e).getName().getFirstName());
				}
			}
		}
		return result;
	}

	@PostMapping("/verification/transaction")
	public TransactionResponse verificationNewTransaction(@RequestBody @Valid Applicant applicant) {

		int index = verifications.addTransaction("me", "you", applicant);
		return TransactionResponse.builder().index(index).build();
	}

	@GetMapping("/verification/validate")
	public boolean validateVerifications() throws JsonProcessingException {
		return verifications.validChain();
	}

	@GetMapping("/request/fiddle")
	public TransactionResponse updateLastBlock() {
		int index = 1;
		if (requests.getChain().size() > 1) {
			Block lastBlock = requests.blockAt(2);

			for (Transaction t : lastBlock.getTransactions()) {
				Contract contract = (Contract) t.getEntity();
				contract.setOwner("none");
				contract.setContractId("none");
			}

			index = lastBlock.getIndex();
		}

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

	@GetMapping("/request/instance")
	public Contract getContractRequest(Integer index, String id) {
		Contract result = null;
		Block block = requests.blockAt(index);
		if (block == null) return null;
		for (Transaction t: block.getTransactions()) {
			if (((Contract)t.getEntity()).getContractId().equals(id)) {
				Entity e = t.getEntity();
				if (e instanceof Contract) {
					result = (Contract) e;
				}
			}
		}
		return result;
	}

	@GetMapping("/request/history")
	public List<Contract> getContractHistory(@RequestParam String contractId) {
		List<Contract> contracts = new ArrayList<>();
		List<Block> blocks = requests.getChain();
		for (Block b: blocks) {
			Optional<Transaction> transaction = b.getTransactions().stream().filter(t->((Contract)t.getEntity()).getContractId().equals(contractId)).findFirst();
			if (transaction.isPresent()) {
				Entity entity = transaction.get().getEntity();
				if (entity instanceof Contract) {
					contracts.add((Contract)entity);
				}
			}
		}
		return contracts;
	}

	@PostMapping("/request/transaction")
	public TransactionResponse requestNewTransaction(@RequestBody @Valid Applicant applicant) {

		int index = requests.addTransaction("me", "you", applicant);
		return TransactionResponse.builder().index(index).build();
	}

	@GetMapping("/request/validate")
	public boolean validateRequest() throws JsonProcessingException {
		return requests.validChain();
	}

	@PostMapping("/contract/create")
	public Contract createNewContract(@RequestParam String authorityId, @RequestParam String contractId) throws JsonProcessingException {
		Contract contract = Contract.builder().contractId(contractId).currentStatus(Contract.ACCOUNT_REQUEST).owner(Contract.APPLICANT).authorityId(authorityId).build();
		// add contract transaction to requests
		requests.addTransaction(authorityId, null,contract);
		// update the registry that there is a new or updated contract
		updateRegister(authorityId,null,contract.getContractId(),requestRecord().getIndex());
		return contract;
	}

	@PostMapping("/contract/account-request")
	public Contract requestAccessToken(String contractId, String userName, String password, String applicantUuId,String authName, String authPassword) throws JsonProcessingException {
		// find contract, if active
		Contract contract = findContract(contractId);
		if (contract == null) return null;
		// Find applicant
		Applicant applicant = Rest.get("http://localhost:8080/applicant/find",Applicant.class,"uuid",applicantUuId);
		if (applicant == null) return null;
		// Validate applicant
		String applicantId = Hasher.hash(Credentials.builder().userName(userName).password(password).build().toString());
		if (!applicantId.equals(applicant.getValidationId())) return null;
		// Find authority
		Authority authority = Rest.get("http://localhost:8080/authority/find",Authority.class,"uuid",contract.getAuthorityId());
		if (authority == null) return null;
		// validate authority
		String validationId = Hasher.hash(Credentials.builder().userName(authName).password(authPassword).build().toString());
		if (!validationId.equals(authority.getValidationId())) return null;
		// record both transactions in the verification record
		verifications.addTransaction(applicant.getUuid(), authority.getUuid(), applicant);
		verifications.addTransaction(applicant.getUuid(), authority.getUuid(), authority);
		// update contract from copy (modifying contract will change the block and invalidate the chain)
		Contract newContract = contract.toBuilder()
				.applicantId(applicantUuId)
				.currentStatus(Contract.GET_ACCOUNT_INFO)
				.owner(Contract.CUSTODIAN)
				.lastVerification(verificationRecord().getIndex())
				.build();
		// add contract transaction to requests
		requests.addTransaction(applicant.getUuid(), authority.getUuid(),newContract);
		// update the registry that there is a new or updated contract
		updateRegister(applicant.getUuid(),authority.getUuid(),contract.getContractId(),requestRecord().getIndex());
		return contract;
	}

	@PostMapping("/contract/get-account-info")
	public Contract addAccessToken(String contractId, String custodianUuId, String userName, String password, String status, String token) throws JsonProcessingException {
		// find contract, if active
		Contract contract = findContract(contractId);
		if (contract == null) return null;
		// Find custodian
		Custodian custodian = Rest.get("http://localhost:8080/custodian/find",Custodian.class,"uuid",custodianUuId);
		if (custodian == null) return null;
		// Validate custodian
		String custodianId = Hasher.hash(Credentials.builder().userName(userName).password(password).build().toString());
		if (!custodianId.equals(custodian.getValidationId())) return null;
		// record both transactions in the verification record
		verifications.addTransaction(custodianUuId, contract.getApplicantId(), custodian);
		// update contract from copy (modifying contract will change the block and invalidate the chain)
		String lookupStatus = Contract.ACCOUNT_NOT_FOUND;
		if (status.equals("account found")) {
			lookupStatus = Contract.ACCOUNT_FOUND;
		} else if (status.equals("multiple matches")) {
			lookupStatus = Contract.MULTIPLE_ACCOUNTS;
		}
		Contract newContract = contract.toBuilder()
				.custodianId(custodian.getUuid())
				.currentStatus(lookupStatus)
				.owner(Contract.ARBITER)
				.tokenId(token)
				.lastVerification(verificationRecord().getIndex())
				.build();
		// add contract transaction to requests
		requests.addTransaction(custodian.getUuid(), contract.getApplicantId(),newContract);
		// update the registry that there is a new or updated contract
		updateRegister(custodian.getUuid(), contract.getApplicantId(),contract.getContractId(),requestRecord().getIndex());
		return contract;
	}

	@PostMapping("/contract/approve-request")
	public Contract addAccessRequest(String contractId, String arbiterUuId, String userName, String password, String status) throws JsonProcessingException {
		// find contract, if active
		Contract contract = findContract(contractId);
		if (contract == null) return null;
		// Find custodian
		Arbiter arbiter = Rest.get("http://localhost:8080/arbiter/find",Arbiter.class,"uuid",arbiterUuId);
		if (arbiter == null) return null;
		// Validate custodian
		String validationId = Hasher.hash(Credentials.builder().userName(userName).password(password).build().toString());
		if (!validationId.equals(arbiter.getValidationId())) return null;
		// record both transactions in the verification record
		verifications.addTransaction(arbiterUuId, contract.getApplicantId(), arbiter);
		// update contract from copy
		String approvalStatus = Contract.FAILED_REVIEW;
		String nextOwner = Contract.APPLICANT;
		if (status.equals("access approved")) {
			approvalStatus = Contract.PASSED_REVIEW;
			nextOwner = Contract.CUSTODIAN;
		}
		Contract newContract = contract.toBuilder()
				.arbiterId(arbiter.getUuid())
				.currentStatus(approvalStatus)
				.owner(nextOwner)
				.lastVerification(verificationRecord().getIndex())
				.build();
		// add contract transaction to requests
		requests.addTransaction(arbiter.getUuid(), contract.getApplicantId(),newContract);
		// update the registry that there is a new or updated contract
		updateRegister(arbiter.getUuid(), contract.getApplicantId(),contract.getContractId(),requestRecord().getIndex());
		return contract;
	}

	@PostMapping("/contract/approve-access")
	public Contract addApproveAccess(String contractId, String custodianUuId, String userName, String password, String status) throws JsonProcessingException {
		// find contract, if active
		Contract contract = findContract(contractId);
		if (contract == null) return null;
		// Find custodian
		Custodian custodian = Rest.get("http://localhost:8080/custodian/find",Custodian.class,"uuid",custodianUuId);
		if (custodian == null) return null;
		// Validate custodian
		String validationId = Hasher.hash(Credentials.builder().userName(userName).password(password).build().toString());
		if (!validationId.equals(custodian.getValidationId())) return null;
		// record both transactions in the verification record
		verifications.addTransaction(custodianUuId, contract.getApplicantId(), custodian);
		// update contract from copy
		String approvalStatus = Contract.ACCESS_DENIED;
		if (status.equals("access approved")) {
			approvalStatus = Contract.ACCESS_APPROVED;
		}
		Contract newContract = contract.toBuilder()
				.custodianId(custodian.getUuid())
				.currentStatus(approvalStatus)
				.owner(Contract.APPLICANT)
				.lastVerification(verificationRecord().getIndex())
				.build();
		// add contract transaction to requests
		requests.addTransaction(custodian.getUuid(), contract.getApplicantId(),newContract);
		// update the registry that there is a new or updated contract
		updateRegister(custodian.getUuid(), contract.getApplicantId(),contract.getContractId(),requestRecord().getIndex());
		return contract;
	}

	private void postToPublic(Contract contract) {
		Applicant applicant = Rest.get("http://localhost:8080/applicant/find",Applicant.class,"uuid",contract.getApplicantId());
		Arbiter arbiter = Rest.get("http://localhost:8080/arbiter/find",Arbiter.class,"uuid",contract.getArbiterId());
		Custodian custodian = Rest.get("http://localhost:8080/custodian/find",Custodian.class,"uuid",contract.getCustodianId());
		Authority authority = Rest.get("http://localhost:8080/authority/find",Authority.class,"uuid",contract.getAuthorityId());
		Rest.post("http://localhost:8080/public/add",Communal.class,
				"contractId",contract.getContractId(),
				"contractStatus",contract.getCurrentStatus(),
				"authorityDescription",authority.getDescription(),
				"subjectZIP",authority.getSubject().getHomeDomicile().getZip(),
				"applicantName",applicant.getName().getLastName() + ", " + applicant.getName().getFirstName(),
				"applicantZIP","",
				"arbiterName",arbiter.getPersonalInfo().getLastName() + ", " + arbiter.getPersonalInfo().getFirstName(),
				"arbiterJurisdiction",arbiter.getJurisdiction(),
				"custodianName",custodian.getName());
	}

	@PostMapping("/contract/account-access")
	public Contract addAccountAccess(String contractId, String userName, String password, String applicantUuId, String token) throws JsonProcessingException {
		// find contract, if active
		Contract contract = findContract(contractId);
		if (contract == null) return null;
		// Find custodian
		Applicant applicant = Rest.get("http://localhost:8080/applicant/find",Applicant.class,"uuid",applicantUuId);
		if (applicant == null) return null;
		// Validate custodian
		String validationId = Hasher.hash(Credentials.builder().userName(userName).password(password).build().toString());
		if (!validationId.equals(applicant.getValidationId())) return null;
		if (!token.equals(contract.getTokenId())) return null;
		// record both transactions in the verification record
		verifications.addTransaction(applicantUuId, contract.getCustodianId(), applicant);
		verifications.addTransaction(applicantUuId,contract.getCustodianId(),contract);
		// update contract from copy
		Contract newContract = contract.toBuilder()
				.applicantId(applicant.getUuid())
				.currentStatus(Contract.DATA_ACCESSED)
				.owner(Contract.APPLICANT)
				.lastVerification(verificationRecord().getIndex())
				.build();
		// add contract transaction to requests
		requests.addTransaction(applicant.getUuid(), contract.getCustodianId(),newContract);
		// update the registry that there is a new or updated contract
		updateRegister(applicant.getUuid(), contract.getCustodianId(),contract.getContractId(),requestRecord().getIndex());
		postToPublic(newContract);
		return contract;
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

	@GetMapping("/contract/find")
	public Contract findContract(@RequestParam String contractId) {
		// get block index
		Block block = register.lastBlock();
		Optional<Transaction> transaction = block.getTransactions().stream().filter(t->((ActiveContract)t.getEntity()).getContractId().equals(contractId)).findFirst();
		if (!transaction.isPresent()) return null;
		// get contract detail
		ActiveContract active = (ActiveContract)transaction.get().getEntity();
		Block contractBlock = requests.blockAt(active.getContractIndex());
		if (contractBlock == null) return null;
		Optional<Transaction> contractTransaction = contractBlock.getTransactions().stream().filter(t->((Contract)t.getEntity()).getContractId().equals(contractId)).findFirst();
		return contractTransaction.map(value -> (Contract) value.getEntity()).orElse(null);
	}

	@GetMapping("/utils/validationId")
	public String generateValidationId(@RequestParam String userName, @RequestParam String password) {
		String result;
		try {
			result = Hasher.hash(Credentials.builder().userName(userName).password(password).build().toString());
		} catch (JsonProcessingException e) {
			result = "Invalid";
		}
		return result;
	}

	@PostMapping("/public/add")
	public Communal addPublicBlock(String contractId, String contractStatus, String authorityDescription, String subjectZIP,String applicantName, String applicantZIP, String arbiterName, String arbiterJurisdiction, String custodianName) throws JsonProcessingException {
		Communal communalRecord = Communal.builder()
				.contractId(contractId)
				.contractStatus(contractStatus)
				.authorityDescription(authorityDescription)
				.subjectZIP(subjectZIP)
				.applicantName(applicantName)
				.applicantZIP(applicantZIP)
				.arbiterName(arbiterName)
				.arbiterJurisdiction(arbiterJurisdiction)
				.custodianName(custodianName)
				.build();

		// add contract transaction to requests
		communal.addTransaction(contractId, contractId, communalRecord);
		// (1) - Calculate the Proof of Work
		Block lastBlock = communal.lastBlock();

		Long lastProof = lastBlock.getProof();

		Long proof = BlockProofOfWorkGenerator.proofOfWork(lastProof);

		// (2) - Add pending transactions to new block
		communal.createBlock(proof, lastBlock.hash(mapper));

		return communalRecord;
	}

	@GetMapping("/public/chain")
	public ChainResponse getPublicChain() {
		return ChainResponse.builder().chain(communal.getChain()).length(communal.getChain().size()).build();
	}

	/* HIPAA Rules */
	@PostMapping("/hipaa/request-records") // new contract request
	public Contract requestRecords(String userName, String password, String applicantUuId, SubjectRequest application, String custodianUuId) throws JsonProcessingException {
		// Find applicant
		Applicant applicant = Rest.get("http://localhost:8080/applicant/find",Applicant.class,"uuid",applicantUuId);
		if (applicant == null) return null;
		// Validate applicant
		String applicantId = Hasher.hash(Credentials.builder().userName(userName).password(password).build().toString());
		if (!applicantId.equals(applicant.getValidationId())) return null;
		Custodian custodian = Rest.get("http://localhost:8080/custodian/find",Custodian.class,"uuid",custodianUuId);
		if (custodian == null) return null;
		// record applicant and custodian record in verfications
		verifications.addTransaction(applicant.getUuid(),custodian.getUuid(), applicant);
		verifications.addTransaction(applicant.getUuid(),custodian.getUuid(), custodian);
		Contract contract = Contract.builder()
				.applicantId(applicantUuId)
				.custodianId(custodianUuId)
				.contractId(UUID.randomUUID().toString())
				.currentStatus(Contract.RECORD_REQUEST)
				.owner(Contract.CUSTODIAN)
				.lastVerification(verificationRecord().getIndex())
				.application(application)
				.build();
		// update contract from copy (modifying contract will change the block and invalidate the chain)

		// add contract transaction to requests
		requests.addTransaction(applicant.getUuid(), custodian.getUuid(),contract);
		// update the registry that there is a new or updated contract
		updateRegister(applicant.getUuid(),custodian.getUuid(),contract.getContractId(),requestRecord().getIndex());
		return contract;
	}

	@PostMapping("/hipaa/request-authorization")
	public Contract requestAuthorization(String custodianUuId, String userName, String password, String contractId, String arbiterUuId, String token) throws JsonProcessingException {
		// find contract, if active
		Contract contract = findContract(contractId);
		if (contract == null) return null;
		// Validate Custodian
		Custodian custodian = Rest.get("http://localhost:8080/custodian/find",Custodian.class,"uuid",custodianUuId);
		if (custodian == null) return null;
		String validationId = Hasher.hash(Credentials.builder().userName(userName).password(password).build().toString());
		if (!validationId.equals(custodian.getValidationId())) return null;
		// Find patient
		Patient patient = Rest.get("http://localhost:8080/patient/find",Patient.class,"uuid",arbiterUuId);
		if (patient == null) return null;

		// record applicant and custodian record in verfications
		verifications.addTransaction(custodian.getUuid(),patient.getUuid(), patient);
		verifications.addTransaction(custodian.getUuid(),patient.getUuid(), custodian);
		Contract newContract = contract.toBuilder()
				.arbiterId(arbiterUuId)
				.currentStatus(Contract.REQUEST_AUTHORIZATION)
				.owner(Contract.ARBITER)
				.tokenId(token)
				.lastVerification(verificationRecord().getIndex())
				.build();
		// update contract from copy (modifying contract will change the block and invalidate the chain)
		requests.addTransaction(custodian.getUuid(), patient.getUuid(),newContract);
		// update the registry that there is a new or updated contract
		updateRegister(custodian.getUuid(),patient.getUuid(),contract.getContractId(),requestRecord().getIndex());
		return contract;
	}

	@PostMapping("/hipaa/record-not-found")
	public Contract recordNotFound(String custodianUuId, String userName, String password, String contractId) throws JsonProcessingException {
		// find contract, if active
		Contract contract = findContract(contractId);
		if (contract == null) return null;
		// Validate Custodian
		Custodian custodian = Rest.get("http://localhost:8080/custodian/find",Custodian.class,"uuid",custodianUuId);
		if (custodian == null) return null;
		String validationId = Hasher.hash(Credentials.builder().userName(userName).password(password).build().toString());
		if (!validationId.equals(custodian.getValidationId())) return null;

		// record applicant and custodian record in verfications
		verifications.addTransaction(custodian.getUuid(),contract.getApplicantId(), custodian);
		Contract newContract = contract.toBuilder()
				.currentStatus(Contract.RECORD_NOT_FOUND)
				.owner(Contract.APPLICANT)
				.lastVerification(verificationRecord().getIndex())
				.build();
		// update contract from copy (modifying contract will change the block and invalidate the chain)
		requests.addTransaction(custodian.getUuid(), contract.getApplicantId(),newContract);
		// update the registry that there is a new or updated contract
		updateRegister(custodian.getUuid(),contract.getApplicantId(),contract.getContractId(),requestRecord().getIndex());
		return contract;
	}

	@PostMapping("/hipaa/access-approval")
	public Contract accessApproval(String arbiterUuId, String userName, String password, String contractId, boolean approved) throws JsonProcessingException {
		// find contract, if active
		Contract contract = findContract(contractId);
		if (contract == null) return null;
		// Validate Custodian
		Patient patient = Rest.get("http://localhost:8080/patient/find",Patient.class,"uuid",arbiterUuId);
		if (patient == null) return null;
		String validationId = Hasher.hash(Credentials.builder().userName(userName).password(password).build().toString());
		if (!validationId.equals(patient.getValidationId())) return null;

		// record applicant and custodian record in verifications
		verifications.addTransaction(patient.getUuid(),contract.getApplicantId(), patient);
		Contract newContract = contract.toBuilder()
				.currentStatus(approved?Contract.APPLICANT_AUTHORIZED:Contract.APPLICANT_NOT_AUTHORIZED)
				.owner(Contract.APPLICANT)
				.lastVerification(verificationRecord().getIndex())
				.build();
		// update contract from copy (modifying contract will change the block and invalidate the chain)
		requests.addTransaction(patient.getUuid(), contract.getApplicantId(),newContract);
		// update the registry that there is a new or updated contract
		updateRegister(patient.getUuid(),contract.getApplicantId(),contract.getContractId(),requestRecord().getIndex());
		return contract;
	}

	@PostMapping("/hipaa/record-access")
	public Contract addRecordAccess(String contractId, String userName, String password, String applicantUuId, String token) throws JsonProcessingException {
		// find contract, if active
		Contract contract = findContract(contractId);
		if (contract == null) return null;
		// Find custodian
		Applicant applicant = Rest.get("http://localhost:8080/applicant/find",Applicant.class,"uuid",applicantUuId);
		if (applicant == null) return null;
		// Validate custodian
		String validationId = Hasher.hash(Credentials.builder().userName(userName).password(password).build().toString());
		if (!validationId.equals(applicant.getValidationId())) return null;
		if (!token.equals(contract.getTokenId())) return null;
		// record both transactions in the verification record
		verifications.addTransaction(applicantUuId, contract.getCustodianId(), applicant);
		verifications.addTransaction(applicantUuId,contract.getCustodianId(),contract);
		// update contract from copy
		Contract newContract = contract.toBuilder()
				.applicantId(applicant.getUuid())
				.currentStatus(Contract.RECORD_DELIVERED)
				.owner(Contract.APPLICANT)
				.lastVerification(verificationRecord().getIndex())
				.build();
		// add contract transaction to requests
		requests.addTransaction(applicant.getUuid(), contract.getCustodianId(),newContract);
		// update the registry that there is a new or updated contract
		updateRegister(applicant.getUuid(), contract.getCustodianId(),contract.getContractId(),requestRecord().getIndex());
		return contract;
	}

}
