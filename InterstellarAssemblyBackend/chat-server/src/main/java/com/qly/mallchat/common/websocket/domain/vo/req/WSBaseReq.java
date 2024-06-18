package com.qly.mallchat.common.websocket.domain.vo.req;


import lombok.Data;

// 这里定义的是来自前端的请求，应该是什么结构，resp包则是后端处理返回给前端的
@Data
public class WSBaseReq {

    /***                                                    点击下面这个可以直接去看类型
     * @see com.qly.mallchat.common.websocket.domain.enums.WSReqTypeEnum
     */
    private Integer type;
    private String data;

}
