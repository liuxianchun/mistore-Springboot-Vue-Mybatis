package com.lxc.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @author liuxianchun
 * @date 2021/3/7
 */
@Configuration
public class RabbitMQConfig {

    /*死信交换机*/
    public static final String DEAD_EXCHANGE = "dead_exchange";
    /*死信队列*/
    public static final String DEAD_QUEUE = "dead_queue";

    /*秒杀派单队列*/
    public static final String SECORDER_DIR_QUEUE = "secorder_dir_queue";
    /*秒杀补库存队列*/
    public static final String SEC_ADDSTOCK_QUEUE = "secorder_create_queue";
    /*秒杀订单交换机*/
    public static final String SECORDER_EXCHANGE = "secorder_exchange";
    /*秒杀补库存交换机*/
    public static final String SEC_ADDSTOCK_EXCHANGE = "secorder_create_exchange";

    //创建初始化RabbitAdmin对象
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        // 只有设置为 true，spring 才会加载 RabbitAdmin 这个类
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    //死信队列
    @Bean
    public Queue DeadQueue(){
        return new Queue(DEAD_QUEUE,true,false,false,null);
    }

    @Bean
    public Exchange DeadExchange(){
        return new DirectExchange(DEAD_EXCHANGE,true,false,null);
    }

    @Bean
    public Binding bindingDead(){
        return BindingBuilder
                .bind(DeadQueue())
                .to(DeadExchange())
                .with("")
                .and(null);
    }


    //派单队列
    @Bean
    public Queue DirSecOrderQueue(){
        HashMap<String, Object> props = new HashMap<>();
        props.put("x-message-ttl",30000);  //30s
        props.put("x-max-length",10000);   //队列最大长度
        return new Queue(SECORDER_DIR_QUEUE,true,false,false,props);
    }

    @Bean
    public Exchange SecOrderExchange(){
        return new DirectExchange(SECORDER_EXCHANGE,true,false,null);
    }

    @Bean
    public Binding bindingDirSecKill(){
        return BindingBuilder
                .bind(DirSecOrderQueue())
                .to(SecOrderExchange())
                .with("seckill")
                .and(null);
    }

    //补库存队列
    @Bean
    public Queue SecAddStockQueue(){
        HashMap<String, Object> props = new HashMap<>();
        props.put("x-dead-exchange",DEAD_EXCHANGE);  //死信交换机
        return new Queue(SEC_ADDSTOCK_QUEUE,true,false,false,props);
    }

    @Bean
    public Exchange SecAddStockExchange(){
        return new DirectExchange(SEC_ADDSTOCK_EXCHANGE,true,false,null);
    }

    @Bean
    public Binding bindingSecAddStock(){
        return BindingBuilder
                .bind(SecAddStockQueue())
                .to(SecAddStockExchange())
                .with("addSecStock")
                .and(null);
    }

}
