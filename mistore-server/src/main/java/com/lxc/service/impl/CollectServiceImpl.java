package com.lxc.service.impl;

import com.lxc.dao.CollectDao;
import com.lxc.entity.Collect;
import com.lxc.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liuxianchun
 * @date 2021/1/22
 */
@Service("collectService")
public class CollectServiceImpl implements CollectService {

    @Autowired
    private CollectDao collectDao;

    @Override
    public void addCollect(int user_id, int product_id) {
        collectDao.addCollect(user_id,product_id);
    }

    @Override
    public List<Collect> getCollect(int user_id) {
        return collectDao.getCollect(user_id);
    }

    @Override
    public int findCollect(int user_id, int product_id) {
        return collectDao.findCollect(user_id,product_id);
    }

    @Override
    public void deleteCollect(int user_id, int product_id) {
        collectDao.deleteCollect(user_id,product_id);
    }
}
