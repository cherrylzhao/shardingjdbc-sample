package com.bytesvc.shardingjdbc.sample.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytesvc.shardingjdbc.sample.service.IOrderService;

@Primary
@Service
public class DefaultOrderServiceImpl implements IOrderService {
	@Autowired
	private DataSource shardingDataSource;
	@Qualifier("requiresNewOrderService")
	@Autowired
	private IOrderService requiresNewOrderService;

	@ShardingTransactionType(TransactionType.XA)
	@Transactional
	public void createOrder(String status) {
		this.doCreateOrder(0, status);
		this.doCreateOrder(3, status);
		this.requiresNewOrderService.createOrder(status);
		this.doCreateOrder(4, status);

		throw new IllegalStateException("rollback");
	}

	private void doCreateOrder(long userId, String status) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = this.shardingDataSource.getConnection();
			// System.out.printf("conn: %s%n", conn);
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

	@Transactional
	public void deleteOrder(long userId) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = this.shardingDataSource.getConnection();
			stmt = conn.prepareStatement("DELETE FROM t_order WHERE user_id = ?");
			stmt.setLong(1, userId);
			stmt.executeUpdate();
		} catch (SQLException error) {
			throw new IllegalStateException(error.getMessage(), error);
		} finally {
			this.closeQuietly(stmt);
			this.closeQuietly(conn);
		}
	}

	public List<Long> listUserId() {
		List<Long> idList = new ArrayList<Long>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rlst = null;
		try {
			conn = this.shardingDataSource.getConnection();
			stmt = conn.prepareStatement("SELECT user_id FROM t_order");
			rlst = stmt.executeQuery();
			while (rlst.next()) {
				long userId = rlst.getLong("user_id");
				idList.add(userId);
			}
		} catch (SQLException error) {
			throw new IllegalStateException(error.getMessage(), error);
		} finally {
			this.closeQuietly(rlst);
			this.closeQuietly(stmt);
			this.closeQuietly(conn);
		}
		return idList;
	}

}
