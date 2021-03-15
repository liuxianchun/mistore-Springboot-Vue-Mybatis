package com.lxc.service;

import com.lxc.entity.SecGood;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author liuxianchun
 * @date 2021/1/30
 */
@Component
public interface SecKillService {

    List<SecGood> getSecGoods();

    SecGood getSecGood(Integer secgood_id);

    int addSecKillOrder(Integer user_id,Integer product_id,Integer secgoods_id);

    /*是否在秒杀期间*/
    boolean isInSecKill(int secgoods_id);

    Map getSecResult(int user_id, int secgoods_id);

}
