package com.lxc.dao;

import com.lxc.entity.Collect;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuxianchun
 * @date 2021/1/22
 */
@Repository
public interface CollectDao {

    @Insert("insert into collect(user_id,product_id,collect_time) values(#{user_id},#{product_id},now())")
    void addCollect(int user_id,int product_id);

    @Select("select * from collect where user_id=#{user_id}")
    List<Collect> getCollect(int user_id);

    @Select("select count(1) from collect where user_id=#{user_id} and product_id=#{product_id}")
    int findCollect(int user_id,int product_id);

    @Delete("delete from collect where user_id=#{user_id} and product_id=#{product_id}")
    void deleteCollect(int user_id, int product_id);

}
