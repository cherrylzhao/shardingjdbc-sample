package com.bytesvc.shardingjdbc.sample.main;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.sql.DataSource;

import org.apache.shardingsphere.shardingjdbc.api.yaml.YamlShardingDataSourceFactory;
import org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication(scanBasePackages = "com.bytesvc.shardingjdbc.sample")
@EnableAutoConfiguration(exclude = { MongoAutoConfiguration.class, DataSourceAutoConfiguration.class,
		SpringBootConfiguration.class })
@MapperScan("com.bytesvc.shardingjdbc.sample.dao")
@Configuration
public class ShardingJdbcSampleBootstrap {
	private static String configFile = "/META-INF/sharding-databases-tables.yaml";

	@org.springframework.context.annotation.Bean("dataSource")
	public DataSource dataSource() throws SQLException, IOException {
		File file = new File(ShardingJdbcSampleBootstrap.class.getResource(configFile).getFile());
		DataSource dataSource = YamlShardingDataSourceFactory.createDataSource(file);
		return dataSource;
	}

	@org.springframework.context.annotation.Bean("sqlSessionFactory")
	public SqlSessionFactoryBean sqlSessionFactory(@Autowired @Qualifier("dataSource") DataSource dataSource) {
		SqlSessionFactoryBean ssfb = new SqlSessionFactoryBean();
		ssfb.setDataSource(dataSource);
		return ssfb;
	}

	@org.springframework.context.annotation.Bean
	public FilterRegistrationBean<Filter> sstxTypeFilter() {
		FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<Filter>();
		registration.setName("sstxTypeFilter");
		registration.setFilter(new Filter() {
			public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
					throws IOException, ServletException {
				TransactionType transactionType = TransactionTypeHolder.get();
				try {
					TransactionTypeHolder.set(TransactionType.XA);
					chain.doFilter(request, response);
				} finally {
					TransactionTypeHolder.set(transactionType);
				}
			}
		});
		registration.setOrder(1);
		registration.addUrlPatterns("/*");
		return registration;
	}

	public static void main(String[] args) {
		new SpringApplicationBuilder(ShardingJdbcSampleBootstrap.class).bannerMode(Banner.Mode.OFF).run(args);
		System.out.println("ShardingJdbcSampleBootstrap started!");
	}

}
