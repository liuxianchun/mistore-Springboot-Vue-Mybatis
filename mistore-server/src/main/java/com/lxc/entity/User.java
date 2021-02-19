package com.lxc.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liuxianchun
 * @date 2021/1/17
 * 用户
 */
@Data
public class User implements Serializable {

    private int user_id;
    private String userName;
    private String password;
    private String userPhoneNumber;
    private String email;
    private String gender;

}
