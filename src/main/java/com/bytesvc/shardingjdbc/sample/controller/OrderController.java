package com.bytesvc.shardingjdbc.sample.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bytesvc.shardingjdbc.sample.service.IOrderService;

@RestController
public class OrderController {
	@Autowired
	private IOrderService orderServiceOne;
	@Qualifier("requiresNewOrderService")
	@Autowired
	private IOrderService orderServiceTwo;

	@RequestMapping(value = "/order/createOne", method = RequestMethod.POST)
	public void createAccountOne() {
		this.orderServiceOne.createOrder("INIT");
	}

	@RequestMapping(value = "/order/createTwo", method = RequestMethod.POST)
	public void createAccountTwo() {
		this.orderServiceTwo.createOrder("INIT");
	}

	@RequestMapping(value = "/order/delete/{id}", method = RequestMethod.POST)
	public void deleteAccount(@PathVariable("id") Long id) {
		this.orderServiceOne.deleteOrder(id);
	}

	@RequestMapping(value = "/order/list", method = RequestMethod.POST)
	public List<Long> queryAccount() {
		return this.orderServiceOne.listUserId();
	}

}
