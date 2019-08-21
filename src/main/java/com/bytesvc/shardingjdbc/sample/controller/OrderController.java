package com.bytesvc.shardingjdbc.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bytesvc.shardingjdbc.sample.service.IOrderService;

@RestController
public class OrderController {
	@Autowired
	private IOrderService orderServiceOne;

	@RequestMapping(value = "/order/createOne", method = RequestMethod.POST)
	public void createAccountOne() {
		// OrderServiceOne使用ShardingConfiguration, 分库分表
		this.orderServiceOne.createOrder("INIT");
	}

	@RequestMapping(value = "/order/createTwo", method = RequestMethod.POST)
	public void createAccountTwo() {
		// OrderServiceOne使用GenericConfiguration, 直接写t_order_0, t_order_1表
		// this.orderServiceTwo.createOrder("INIT");
	}

}
