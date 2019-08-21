package com.bytesvc.shardingjdbc.sample.config;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.apache.commons.dbcp2.managed.BasicManagedDataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.jta.JtaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.StringUtils;

import com.atomikos.icatch.config.UserTransactionService;
import com.atomikos.icatch.config.UserTransactionServiceImp;
import com.atomikos.icatch.jta.UserTransactionManager;

/**
 * 配置普通数据源, 用于/order/createTwo
 */
@EnableConfigurationProperties({ AtomikosProperties.class, JtaProperties.class })
@EnableAutoConfiguration(exclude = { JtaAutoConfiguration.class })
public class GenericConfiguration implements InitializingBean {
	private final JtaProperties jtaProperties;

	public GenericConfiguration(JtaProperties jtaProperties) {
		this.jtaProperties = jtaProperties;
	}

	@Bean(initMethod = "init", destroyMethod = "shutdownWait")
	@ConditionalOnMissingBean(UserTransactionService.class)
	public UserTransactionServiceImp userTransactionService(AtomikosProperties atomikosProperties) {
		Properties properties = new Properties();
		if (StringUtils.hasText(this.jtaProperties.getTransactionManagerId())) {
			properties.setProperty("com.atomikos.icatch.tm_unique_name", this.jtaProperties.getTransactionManagerId());
		}
		properties.setProperty("com.atomikos.icatch.log_base_dir", "./");
		properties.putAll(atomikosProperties.asProperties());
		return new UserTransactionServiceImp(properties);
	}

	@org.springframework.context.annotation.Bean(initMethod = "init", destroyMethod = "close")
	public UserTransactionManager atomikosTransactionManager(UserTransactionService userTransactionService)
			throws Exception {
		UserTransactionManager manager = new UserTransactionManager();
		manager.setStartupTransactionService(false);
		manager.setForceShutdown(true);
		return manager;
	}

	@org.springframework.context.annotation.Bean
	public JtaTransactionManager transactionManager(UserTransaction userTransaction,
			TransactionManager transactionManager) {
		return new JtaTransactionManager(userTransaction, transactionManager);
	}

	@org.springframework.context.annotation.Bean("dataSourceOne")
	public DataSource dataSourceOne(TransactionManager transactionManager) throws SQLException, IOException {
		BasicManagedDataSource dataSource = new BasicManagedDataSource();
		dataSource.setUrl("jdbc:mysql://localhost:3306/demo_ds_0?serverTimezone=GMT%2B8&useSSL=false");
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUsername("root");
		dataSource.setPassword("123456");
		dataSource.setMaxTotal(5);
		dataSource.setMaxIdle(2);
		dataSource.setInitialSize(2);
		dataSource.setTransactionManager(transactionManager);

		return dataSource;
	}

	@org.springframework.context.annotation.Bean("dataSourceTwo")
	public DataSource dataSourceTwo(TransactionManager transactionManager) throws SQLException, IOException {
		BasicManagedDataSource dataSource = new BasicManagedDataSource();
		dataSource.setUrl("jdbc:mysql://localhost:3306/demo_ds_1?serverTimezone=GMT%2B8&useSSL=false");
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUsername("root");
		dataSource.setPassword("123456");
		dataSource.setMaxTotal(5);
		dataSource.setMaxIdle(2);
		dataSource.setInitialSize(2);
		dataSource.setTransactionManager(transactionManager);

		return dataSource;
	}

	public void afterPropertiesSet() throws Exception {
	}

}
