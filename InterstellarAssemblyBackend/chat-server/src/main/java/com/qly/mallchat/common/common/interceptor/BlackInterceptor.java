package com.qly.mallchat.common.common.interceptor;

import cn.hutool.core.collection.CollectionUtil;
import com.qly.mallchat.common.common.domain.dto.RequestInfo;
import com.qly.mallchat.common.common.exception.HttpErrorEnum;
import com.qly.mallchat.common.common.utils.RequestHolder;
import com.qly.mallchat.common.user.domain.entity.Black;
import com.qly.mallchat.common.user.domain.enums.BlackTypeEnum;
import com.qly.mallchat.common.user.service.cache.UserCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
@Component
public class BlackInterceptor implements HandlerInterceptor {
    @Autowired
    private UserCache userCache;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<Integer, Set<String>> blackMap = userCache.getBlackMap();
        RequestInfo requestInfo = RequestHolder.get();
        if(inBlankList(requestInfo.getUid(),blackMap.get(BlackTypeEnum.UID.getType()))){
            HttpErrorEnum.ACCESS_DENIED.sendHttpError(response);
            return false;
        }
        if(inBlankList(requestInfo.getIp(),blackMap.get(BlackTypeEnum.IP.getType()))){
            HttpErrorEnum.ACCESS_DENIED.sendHttpError(response);
            return false;
        }
        return true;
    }

    private boolean inBlankList(Object target, Set<String> set) {
        if(Objects.isNull(target) || CollectionUtil.isEmpty(set)){
            return false;
        }
        return set.contains(target.toString());
    }
}
