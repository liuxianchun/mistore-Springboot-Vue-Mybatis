package com.lxc.controller;

import com.lxc.entity.Carousel;
import com.lxc.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuxianchun
 * @date 2021/1/18
 */
@RestController
@RequestMapping(value = "/resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @RequestMapping(value = "/carousel")
    public Map getCarousel(){
        List<Carousel> carousel = resourceService.getCarousel();
        HashMap<String, Object> map = new HashMap<>();
        map.put("code","001");
        map.put("carousel",carousel);
        return map;
    }

}
