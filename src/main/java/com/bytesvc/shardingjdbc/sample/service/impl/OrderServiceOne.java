package com.bytesvc.shardingjdbc.sample.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytesvc.shardingjdbc.sample.service.IOrderService;

@Primary
@Service
public class OrderServiceOne implements IOrderService {
	@Qualifier("shardingDataSource")
	@Autowired(required = false)
	private DataSource shardingDataSource;

	@ShardingTransactionType(TransactionType.XA)
	@Transactional
	public void createOrder(String status) {
		this.doCreateOrder(0, status);
		this.doCreateOrder(1, status);

		// 期望事务回滚, 但事务实际被提交
		// throw new IllegalStateException("rollback");
	}

	private void doCreateOrder(long userId, String status) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DataSourceUtils.getConnection(this.shardingDataSource);
			// conn = this.shardingDataSource.getConnection();
			stmt = conn.prepareStatement("INSERT INTO t_order (user_id, status) VALUES (?, ?)");
			stmt.setLong(1, userId);
			stmt.setString(2, status);
			stmt.executeUpdate();
		} catch (SQLException error) {
			throw new IllegalStateException(error.getMessage(), error);
		} finally {
			this.closeQuietly(stmt);
			this.closeQuietly(conn);
		}
	}

	private void closeQuietly(AutoCloseable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception error) {
				// ignore
			}
		}
	}

}
