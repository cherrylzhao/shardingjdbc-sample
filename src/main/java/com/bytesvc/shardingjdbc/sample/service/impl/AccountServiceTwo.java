package com.bytesvc.shardingjdbc.sample.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bytesvc.shardingjdbc.sample.dao.AccountMapper;
import com.bytesvc.shardingjdbc.sample.service.IAccountService;

@Service("accountServiceTwo")
public class AccountServiceTwo implements IAccountService {
	@Autowired
	private AccountMapper accountDao;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void createAccount(long userId, String status) {
		this.accountDao.createAccount(userId, "TEMP");
	}

	public List<Object> findAllAcount() {
		return null;
	}

	public void deleteAccount(long orderId) {
	}

}
