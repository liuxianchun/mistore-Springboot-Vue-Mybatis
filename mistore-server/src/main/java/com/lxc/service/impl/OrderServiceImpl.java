package com.lxc.service.impl;

import com.lxc.dao.OrderDao;
import com.lxc.entity.Order;
import com.lxc.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author liuxianchun
 * @date 2021/1/21
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public void addOrder(Long order_id,int user_id, int product_id, int product_num, BigDecimal product_price) {
        orderDao.addOrder(order_id,user_id,product_id,product_num,product_price);
        Map<Object, Object> sales_num = redisTemplate.opsForHash().entries("sales_num");
        if(sales_num.isEmpty()||sales_num.get(product_id+"")==null){
            sales_num.put(product_id+"",product_num);
        }else{
            sales_num.put(product_id+"",(int)sales_num.get(product_id+"")+product_num);
        }
        redisTemplate.opsForHash().putAll("sales_num",sales_num);
    }

    @Override
    public void addOrderNoPay(Long order_id,int user_id, int product_id, int product_num, BigDecimal product_price) {
        orderDao.addOrderNoPay(order_id,user_id,product_id,product_num,product_price);
    }


    @Override
    public List<Order> getOrders(Long order_id) {
        return orderDao.getOrders(order_id);
    }

    @Override
    public List<Long> getOrderIdByUser(int user_id) {
        return orderDao.getOrderIdByUser(user_id);
    }


    @Override
    public Long getOrderId() {
        return orderDao.getOrderId();
    }

    @Override
    public int orderPay(Integer user_id, Long order_id) {
        if(null==user_id||null==order_id)
            return 0;
        List<Order> orders = orderDao.getOrders(order_id);
        Map<Object, Object> sales_num = redisTemplate.opsForHash().entries("sales_num");
        for(Order order:orders){
            int product_id = order.getProduct_id();
            int product_num = order.getProduct_num();
            if(sales_num.isEmpty()||sales_num.get(product_id+"")==null){
                sales_num.put(product_id+"",product_num);
            }else{
                sales_num.put(product_id+"",(int)sales_num.get(product_id+"")+product_num);
            }
            redisTemplate.opsForHash().putAll("sales_num",sales_num);
        }
        return orderDao.orderPay(user_id,order_id);
    }
}
