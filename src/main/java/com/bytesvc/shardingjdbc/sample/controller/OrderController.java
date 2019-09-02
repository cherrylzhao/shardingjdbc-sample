package com.bytesvc.shardingjdbc.sample.controller;

import com.bytesvc.shardingjdbc.sample.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
	
	@Autowired
	private IOrderService orderServiceOne;
	
	@Autowired(required = false)
	@Qualifier("rawLocalOrder")
	private IOrderService orderServiceThree;

	@RequestMapping(value = "/order/createOne", method = RequestMethod.POST)
	public void createAccountOne() {
		// OrderServiceOne使用ShardingConfiguration, 分库分表
		this.orderServiceOne.createOrder("INIT");
	}

	@RequestMapping(value = "/order/createTwo", method = RequestMethod.POST)
	public void createAccountTwo() {
		// OrderServiceTwo使用GenericConfiguration, 直接写t_order_0, t_order_1表
		// this.orderServiceTwo.createOrder("INIT");
	}
	
	@RequestMapping(value = "/order/createThree", method = RequestMethod.POST)
	public void createAccountThree() {
		// OrderServiceOne使用ShardingConfiguration, 分库分表
		this.orderServiceThree.createOrder("INIT");
	}
}
