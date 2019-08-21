package com.bytesvc.shardingjdbc.sample.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytesvc.shardingjdbc.sample.service.IOrderService;

@Service("orderServiceTwo")
public class OrderServiceTwo implements IOrderService {
	@Qualifier("dataSourceOne")
	@Autowired(required = false)
	private DataSource dataSourceOne;
	@Qualifier("dataSourceTwo")
	@Autowired(required = false)
	private DataSource dataSourceTwo;

	@Transactional
	public void createOrder(String status) {
		this.doCreateOrderOne(0, status);
		this.doCreateOrderTwo(1, status);
		throw new IllegalStateException("rollback");
	}

	private void doCreateOrderOne(long userId, String status) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = this.dataSourceOne.getConnection();
			stmt = conn.prepareStatement("INSERT INTO t_order_0 (user_id, status) VALUES (?, ?)");
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

	private void doCreateOrderTwo(long userId, String status) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = this.dataSourceTwo.getConnection();
			stmt = conn.prepareStatement("INSERT INTO t_order_1 (user_id, status) VALUES (?, ?)");
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
