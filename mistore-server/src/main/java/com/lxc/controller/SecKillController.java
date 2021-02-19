package com.lxc.controller;

import com.lxc.entity.SecGood;
import com.lxc.service.SecKillService;
import com.lxc.utils.CookieUtil;
import com.lxc.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuxianchun
 * @date 2021/1/30
 * 秒杀
 */
@RestController
@RequestMapping(value = "/seckill")
public class SecKillController {

    @Autowired
    private SecKillService secKillService;

    @RequestMapping(value = "/getSecGoods")
    public Map getSecGoods(){
        List<SecGood> secGoods = secKillService.getSecGoods();
        HashMap<String, Object> map = new HashMap<>();
        map.put("secGoods",secGoods);
        return map;
    }

    @RequestMapping(value = "/getDetails")
    public Map getDetails(@RequestBody Map<String,Integer> paramMap){
        Integer productID = paramMap.get("productID");
        SecGood secGood = secKillService.getSecGood(productID);
        HashMap<String, Object> map = new HashMap<>();
        if(null==secGood){
            map.put("code","003");
            map.put("msg","获取商品失败");
        }else {
            map.put("code", "001");
            map.put("msg", "获取商品成功");
            map.put("secgood", secGood);
        }
        return map;
    }

    @RequestMapping(value = "/addSecKillOrder")
    public Map addSecKillOrder(@RequestBody Map<String, Integer> paramMap, HttpServletRequest req){
        String ticket = CookieUtil.getCookieValue(req, "userTicket", false);
        if(StringUtils.isEmpty(ticket)||req.getSession().getAttribute(ticket)==null){
            return RespBeanEnum.WITHOUT_LOGIN.getMap();
        }
        Integer user_id = paramMap.get("user_id");
        Integer product_id = paramMap.get("product_id");
        Integer secgoods_id = paramMap.get("secgoods_id");
        int code = secKillService.addSecKillOrder(user_id, product_id, secgoods_id);
        HashMap<String, Object> map = new HashMap<>();
        switch (code){
            case -2:
                map.put("code","002");
                map.put("msg","参数错误");
                break;
            case -1:
                map.put("code","003");
                map.put("msg","此商品每人限购一件");
                break;
            case 1:
                map.put("code","001");
                map.put("msg","抢购成功");
                break;
            case 0:
                map.put("code","004");
                map.put("msg","此商品已售罄");
        }
        return map;
    }

}
