package com.lxc.schedule;

import com.lxc.dao.ScheduleDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author liuxianchun
 * @date 2021/2/3
 * 对失效的秒杀商品和订单进行处理
 */
@Component
@Slf4j
public class OutDateSchedule {

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /*处理失效订单*/
    @Scheduled(cron = "0 0/10 * * * ?")   //每10分钟执行一次
    public void handleOrder(){
        int count = scheduleDao.handleOrder();
        log.info("处理过期订单共"+count+"笔");
    }

    /*处理失效秒杀商品*/
    @Scheduled(cron = "0 0 0/1 * * ?")   //每小时执行一次
    public void handleSecGood(){
        int count = scheduleDao.handleSecGood();
        redisTemplate.delete("secgood");
        redisTemplate.delete("secgoods");
        log.info("处理失效的秒杀商品共"+count+"件");
    }

}
