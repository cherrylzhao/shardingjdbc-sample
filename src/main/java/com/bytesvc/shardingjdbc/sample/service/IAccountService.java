package com.bytesvc.shardingjdbc.sample.service;

import java.util.List;

public interface IAccountService {

	public void createAccount(long userId1, long userId2, long userId3, long userId4, String status);

	public void deleteAccount(long userId);

	public List<Object> findAllAcount();

}
