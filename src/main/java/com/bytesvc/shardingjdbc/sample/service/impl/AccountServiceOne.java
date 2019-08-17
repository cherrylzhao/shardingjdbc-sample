package com.bytesvc.shardingjdbc.sample.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytesvc.shardingjdbc.sample.service.IAccountService;

@Primary
@Service
public class AccountServiceOne implements IAccountService {
	@Autowired
	private DataSource dataSource;

	@Transactional
	public void createAccount(long userId, String status) {
		this.doCreateAccount(userId, status);
		throw new IllegalStateException("rollback");
	}

	private void doCreateAccount(long userId, String status) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = this.dataSource.getConnection();
			stmt = conn.prepareStatement("INSERT INTO t_order (user_id, status) VALUES (?, ?)");
			stmt.setLong(1, userId);
			stmt.setString(2, status);
			stmt.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
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

	public void deleteAccount(long orderId) {
		// this.accountDao.deleteAccount(orderId);
	}

	public List<Object> findAllAcount() {
		return null; // return this.accountDao.findAllAcount();
	}

}
