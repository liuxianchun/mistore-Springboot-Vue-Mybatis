package com.lxc.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;

/**
 * @author liuxianchun
 * @date 2021/2/4
 */
@Getter
@ToString
@AllArgsConstructor
public enum RespBeanEnum {

    WITHOUT_LOGIN(new HashMap<String,String>(){
        {
            put("code","005");
            put("msg","用户未登录");
        }
    });

    private final HashMap<String,String> map;

}
