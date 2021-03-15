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

    WITHOUT_LOGIN(new HashMap<String,Object>(){
        {
            put("code","005");
            put("msg","用户未登录");
        }
    }),
    PARAM_ERROR(new HashMap<String,Object>(){
        {
            put("code","002");
            put("msg","参数错误");
        }
    }),
    TIME_ERROR(new HashMap<String,Object>(){
        {
            put("code","004");
            put("msg","时间错误");
        }
    }),
    ORDER_SUCCESS(new HashMap<String,Object>(){
        {
            put("code","001");
            put("msg","购买成功");
        }
    }),
    ORDER_WAITING(new HashMap<String,Object>(){
        {
            put("code","001");
            put("msg","排队中，请稍等");
        }
    }),
    SECORDER_NOTFOUND(new HashMap<String,Object>(){
        {
            put("code","004");
            put("msg","秒杀订单未找到");
        }
    }),
    BUYNUM_LIMIT(new HashMap<String,Object>(){
        {
            put("code","003");
            put("msg","此商品购买数量达到上限");
        }
    }),
    STOCK_ZERO(new HashMap<String,Object>(){
        {
            put("code","004");
            put("msg","此商品已售罄");
        }
    }),
    PAY_SUCCESS(new HashMap<String,Object>(){
        {
            put("code","001");
            put("msg","付款成功");
        }
    }),
    PAY_ERROR(new HashMap<String,Object>(){
        {
            put("code","004");
            put("msg","付款失败");
        }
    }),
    GET_GOOD_SUCCESS(new HashMap<String,Object>(){
        {
            put("code","001");
            put("msg","获取商品成功");
        }
    }),
    GET_GOOD_ERROR(new HashMap<String,Object>(){
        {
            put("code","004");
            put("msg","获取商品失败");
        }
    }),
    FIND_ORDER_ERROR(new HashMap<String,Object>(){
        {
            put("code","004");
            put("msg","未找到订单");
        }
    });

    private final HashMap<String,Object> map;

}
