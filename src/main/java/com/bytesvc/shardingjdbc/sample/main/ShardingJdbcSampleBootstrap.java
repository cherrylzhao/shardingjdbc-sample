package com.bytesvc.shardingjdbc.sample.main;

import com.bytesvc.shardingjdbc.sample.config.RawLocalTransactionConfiguration;
import org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = { "com.bytesvc.shardingjdbc.sample", "io.shardingsphere.transaction.aspect" })
@EnableAutoConfiguration(exclude = { SpringBootConfiguration.class, DataSourceAutoConfiguration.class })
@EnableTransactionManagement
//@Import(ShardingConfiguration.class)
@Import(RawLocalTransactionConfiguration.class)
public class ShardingJdbcSampleBootstrap {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ShardingJdbcSampleBootstrap.class).bannerMode(Banner.Mode.OFF).run(args);
		System.out.println("ShardingJdbcSampleBootstrap started!");
	}

}
