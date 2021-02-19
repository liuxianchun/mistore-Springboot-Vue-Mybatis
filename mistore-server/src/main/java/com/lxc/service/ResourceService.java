package com.lxc.service;

import com.lxc.entity.Carousel;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author liuxianchun
 * @date 2021/1/18
 */
@Component
public interface ResourceService {

    List<Carousel> getCarousel();

}
