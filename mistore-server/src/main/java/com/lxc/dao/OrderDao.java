package com.lxc.dao;

import com.lxc.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liuxianchun
 * @date 2021/1/21
 */
@Repository
public interface OrderDao {

    /*从购物车结算*/
    @Insert("insert into orders(order_id,user_id,product_id,product_num,product_price,order_time,pay_time) values(#{order_id},#{user_id},#{product_id},#{product_num},#{product_price},now(),now())")
    void addOrder(Long order_id,int user_id, int product_id, int product_num, BigDecimal product_price);

    @Select("select * from orders where order_id=#{order_id}")
    List<Order> getOrders(Long order_id);

    @Select("select distinct order_id from orders where user_id=#{user_id} order by order_id desc")
    List<Long> getOrderIdByUser(int user_id);

    @Select("select get_trans_num('order')")
    Long getOrderId();

    /*秒杀生成订单*/
    @Insert("insert into orders(order_id,user_id,product_id,product_num,product_price,order_time,status) values(#{order_id},#{user_id},#{product_id},#{product_num},#{product_price},now(),1)")
    void addOrderNoPay(Long order_id,int user_id, int product_id, int product_num, BigDecimal product_price);

    /*付款*/
    @Update("update orders set pay_time=now() where user_id=#{user_id} and order_id=#{order_id}")
    int orderPay(Integer user_id, Long order_id);
}
