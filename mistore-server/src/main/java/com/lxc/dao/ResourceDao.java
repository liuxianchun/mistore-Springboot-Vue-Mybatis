package com.lxc.dao;

import com.lxc.entity.Carousel;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuxianchun
 * @date 2021/1/18
 */
@Repository
public interface ResourceDao {

    @Select("select * from carousel")
    List<Carousel> getCarousel();

}
