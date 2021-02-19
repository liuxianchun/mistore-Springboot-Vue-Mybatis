package com.lxc.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liuxianchun
 * @date 2021/1/17
 * 目录
 */
@Data
public class Category implements Serializable {

    private int category_id;
    private String category_name;

}
