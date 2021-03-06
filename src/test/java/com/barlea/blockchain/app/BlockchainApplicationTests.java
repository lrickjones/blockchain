package com.barlea.blockchain.app;

import com.barlea.blockchain.api.BlockchainController;
import com.barlea.blockchain.domain.Transaction;
import com.barlea.blockchain.entities.Entity;
import com.barlea.blockchain.model.ChainResponse;
import com.barlea.blockchain.model.RecordResponse;
import com.barlea.blockchain.model.TransactionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Objects;


/**
 * 
 * @author Praveendra Singh
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class BlockchainApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private ObjectMapper mapper;

	private RestTemplate client;

	private String baseUrl;

	@Before
	public void init() {

		OkHttp3ClientHttpRequestFactory rf = new OkHttp3ClientHttpRequestFactory();
		rf.setConnectTimeout(1000);
		rf.setReadTimeout(1000 * 1000);

		client = new RestTemplate(rf);

		baseUrl = "http://localhost:" + port;
	}

	@Test
	public void mineBlocksAddTransactionsAndFetchChainDetailsTest() {

		int numberOfBlocksToMine = 3;

		for (int i = 0; i < numberOfBlocksToMine; i++) {

			Transaction transaction = Transaction.builder().sender("s" + i).recipient("r" + i)
					.entity(new Entity()).build();

			TransactionResponse txnResponse = client.postForObject(baseUrl + "/transactions", transaction,
					TransactionResponse.class);

			RecordResponse mineResponse = client.getForObject(baseUrl + "/mine", RecordResponse.class);

			log.info("mineResponse={}, txnResponse={}", mineResponse, txnResponse);
		}

		ChainResponse chainResponse = client.getForObject(baseUrl + "/chain", ChainResponse.class);
		log.info("chainResponse={}", chainResponse);

		assert chainResponse != null;
		Assert.assertEquals(Long.valueOf(numberOfBlocksToMine + 1), Long.valueOf(chainResponse.getLength()));
	}

	@Test
	public void blockMiningTest() throws IOException {

		Request request = new Request.Builder().url(baseUrl + "/mine").build();
		Response response = new OkHttpClient().newCall(request).execute();

		if (response.code() != 200) {
			Assert.fail();
		}

		RecordResponse mined = mapper.readValue(Objects.requireNonNull(response.body()).string(),RecordResponse.class);
		Assert.assertNotNull(mined);
		Assert.assertEquals(BlockchainController.NODE_ACCOUNT_ADDRESS, mined.getTransactions().get(0).getSender());

	}

}
