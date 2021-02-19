package com.lxc.service.impl;

import com.lxc.dao.OrderDao;
import com.lxc.dao.SecKillDao;
import com.lxc.dao.UserDao;
import com.lxc.entity.SecGood;
import com.lxc.service.SecKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author liuxianchun
 * @date 2021/1/30
 */
@Service("secKillService")
public class SecKillServiceImpl implements SecKillService {

    @Autowired
    private SecKillDao secKillDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private UserDao userDao;
    
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public List<SecGood> getSecGoods() {
        Map<Object, Object> secgoods = redisTemplate.opsForHash().entries("secgoods");
        if(secgoods.isEmpty()){
            List<SecGood> secGoods = secKillDao.getSecGoods();
            secgoods.put("secgoods",secGoods);
            redisTemplate.opsForHash().putAll("secgoods",secgoods);
        }
        return (List<SecGood>) secgoods.get("secgoods");
    }

    @Override
    public SecGood getSecGood(Integer productID) {
        if(null==productID)
            return null;
        Map<Object, Object> secgood = redisTemplate.opsForHash().entries("secgood");
        if(secgood.isEmpty()||secgood.get(productID)==null){
            SecGood secGood = secKillDao.getSecGood(productID);
            secgood.put(productID+"",secGood);
            redisTemplate.opsForHash().putAll("secgood",secgood);
        }
        return (SecGood) secgood.get(productID+"");
    }

    @Override
    public int addSecKillOrder(Integer user_id, Integer product_id,Integer secgoods_id) {
        if(null==user_id||null==product_id||null==secgoods_id)
            return -2;  //参数错误
        int secKillOrder = secKillDao.findSecKillOrder(user_id, product_id,secgoods_id);
        if(secKillOrder>0){
            return -1;  //重复抢购
        }else{
           return  decreaseSecGoodsStock(secgoods_id,user_id,product_id);
        }
    }

    private int decreaseSecGoodsStock(int secgoods_id,int user_id,int product_id){
        BigDecimal secGoodPrice = secKillDao.findSecGoodPrice(secgoods_id);   //符合条件的价格
        if(null!=secGoodPrice&&userDao.findUserById(user_id)>0){
            int decreStock =  secKillDao.decreaseSecGoodsStock(secgoods_id);  //尝试减库存
            if(decreStock>0){
                Long order_id = orderDao.getOrderId();
                //生成订单
                orderDao.addOrderNoPay(order_id,user_id,product_id,1,secGoodPrice);
                //生成秒杀订单
                secKillDao.addSecKillOrder(secgoods_id,order_id,user_id,product_id);
                return 1;   //抢购成功
            }
        }
        return 0;   //抢购失败
    }
}
