package com.qly.mallchat.common.user.dao;

import com.qly.mallchat.common.user.mapper.UserMapper;
import com.qly.mallchat.common.user.service.IUserService;
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
public class UserDao extends ServiceImpl<UserMapper, com.qly.mallchat.common.user.domain.entity.User> implements IUserService {

}
