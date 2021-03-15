package com.lxc.mq;

import com.lxc.config.RabbitMQConfig;
import com.lxc.service.SecKillService;
import com.lxc.utils.RedisUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author liuxianchun
 * @date 2021/3/7
 */
@Slf4j
@Component
public class MessageReceiver {

    @Autowired
    private SecKillService secKillService;

    @Autowired
    private RedisUtil redisUtil;

    /*秒杀队列*/
    @RabbitListener(queues = {RabbitMQConfig.SECORDER_DIR_QUEUE})
    @RabbitHandler
    public void addSecKill(Message msg, Channel channel) {
        try {
            //消息逐条消费
            channel.basicQos(0,1,true);
            log.info("接收到秒杀消息:"+msg.getHeaders());
            long delivery_tag = (long) msg.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
            int user_id = (int) msg.getHeaders().get("user_id");
            int product_id = (int) msg.getHeaders().get("product_id");
            int secgoods_id = (int) msg.getHeaders().get("secgoods_id");
            int status = secKillService.addSecKillOrder(user_id, product_id, secgoods_id);
            //秒杀成功，redis订单状态变为success
            if(status==1){
                redisUtil.set("secOrder:"+secgoods_id+":"+user_id,"success");
                log.info("秒杀成功");
            }
            channel.basicAck(delivery_tag,false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*死信队列*/
    @RabbitListener(queues = {RabbitMQConfig.DEAD_QUEUE})
    @RabbitHandler
    public void consumeDead(Message msg, Channel channel) {
        long delivery_tag = (long) msg.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        try {
            channel.basicAck(delivery_tag,true);
            log.info("成功处理秒杀死信");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
