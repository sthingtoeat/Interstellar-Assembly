package com.qly.mallchat.common.user.dao;

import com.qly.mallchat.common.common.domain.enums.YesOrNoEnum;
import com.qly.mallchat.common.user.domain.entity.User;
import com.qly.mallchat.common.user.mapper.UserMapper;
import com.qly.mallchat.common.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author qly
 * @since 2024-06-19
 */
@Service
public class UserDao extends ServiceImpl<UserMapper, User> {

    public User getByOpenId(String openId) {
        return lambdaQuery().eq(User::getOpenId,openId).one();
    }

    public User getByName(String name){
        return lambdaQuery().eq(User::getName,name).one();
    }

    public void modifyName(Long uid, String name) {
        lambdaUpdate()
                .eq(User::getId,uid)
                .set(User::getName,name)
                .update();
    }

    public void wearingBadge(Long uid, Long itemId) {
        lambdaUpdate()
                .eq(User::getId,uid)
                .eq(User::getItemId,itemId)
                .update();
    }

    public void invalidUid(Long id) {
        lambdaUpdate()
                .eq(User::getId,id)
                .set(User::getStatus, YesOrNoEnum.YES.getStatus())
                .update();
    }
}
