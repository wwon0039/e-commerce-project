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
	}
}
