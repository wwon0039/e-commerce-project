package com.example.e.commerce.controller;

import com.example.e.commerce.mapper.OrderMapper;
import com.example.e.commerce.model.Order;
import com.example.e.commerce.model.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private final OrderMapper orderMapper;

    public OrderController(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    // insert a new order and its details
    @PostMapping("/add")
    public String addOrder(@RequestBody Map<String, Object> requestData) {
        // Create Order model
        Order order = new Order();

        // Create Order Detail model
        OrderDetail orderDetail = new OrderDetail();

        // Loop through each key, value pair to set the appropriate data for Order and OrderDetail models
        Integer createdOrderCount = 0;
        Integer createdOrderDetailsCount = 0;
        for (Map.Entry<String, Object> entry : requestData.entrySet()) {
            if (entry.getKey().equals("total_amount")) {
                order.setTotalAmount(Double.valueOf(String.valueOf(entry.getValue())));
            } else if (entry.getKey().equals("total_discount_amount")) {
                order.setTotalDiscountAmount(Double.valueOf(String.valueOf(entry.getValue())));
            } else if (entry.getKey().equals("detail")) {
                // Insert Order to DB at this point, since there could be an error when we are inserting orderDetails later
                orderMapper.insertOrder(order);
                createdOrderCount += 1;

                // we have an ArrayList<Map<String, Object>> type because the request body
                // is a json structure that contains an array of key, value pairs
                ArrayList<Map<String, Object>> orderDetailList = (ArrayList<Map<String, Object>>) entry.getValue();
                for (int i = 0; i < orderDetailList.size(); i++) {
                    for (Map.Entry<String, Object> orderDetailEntry : orderDetailList.get(i).entrySet()) {
                        if (orderDetailEntry.getKey().equals("order_id")) {
                            orderDetail.setOrderId(Integer.valueOf(String.valueOf(orderDetailEntry.getValue())));
                        } else if (orderDetailEntry.getKey().equals("product_name")) {
                            orderDetail.setProductName(orderDetailEntry.getValue().toString());
                        } else if (orderDetailEntry.getKey().equals("product_price")) {
                            orderDetail.setProductPrice(Double.valueOf(String.valueOf(orderDetailEntry.getValue())));
                        } else if (orderDetailEntry.getKey().equals("product_quantity")) {
                            orderDetail.setProductQuantity(Double.valueOf(String.valueOf(orderDetailEntry.getValue())));
                        } else if (orderDetailEntry.getKey().equals("product_discount")) {
                            orderDetail.setProductDiscount(Double.valueOf(String.valueOf(orderDetailEntry.getValue())));
                        }
                    }

                    // We insert order detail record to DB here so that the data wont be lost in the next for loop
                    orderMapper.insertOrderDetails(orderDetail);
                    createdOrderDetailsCount += 1;
                }
            }
        }

        return "created order count: " + createdOrderCount + ", created order details count: " + createdOrderDetailsCount;
    }

    // Update an existing order and its details.
    @PatchMapping("/update/{id}")
    public void updateOrder(@PathVariable("id") Integer id, @RequestBody Map<String, Object> requestData) {
        // Loop through each key, value pair to update the appropriate data for Order and OrderDetail models
        Double orderTotalAmt = 0.0;
        Double orderTotalDiscountAmt = 0.0;
        for (Map.Entry<String, Object> entry : requestData.entrySet()) {
            if (entry.getKey().equals("total_amount")) {
                orderTotalAmt = Double.valueOf(String.valueOf(entry.getValue()));
            } else if (entry.getKey().equals("total_discount_amount")) {
                orderTotalDiscountAmt = Double.valueOf(String.valueOf(entry.getValue()));
            } else if (entry.getKey().equals("detail")) {
                // Update Order to DB at this point, since there could be an error
                // when we are updating orderDetail model record later
                LocalDateTime localDateTime = LocalDateTime.now();
                orderMapper.updateOrder(id, orderTotalAmt, orderTotalDiscountAmt, localDateTime);

                // we have an ArrayList<Map<String, Object>> type because the request body
                // is a json structure that contains an array of key, value pairs
                ArrayList<Map<String, Object>> orderDetailList = (ArrayList<Map<String, Object>>) entry.getValue();
                for (int i = 0; i < orderDetailList.size(); i++) {
                    Integer orderDetailId = null;
                    String productName = "";
                    Double productPrice = 0.0;
                    Double productQuantity = 0.0;
                    Double productDiscount = 0.0;

                    for (Map.Entry<String, Object> orderDetailEntry : orderDetailList.get(i).entrySet()) {
                        if (orderDetailEntry.getKey().equals("product_name")) {
                            productName = orderDetailEntry.getValue().toString();
                        } else if (orderDetailEntry.getKey().equals("product_price")) {
                            productPrice = Double.valueOf(String.valueOf(orderDetailEntry.getValue()));
                        } else if (orderDetailEntry.getKey().equals("product_quantity")) {
                            productQuantity = Double.valueOf(String.valueOf(orderDetailEntry.getValue()));
                        } else if (orderDetailEntry.getKey().equals("product_discount")) {
                            productDiscount = Double.valueOf(String.valueOf(orderDetailEntry.getValue()));
                        } else if (orderDetailEntry.getKey().equals("id")) {
                            orderDetailId = Integer.valueOf(String.valueOf(orderDetailEntry.getValue()));
                        }
                    }

                    // We update order detail record to DB here so that the data wont be lost in the next for loop
                    LocalDateTime localDateTimeHere = LocalDateTime.now();
                    orderMapper.updateOrderDetails(orderDetailId, productName, productQuantity, productPrice, productDiscount, localDateTimeHere);
                }
            }
        }
    }

    // Deletes an order and its associated details
    @DeleteMapping("/delete/{id}")
    public void deleteOrder(@PathVariable("id") Integer id) {
        orderMapper.deleteOrderById(id);
    }

    // retrieves order within a specified data range (start_date and end_date),
    // returning only the order information and the count of related order_detail records
    @GetMapping("/query")
    public Map<String, Object> queryOrder(@RequestBody Map<String, Object> requestData) {
        LocalDate startDateTime = null;
        LocalDate endDateTime = null;

        for (Map.Entry<String, Object> entry : requestData.entrySet()) {
            if (entry.getKey().equals("start_date")) {
                startDateTime = LocalDate.parse(entry.getValue().toString());

            } else if (entry.getKey().equals("end_date")) {
                endDateTime = LocalDate.parse(entry.getValue().toString());

            }
        }

        List<Order> orders = orderMapper.query1stOrders(startDateTime, endDateTime);
        List<OrderDetail> orderDetails = orderMapper.query2ndOrders(startDateTime, endDateTime);

        // We create a new Hashmap so that we can store relevant data from order and order_detail tables
        // as well as return the correct json structure in Postman
        Map<String, Object> res = new HashMap<>();
        for(Order order : orders) {
            res.put("id", order.getId());
            res.put("total_amount", order.getTotalAmount());
            res.put("total_discount_amount", order.getTotalDiscountAmount());

            // We get count of order details that are associated to the current order
            Integer orderDetailCount = 0;
            for (OrderDetail orderDetail : orderDetails) {
                if (orderDetail.getOrderId() == order.getId()) {
                    orderDetailCount += 1;
                }
            }

            res.put("order_detail_count", orderDetailCount);
        }

        return res;
    }

    // fetch detailed information about a specific order, including all its details from the order_detail table.
    @GetMapping("/query/detail/{id}")
    public Map<String, Object> queryOrderDetail(@PathVariable("id") Integer id) {
        List<Order> orderList = orderMapper.query1stOrderDetail(id);
        List<OrderDetail> orderDetailList = orderMapper.query2ndOrderDetail(id);

        // We create a new Hashmap so that we can store relevant data from order and order_detail tables
        // as well as return the correct json structure in Postman
        Map<String, Object> map = new HashMap<>();
        for(Order order : orderList) {
            map.put("id", order.getId());
            map.put("total_amount", order.getTotalAmount());
            map.put("total_discount_amount", order.getTotalDiscountAmount());

            // We create an ArrayList so that we can group all the related order details to one order
            ArrayList orderDetailArr = new ArrayList();
            for(OrderDetail orderDetail : orderDetailList) {
                Map<String, Object> orderDetailData = new HashMap<>();

                if (orderDetail.getOrderId() == order.getId()) {
                    orderDetailData.put("id", orderDetail.getId());
                    orderDetailData.put("order_id", orderDetail.getOrderId());
                    orderDetailData.put("product_name", orderDetail.getProductName());
                    orderDetailData.put("product_quantity", orderDetail.getProductQuantity());
                    orderDetailData.put("product_price", orderDetail.getProductPrice());
                    orderDetailData.put("product_discount", orderDetail.getProductDiscount());

                    orderDetailArr.add(orderDetailData);
                }
            }

            // we append the order details to current Order object
            map.put("detail", orderDetailArr);
        }

        return map;
    }
}
