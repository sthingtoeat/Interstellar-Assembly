package com.qly.mallchat.common.user.controller;


import cn.hutool.system.UserInfo;
import com.qly.mallchat.common.common.domain.vo.resp.ApiResult;
import com.qly.mallchat.common.common.utils.RequestHolder;
import com.qly.mallchat.common.user.domain.vo.req.ModifyNameReq;
import com.qly.mallchat.common.user.domain.vo.req.WearingBadgeReq;
import com.qly.mallchat.common.user.domain.vo.resp.BadgeResp;
import com.qly.mallchat.common.user.domain.vo.resp.UserInfoResp;
import com.qly.mallchat.common.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author qly
 * @since 2024-06-19
 */
@RestController
@RequestMapping("/capi/user")
@Api(tags = "用户相关接口")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/userInfo")
    @ApiOperation("获取用户个人信息")
    public ApiResult<UserInfoResp> getUserInfo(){
        return ApiResult.success(userService.getUserInfo(RequestHolder.get().getUid()));
    }

    @PutMapping("/name")
    @ApiOperation("修改用户名")
    public ApiResult<Void> modifyName(@Valid @RequestBody ModifyNameReq req){
        userService.modifyName(RequestHolder.get().getUid(),req.getName());
        return ApiResult.success();
    }

    @GetMapping("/badges")
    @ApiOperation("可选徽章预览")
    public ApiResult<List<BadgeResp>> badges(){
        return ApiResult.success(userService.badges(RequestHolder.get().getUid()));
    }

    @PutMapping("/badge")
    @ApiOperation("佩戴徽章")
    public ApiResult<Void> wearingBadge(@Valid @RequestBody WearingBadgeReq req){
        userService.wearingBadge(RequestHolder.get().getUid(),req.getItemId());
        return ApiResult.success();
    }
}

