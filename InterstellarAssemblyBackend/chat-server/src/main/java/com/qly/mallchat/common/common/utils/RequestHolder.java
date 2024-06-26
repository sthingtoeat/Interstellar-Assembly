package com.qly.mallchat.common.common.utils;

import com.qly.mallchat.common.common.domain.dto.RequestInfo;

/**
 * Description: 请求上下文
 * Author: <a href="https://github.com/zongzibinbin">abin</a>
 * Date: 2023-09-09
 */
public class RequestHolder {
    private static final ThreadLocal<RequestInfo> threadLocal = new ThreadLocal<RequestInfo>();

    public static void set(RequestInfo requestInfo) {
        threadLocal.set(requestInfo);
    }

    public static RequestInfo get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
