package com.qly.mallchat.common.user.dao;

import com.qly.mallchat.common.user.domain.entity.Role;
import com.qly.mallchat.common.user.domain.enums.RoleEnum;
import com.qly.mallchat.common.user.mapper.RoleMapper;
import com.qly.mallchat.common.user.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author qly
 * @since 2024-06-28
 */
@Service
public class RoleDao extends ServiceImpl<RoleMapper, Role>{

}
