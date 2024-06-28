package com.qly.mallchat.common.user.service;

import com.qly.mallchat.common.user.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qly.mallchat.common.user.domain.vo.req.BlackReq;
import com.qly.mallchat.common.user.domain.vo.resp.BadgeResp;
import com.qly.mallchat.common.user.domain.vo.resp.UserInfoResp;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author qly
 * @since 2024-06-19
 */
public interface UserService {

    Long register(User insert);

    UserInfoResp getUserInfo(Long uid);

    void modifyName(Long uid, String name);

    List<BadgeResp> badges(Long uid);

    void wearingBadge(Long uid, Long itemId);

    void black(BlackReq req);
}
