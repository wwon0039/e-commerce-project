package com.example.e.commerce.model;

import java.time.LocalDateTime;

public class Order {
    private Integer id;

    private double totalAmount;

    private double totalDiscountAmount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Order() {

    }

    public Order(double totalAmount, double totalDiscountAmount) {
        this.totalAmount = totalAmount;
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(double totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
