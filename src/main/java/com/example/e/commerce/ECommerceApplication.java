package com.example.e.commerce;

import com.example.e.commerce.mapper.OrderMapper;
import com.example.e.commerce.model.Order;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class ECommerceApplication implements CommandLineRunner{

	private final OrderMapper orderMapper;

	public ECommerceApplication(OrderMapper orderMapper) {
		this.orderMapper = orderMapper;
	}

	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(this.orderMapper);

//		Order newItem = new Order(20,5);

//		Map<String, Object> newItem = new HashMap<String, Object>();
//
//		newItem.put("total_amount", 20);
//		newItem.put("total_discount_amount", 5);
//		int createdCount = orderMapper.insertOrder(newItem);
//		System.out.println("Created items count : " + createdCount);
	}
}
