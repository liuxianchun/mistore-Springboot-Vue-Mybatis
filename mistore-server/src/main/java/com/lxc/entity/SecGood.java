package com.lxc.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author liuxianchun
 * @date 2021/1/30
 * 秒杀商品
 */
@Data
public class SecGood {

    private Long id;
    private int product_id;
    private int goods_stock;  //秒杀商品库存
    private BigDecimal seckill_price;  //秒杀价
    private Date start_date;
    private Date end_date;
    private Product detail;  //商品详情

}
