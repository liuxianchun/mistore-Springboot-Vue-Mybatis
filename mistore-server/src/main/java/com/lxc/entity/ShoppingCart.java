package com.lxc.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liuxianchun
 * @date 2021/1/17
 * 购物车
 */
@Data
public class ShoppingCart implements Serializable {

    private int id;
    private int user_id;
    private int product_id;
    private int num;

}
