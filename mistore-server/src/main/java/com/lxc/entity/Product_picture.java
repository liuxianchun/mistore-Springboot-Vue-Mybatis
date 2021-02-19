package com.lxc.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liuxianchun
 * @date 2021/1/17
 * 产品图片
 */
@Data
public class Product_picture implements Serializable {

    private int id;
    private int product_id;
    private String product_picture;
    private String introduction;

}
