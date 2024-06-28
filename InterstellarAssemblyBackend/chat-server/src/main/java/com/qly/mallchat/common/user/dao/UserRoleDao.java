package com.qly.mallchat.common.user.dao;

import com.qly.mallchat.common.user.domain.entity.UserRole;
import com.qly.mallchat.common.user.mapper.UserRoleMapper;
import com.qly.mallchat.common.user.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户角色关系表 服务实现类
 * </p>
 *
 * @author qly
 * @since 2024-06-28
 */
@Service
public class UserRoleDao extends ServiceImpl<UserRoleMapper, UserRole> {
    public List<UserRole> listByUid(Long uid) {
        return lambdaQuery()
                .eq(UserRole::getUid, uid)
                .list();
    }
}
