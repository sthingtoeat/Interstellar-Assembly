package com.qly.mallchat.common.user.service.impl;

import com.qly.mallchat.common.common.constant.RedisKey;
import com.qly.mallchat.common.common.utils.JwtUtils;
import com.qly.mallchat.common.common.utils.RedisUtils;
import com.qly.mallchat.common.user.service.LoginService;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {
    public static final int TOKEN_EXPIRE_DAYS = 3;
    public static final int TOKEN_RENEWAL_DAYS = 1;
    @Autowired
    private JwtUtils jwtUtils;
    @Override
    public boolean verify(String token) {
        return false;
    }

    @Override
    @Async
    public void renewalTokenIfNecessary(String token) {
        Long uid = getValidUid(token);
        String userTokenKey = getUserTokenKey(uid);
        Long expireDays = RedisUtils.getExpire(userTokenKey,TimeUnit.DAYS);
        if(expireDays == -2){//key不存在
            return;
        }
        if(expireDays < TOKEN_RENEWAL_DAYS){//有效期小于设定的时长则刷新时长
            RedisUtils.expire(getUserTokenKey(uid),TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
        }
    }

    @Override
    public String login(Long uid) {
        String token = jwtUtils.createToken(uid);
        RedisUtils.set(getUserTokenKey(uid),token, TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
        return token;
    }

    @Override
    public Long getValidUid(String token) {
        String s = "";
        Long uid = jwtUtils.getUidOrNull(token);
        if(Objects.isNull(uid)){
            return null;
        }
        String oldToken = RedisUtils.get(getUserTokenKey(uid));

        return Objects.equals(oldToken,token)?uid:null;
    }

    private String getUserTokenKey(Long uid){
        return RedisKey.getKey(RedisKey.USER_TOKEN_STRING);
    }
}
