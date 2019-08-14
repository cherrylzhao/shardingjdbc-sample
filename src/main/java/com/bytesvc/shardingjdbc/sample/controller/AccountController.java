package com.bytesvc.shardingjdbc.sample.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bytesvc.shardingjdbc.sample.service.IAccountService;

@RestController
public class AccountController {
	static String TAB_ORDER_0 = "t_order_0";
	static String TAB_ORDER_1 = "t_order_1";

	@Autowired
	private IAccountService accountService;
	@Autowired
	private DataSource dataSource;

	private Map<String, String> ddls = new HashMap<String, String>();
	String url0 = "jdbc:h2:~/demo_ds_0;DB_CLOSE_DELAY=-1";
	String url1 = "jdbc:h2:~/demo_ds_1;DB_CLOSE_DELAY=-1";

	public AccountController() {
		StringBuilder order0 = new StringBuilder();
		order0.append("CREATE TABLE t_order_0 (");
		order0.append("order_id bigserial PRIMARY KEY,");
		order0.append("user_id BIGINT NOT NULL,");
		order0.append("status VARCHAR(50) NULL DEFAULT NULL");
		order0.append(")");

		StringBuilder order1 = new StringBuilder();
		order1.append("CREATE TABLE t_order_1 (");
		order1.append("order_id bigserial PRIMARY KEY,");
		order1.append("user_id BIGINT NOT NULL,");
		order1.append("status VARCHAR(50) NULL DEFAULT NULL");
		order1.append(")");

		ddls.put(TAB_ORDER_0, order0.toString());
		ddls.put(TAB_ORDER_1, order1.toString());
	}

	@RequestMapping(value = "/initialize", method = RequestMethod.POST)
	public void initTableIfNecessary() throws SQLException {
		ShardingDataSource shardingDataSource = (ShardingDataSource) this.dataSource;
		Iterator<Map.Entry<String, DataSource>> itr = shardingDataSource.getDataSourceMap().entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<String, DataSource> entry = itr.next();
			DataSource delegate = entry.getValue();
			Connection conn = null;
			try {
				conn = delegate.getConnection();
				this.createTableIfNecessary(conn, TAB_ORDER_0);
				this.createTableIfNecessary(conn, TAB_ORDER_1);
			} finally {
				this.closeQuietly(conn);
			}
		}
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@Transactional(propagation = Propagation.NEVER) // don't participant in a transaction
	public void createAccount() {
		// AccountServiceImpl -> AccountServiceTemp(REQUIRES_NEW)
		// 1. AccountServiceImpl insert 0
		// 2. AccountServiceTemp insert 2 & 3 (REQUIRES_NEW)
		// 3. AccountServiceImpl insert 0
		// 4. throw new IllegalStateException
		this.accountService.createAccount(0, 1, 2, 3, "INIT");

		// expect: 2 & 3 created; actual: 2, 1, 3 created
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public void deleteAccount(@PathVariable("id") long id) {
		this.accountService.deleteAccount(id);
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public List<Object> queryAccount() {
		return this.accountService.findAllAcount();
	}

	protected void createTableIfNecessary(Connection conn, String table) throws SQLException {
		try {
			conn.createStatement().executeQuery(String.format("select count(*) from %s", table));
		} catch (Exception ex) {
			String ddl = this.ddls.get(table);
			conn.createStatement().execute(ddl);
		}
	}

	protected void closeQuietly(ResultSet closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception ex) {
				// ignore
			}
		}
	}

	protected void closeQuietly(Statement closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception ex) {
				// ignore
			}
		}
	}

	protected void closeQuietly(Connection closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception ex) {
				// ignore
			}
		}
	}

}
