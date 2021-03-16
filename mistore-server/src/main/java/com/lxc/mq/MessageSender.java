package com.lxc.mq;

import com.lxc.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxianchun
 * @date 2021/3/7
 */
@Slf4j
@Component
public class MessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //消息发送成功
    private RabbitTemplate.ConfirmCallback confirmCallback = (correlationData, ack, cause) -> {
        if(ack){  //客户端确认
            log.info("消息发送成功，消息id："+ (correlationData != null ? correlationData.getId() : null));
        }else{   //客户端未确认
            log.info("消息发送失败，消息id："+ (correlationData != null ? correlationData.getId() : null));
        }
    };
    //消息发送失败的回调
    private RabbitTemplate.ReturnsCallback returnsCallback = new RabbitTemplate.ReturnsCallback() {
        @Override
        public void returnedMessage(ReturnedMessage returned) {
            log.info("发送失败{replyCode:"+returned.getReplyCode()+",exchange:"+returned.getExchange()+
                    ",routingKey:"+returned.getRoutingKey()+",replyText:"+returned.getReplyText()+"}");
        }
    };

    public void sendSeckill(int user_id, int product_id, int secgoods_id){
        Map<String,Object> props = new HashMap<>();
        props.put("user_id",user_id);
        props.put("product_id",product_id);
        props.put("secgoods_id",secgoods_id);
        MessageHeaders headers = new MessageHeaders(props);
        Message message = MessageBuilder.createMessage("", headers);
        CorrelationData correlationData = new CorrelationData();
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnsCallback(returnsCallback);
        rabbitTemplate.convertAndSend(RabbitMQConfig.SECORDER_EXCHANGE,"seckill",message,correlationData);
    }

    //数据库减库存操作失败，进行补库存
    public void sendAddSecStock(int secgoods_id){
        Message<Integer> message = MessageBuilder.createMessage(secgoods_id, new MessageHeaders(null));
        CorrelationData correlationData = new CorrelationData();
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnsCallback(returnsCallback);
        rabbitTemplate.convertAndSend(RabbitMQConfig.SECORDER_EXCHANGE,"addSecStock",message,correlationData);
    }

}
