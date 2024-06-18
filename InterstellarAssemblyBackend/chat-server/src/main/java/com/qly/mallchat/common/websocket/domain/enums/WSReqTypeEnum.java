package com.qly.mallchat.common.websocket.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
@AllArgsConstructor
@Getter
public enum WSReqTypeEnum { //enum是一个枚举类型，一般表示一组常量，但是也可以写方法
    LOGIN(1,"请求登录二维码"),
    HEARTBEAT(2,"心跳包"),
    AUTHORIZE(3,"登录认证"),
    ;

    private final Integer type;
    private final String desc;

    private static Map<Integer, WSReqTypeEnum> cache;

    static {
        cache = Arrays.stream(WSReqTypeEnum.values()).collect(Collectors.toMap(WSReqTypeEnum::getType, Function.identity()));
    }

    public static WSReqTypeEnum of(Integer type) {
        return cache.get(type);
    }
}
