package com.bytesvc.shardingjdbc.sample.service;

import java.util.List;

public interface IOrderService {

	public void createOrder(String status);

	public void deleteOrder(long userId);

	public List<Long> listUserId();

}
