package com.bytesvc.shardingjdbc.sample.service;

import java.util.List;

public interface IAccountService {

	public void createAccount(long userId, String status);

	public void deleteAccount(long userId);

	public List<Object> findAllAcount();

}
