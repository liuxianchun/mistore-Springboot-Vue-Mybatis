package com.lxc.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liuxianchun
 * @date 2021/1/17
 * 轮播图数据
 */
@Data
public class Carousel implements Serializable {

    private int carousel_id;
    private int product_id;
    private String imgPath;
    private String describes;

}
