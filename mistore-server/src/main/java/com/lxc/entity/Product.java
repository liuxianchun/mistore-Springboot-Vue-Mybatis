package com.lxc.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author liuxianchun
 * @date 2021/1/17
 * 产品
 */
@Data
public class Product implements Serializable {

    private int product_id;
    private String product_name;
    private int category_id;
    private String product_title;
    private String product_intro;
    private String product_picture;
    private BigDecimal product_price;   //原价
    private BigDecimal product_selling_price;    //售价
    private int product_num;
    private int product_sales;

}
