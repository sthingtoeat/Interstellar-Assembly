package com.qly.mallchat.common.websocket.domain.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Description: 群成员列表的成员信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMemberResp {
    @ApiModelProperty("uid")
    private Long uid;
    /**
     * @see com.abin.mallchat.common.user.domain.enums.ChatActiveStatusEnum
     */
    @ApiModelProperty("在线状态 1在线 2离线")
    private Integer activeStatus;
    @ApiModelProperty("最后一次上下线时间")
    private Date lastOptTime;
}
