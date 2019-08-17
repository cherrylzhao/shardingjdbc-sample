package com.bytesvc.shardingjdbc.sample.config;

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
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {
	static final String configFile = "/META-INF/sharding-databases-tables.yaml";

	// @org.springframework.context.annotation.Bean("dataSource")
	// public DataSource dataSource(@Autowired TransactionManager
	// transactionManager) throws SQLException, IOException {
	// BasicManagedDataSource dataSource = new BasicManagedDataSource();
	// dataSource.setUrl("jdbc:mysql://localhost:3306/demo_ds_0?serverTimezone=GMT%2B8&useSSL=false");
	// dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
	// dataSource.setUsername("root");
	// dataSource.setPassword("123456");
	// dataSource.setMaxTotal(5);
	// dataSource.setMaxIdle(2);
	// dataSource.setInitialSize(2);
	// dataSource.setTransactionManager(transactionManager);
	//
	// return dataSource;
	// }

	@org.springframework.context.annotation.Bean("dataSource")
	public DataSource dataSource() throws SQLException, IOException {
		File file = new File(CommonConfiguration.class.getResource(configFile).getFile());
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

}
