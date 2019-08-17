package com.bytesvc.shardingjdbc.sample.main;

import org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.bytesvc.shardingjdbc.sample")
@EnableAutoConfiguration(exclude = { SpringBootConfiguration.class, DataSourceAutoConfiguration.class	 })
@EnableTransactionManagement
@MapperScan("com.bytesvc.shardingjdbc.sample.dao")
public class ShardingJdbcSampleBootstrap {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ShardingJdbcSampleBootstrap.class).bannerMode(Banner.Mode.OFF).run(args);
		System.out.println("ShardingJdbcSampleBootstrap started!");
	}

}
