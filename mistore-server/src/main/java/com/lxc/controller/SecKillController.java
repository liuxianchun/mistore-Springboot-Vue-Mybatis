package com.lxc.controller;

import com.lxc.entity.SecGood;
import com.lxc.mq.MessageSender;
import com.lxc.schedule.RedisSchedule;
import com.lxc.service.SecKillService;
import com.lxc.utils.CookieUtil;
import com.lxc.utils.RedisUtil;
import com.lxc.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private RedisSchedule redisSchedule;

    @Autowired
    private RedisUtil redisUtil;

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
            return RespBeanEnum.GET_GOOD_ERROR.getMap();
        }else {
            map = RespBeanEnum.GET_GOOD_SUCCESS.getMap();
            map.put("secgood", secGood);
            return map;
        }
    }

    @RequestMapping(value = "/addSecKillOrder")
    public Map addSecKillOrder(@RequestBody Map<String, Integer> paramMap, HttpServletRequest req){
        Map loginMap = CookieUtil.validateLogin(req);
        if(loginMap!=null)
            return loginMap;
        Integer user_id = paramMap.get("user_id");
        Integer product_id = paramMap.get("product_id");
        Integer secgoods_id = paramMap.get("secgoods_id");
        if(user_id != null && product_id != null && secgoods_id !=null){
            boolean isInSecKill = secKillService.isInSecKill(secgoods_id);
            if(!isInSecKill)
                return RespBeanEnum.TIME_ERROR.getMap();
            //高并发下有效，分布式下无效
            synchronized (this){
                //先查询redis是否已抢购，未抢购则加入秒杀队列
                String secOrder = redisUtil.get("secOrder:"+secgoods_id+":"+user_id);
                Integer secStock = (Integer) redisUtil.hget("secStock", secgoods_id + "");
                if(secStock==null){
                    redisSchedule.syncSecStock();
                    secStock = (Integer) redisUtil.hget("secStock",secgoods_id+"");
                }
                //有库存且未秒杀,加入秒杀队列，redis添加抢购记录为秒杀就绪中
                if(secStock!=null&& secStock >0&&secOrder==null){
                    redisUtil.set("secOrder:"+secgoods_id+":"+user_id,"ready");
                    redisUtil.hset("secStock",secgoods_id+"", secStock -1);
                    messageSender.sendSeckill(user_id,product_id,secgoods_id);
                    return RespBeanEnum.ORDER_WAITING.getMap();
                }else{
                    if(secStock!=null&&secStock==0)
                        return RespBeanEnum.STOCK_ZERO.getMap();
                    return RespBeanEnum.BUYNUM_LIMIT.getMap();
                }
            }
        }else{
            return RespBeanEnum.PARAM_ERROR.getMap();
        }
    }

    /*查询秒杀结果*/
    @RequestMapping(value = "/getSecKillResult")
    public Map getSecKillResult(@RequestBody Map<String, Integer> paramMap, HttpServletRequest req){
        Map loginMap = CookieUtil.validateLogin(req);
        if(loginMap!=null)
            return loginMap;
        Integer user_id = paramMap.get("user_id");
        Integer product_id = paramMap.get("product_id");
        if(user_id==null||product_id==null){
            return RespBeanEnum.PARAM_ERROR.getMap();
        }else{
            return secKillService.getSecResult(user_id,product_id);
        }
    }

}
