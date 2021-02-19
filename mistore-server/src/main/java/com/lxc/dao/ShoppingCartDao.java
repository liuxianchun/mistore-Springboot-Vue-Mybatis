package com.lxc.dao;

import com.lxc.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuxianchun
 * @date 2021/1/19
 */
@Repository
public interface ShoppingCartDao {

    @Update("update shoppingcart set num=#{num} where user_id=#{user_id} and product_id=#{product_id}")
    void updateShoppingCart(int user_id,int product_id,int num);

    @Insert("insert into shoppingcart(user_id,product_id,num) values(#{user_id},#{product_id},1)")
    void addShoppingCart(int user_id,int product_id);

    @Select("select * from shoppingcart where user_id=#{user_id} and product_id=#{product_id}")
    ShoppingCart findShoppingCart(int user_id,int product_id);

    @Select("select * from shoppingcart where user_id=#{user_id}")
    List<ShoppingCart> getShoppingCart(int user_id);

    @Delete("delete from shoppingcart where user_id=#{user_id} and product_id=#{product_id}")
    void deleteShoppingCart(int user_id,int product_id);

}
