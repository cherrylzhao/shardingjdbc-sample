package com.bytesvc.shardingjdbc.sample.config;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.apache.shardingsphere.shardingjdbc.api.yaml.YamlShardingDataSourceFactory;
import org.apache.shardingsphere.transaction.xa.yaml.config.YamlShardingConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionManagerImple;
import com.arjuna.ats.internal.jta.transaction.arjunacore.UserTransactionImple;

public class NarayanaConfiguration {
	static final String configFile = "/META-INF/sharding-databases-tables.yaml";

	@org.springframework.context.annotation.Bean("shardingDataSource")
	public DataSource shardingDataSource() throws SQLException, IOException {
		File file = new File(ShardingConfiguration.class.getResource(configFile).getFile());
		return YamlShardingDataSourceFactory.createDataSource(file, YamlShardingConfiguration.class);
	}

	@org.springframework.context.annotation.Bean
	public UserTransactionImple userTransaction() {
		return new UserTransactionImple();
	}

	@org.springframework.context.annotation.Bean
	public TransactionManager transactionManager() {
		return new TransactionManagerImple();
	}

	@org.springframework.context.annotation.Bean
	public PlatformTransactionManager jtaTransactionManager(UserTransaction userTransaction,
			TransactionManager transactionManager) {
		return new JtaTransactionManager(userTransaction, transactionManager);
	}

}
