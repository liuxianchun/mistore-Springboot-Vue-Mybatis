package com.lxc.schedule;

import com.lxc.dao.ProductDao;
import com.lxc.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author liuxianchun
 * @date 2021/1/24
 * redis相关的定时任务
 */
//@Component
@Slf4j
public class RedisSchedule {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    @Scheduled(cron = "0 0 0 * * ?")   //每天执行一次
    public void syncSales(){
        Map<Object, Object> sales = redisTemplate.opsForHash().entries("sales_num");
        redisTemplate.opsForHash().putAll("sales_num",null);
        for(Object product_id:sales.keySet()){
            productDao.updateSales(Integer.parseInt(product_id+""), (int) sales.get(product_id));
        }
        log.info("更新商品销量完成");
    }

    @Scheduled(cron = "0 0 0 * * ?")   //每天执行一次
    public void syncLoginCount(){
        Map<Object, Object> login_count = redisTemplate.opsForHash().entries("login_count");
        redisTemplate.opsForHash().putAll("login_count",null);
        for(Object username:login_count.keySet()){
            userDao.updateLoginCount((String)username, (int) login_count.get(username));
        }
        log.info("更新用户登录次数完成");
    }

}
