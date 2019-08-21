package com.bytesvc.shardingjdbc.sample.config;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.shardingsphere.shardingjdbc.api.yaml.YamlShardingDataSourceFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * 配置shardingjdbc数据源, 用于/order/createOne
 */
@EnableAutoConfiguration(exclude = { JtaAutoConfiguration.class })
public class ShardingConfiguration {
	static final String configFile = "/META-INF/sharding-databases-tables.yaml";

	@org.springframework.context.annotation.Bean("shardingDataSource")
	@org.springframework.context.annotation.Primary
	public DataSource shardingDataSource() throws SQLException, IOException {
		File file = new File(ShardingConfiguration.class.getResource(configFile).getFile());
		DataSource dataSource = YamlShardingDataSourceFactory.createDataSource(file);
		return dataSource;
	}

	@org.springframework.context.annotation.Bean
	public DataSourceTransactionManager transactionManager(DataSource shardingDataSource) {
		return new DataSourceTransactionManager(shardingDataSource);
	}

}
