package com.lxc.dao;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuxianchun
 * @date 2021/2/3
 */
@Repository
public interface ScheduleDao {

    /*秒杀商品结束6小时失效*/
    @Select("select id from secgoods where status=1 and ROUND((unix_timestamp(NOW())-unix_timestamp(end_date))/3600,0)>6")
    List<Integer> selectSecGood();

    @Update("update secgoods set status=0 where id=#{id}")
    void handleSecGood(int id);

    /*未付款订单一小时失效*/
    @Update("update orders set status=0 where status=1 and pay_time is null and ROUND((unix_timestamp(NOW())-unix_timestamp(order_time))/60,0)>60")
    int handleOrder();
}
