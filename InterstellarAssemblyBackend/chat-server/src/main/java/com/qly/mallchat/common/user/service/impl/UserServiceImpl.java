package com.qly.mallchat.common.user.service.impl;

import com.qly.mallchat.common.user.dao.UserDao;
import com.qly.mallchat.common.user.domain.entity.User;
import com.qly.mallchat.common.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Override
    @Transactional
    public Long register(User insert) {
        boolean save = userDao.save(insert);
        //todo 用户的注册事件
        userDao.save(insert);
        return insert.getId();
    }
}
