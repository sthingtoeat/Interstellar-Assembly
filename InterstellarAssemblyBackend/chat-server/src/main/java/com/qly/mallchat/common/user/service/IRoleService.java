package com.qly.mallchat.common.user.service;

import com.qly.mallchat.common.user.domain.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qly.mallchat.common.user.domain.enums.RoleEnum;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author qly
 * @since 2024-06-28
 */
public interface IRoleService {
    /***
     * 检查是否拥有权限，测试写法，临时用
     *
     *     */
    boolean hasPower(Long uid , RoleEnum roleEnum);
}
