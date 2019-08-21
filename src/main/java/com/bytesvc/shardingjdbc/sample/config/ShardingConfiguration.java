package com.bytesvc.shardingjdbc.sample.config;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.shardingsphere.shardingjdbc.api.yaml.YamlShardingDataSourceFactory;

/**
 * 配置shardingjdbc数据源, 用于/order/createOne
 */
public class ShardingConfiguration {
	static final String configFile = "/META-INF/sharding-databases-tables.yaml";

	@org.springframework.context.annotation.Bean("shardingDataSource")
	@org.springframework.context.annotation.Primary
	public DataSource shardingDataSource() throws SQLException, IOException {
		File file = new File(ShardingConfiguration.class.getResource(configFile).getFile());
		DataSource dataSource = YamlShardingDataSourceFactory.createDataSource(file);
		return dataSource;
	}

}
