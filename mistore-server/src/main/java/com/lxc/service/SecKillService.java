package com.lxc.service;

import com.lxc.entity.SecGood;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author liuxianchun
 * @date 2021/1/30
 */
@Component
public interface SecKillService {

    List<SecGood> getSecGoods();

    SecGood getSecGood(Integer productID);

    int addSecKillOrder(Integer user_id,Integer product_id,Integer secgoods_id);

}
