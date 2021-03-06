package com.barlea.blockchain.app;

import com.barlea.blockchain.config.BlockchainConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Starts WebService and initializes SpringBoot framework.
 * 
 * @author Praveendra Singh
 *
 */
@SpringBootApplication(scanBasePackageClasses = { BlockchainConfig.class })
public class BlockchainApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlockchainApplication.class, args);
	}
}
