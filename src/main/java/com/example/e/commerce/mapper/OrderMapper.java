package com.example.e.commerce.mapper;

import com.example.e.commerce.model.Order;
import com.example.e.commerce.model.OrderDetail;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {

    @Results(id = "1stOrdersQueryMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "totalAmount", column = "total_amount"),
            @Result(property = "totalDiscountAmount", column = "total_discount_amount"),
    })
    @Select({"<script>",
            "SELECT id, total_amount, total_discount_amount",
            "FROM order_table",
            "WHERE order_table.created_at >= #{startDate} ",
            "OR order_table.created_at &lt;= #{endDate}",
            "</script>"})
    public List<Order> query1stOrders(LocalDate startDate, LocalDate endDate);

    @Results(id = "2ndOrdersQueryMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "orderId", column = "order_id"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")
    })
    @Select({"<script>",
            "SELECT id, order_id",
            "FROM order_detail_table odt",
            "WHERE odt.created_at BETWEEN #{startDate} ",
            "AND #{endDate}",
            "</script>"})
    public List<OrderDetail> query2ndOrders(LocalDate startDate, LocalDate endDate);

    @Results(id = "1stOrderDetailMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "totalAmount", column = "total_amount"),
            @Result(property = "totalDiscountAmount", column = "total_discount_amount")
    })
    @Select({"<script>",
            "SELECT id, total_amount, total_discount_amount",
            "FROM order_table",
            "WHERE id = #{id}",
            "</script>"})
    public List<Order> query1stOrderDetail(Integer id);

    @Results(id = "2ndOrderDetailMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "orderId", column = "order_id"),
            @Result(property = "productName", column = "product_name"),
            @Result(property = "productQuantity", column = "product_quantity"),
            @Result(property = "productPrice", column = "product_price"),
            @Result(property = "productDiscount", column = "product_discount")
    })
    @Select({"<script>",
            "SELECT id, order_id,  product_name, product_quantity, product_price, product_discount",
            "FROM order_detail_table",
            "WHERE order_id = #{id} ",
            "GROUP BY order_detail_table.id, order_detail_table.order_id",
            "</script>"})
    public List<OrderDetail> query2ndOrderDetail(Integer id);

    @Delete("DELETE FROM order_table WHERE id = #{id}")
    public void deleteOrderById(Integer id);

    @Insert("INSERT INTO order_table (total_amount, total_discount_amount) " +
            " VALUES (#{totalAmount}, #{totalDiscountAmount})")
    public void insertOrder(Order order);

    @Insert("INSERT INTO order_detail_table(order_id, product_name, product_quantity, product_price, product_discount) " +
            "VALUES (#{orderId}, #{productName}, #{productQuantity}, #{productPrice}, #{productDiscount})")
    public void insertOrderDetails(OrderDetail orderDetail);

    @Update("Update order_table set total_amount=#{totalAmount}, " +
            " total_discount_amount=#{totalDiscountAmount}, updated_at=#{updatedAt} where id=#{id}")
    public void updateOrder(Integer id, Double totalAmount, Double totalDiscountAmount, LocalDateTime updatedAt);

    @Update("Update order_detail_table set product_name=#{productName}, " +
            " product_quantity=#{productQuantity}, product_price=#{productPrice}, " +
            " product_discount=#{productDiscount}, updated_at=#{updatedAt} where id=#{id}")
    public void updateOrderDetails(Integer id, String productName, Double productQuantity, Double productPrice, Double productDiscount, LocalDateTime updatedAt);
}
