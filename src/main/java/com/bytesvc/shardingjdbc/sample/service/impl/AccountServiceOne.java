package com.bytesvc.shardingjdbc.sample.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytesvc.shardingjdbc.sample.dao.AccountMapper;
import com.bytesvc.shardingjdbc.sample.service.IAccountService;

@Primary
@Service
public class AccountServiceOne implements IAccountService {
	@Autowired
	private AccountMapper accountDao;
	@Qualifier("accountServiceTwo")
	@Autowired
	private IAccountService accountTest;

	@Transactional
	public void createAccount(long userId1, long userId2, long userId3, long userId4, String status) {
		this.accountDao.createAccount(userId1, status);
		this.accountTest.createAccount(-1, -1, userId3, userId4, status);
		this.accountDao.createAccount(userId2, status);
		throw new IllegalStateException("rollback");
	}

	public void deleteAccount(long orderId) {
		this.accountDao.deleteAccount(orderId);
	}

	public List<Object> findAllAcount() {
		return this.accountDao.findAllAcount();
	}

}
