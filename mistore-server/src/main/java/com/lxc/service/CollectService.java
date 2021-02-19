package com.lxc.service;

import com.lxc.entity.Collect;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author liuxianchun
 * @date 2021/1/22
 */
@Component
public interface CollectService {

    void addCollect(int user_id,int product_id);

    List<Collect> getCollect(int user_id);

    int findCollect(int user_id,int product_id);

    void deleteCollect(int user_id, int product_id);
}
