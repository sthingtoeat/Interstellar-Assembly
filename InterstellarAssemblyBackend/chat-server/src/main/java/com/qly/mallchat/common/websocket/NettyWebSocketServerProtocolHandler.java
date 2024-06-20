package com.qly.mallchat.common.websocket;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.qly.mallchat.common.websocket.domain.enums.WSReqTypeEnum;
import com.qly.mallchat.common.websocket.domain.vo.req.WSBaseReq;
import com.qly.mallchat.common.websocket.domain.vo.resp.WSBaseResp;
import com.qly.mallchat.common.websocket.service.WebSocketService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;

public class NettyWebSocketServerProtocolHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Autowired
    private WebSocketService websSocketService;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        websSocketService = SpringUtil.getBean(WebSocketService.class);
        websSocketService.connect(ctx.channel());
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx){
        userOffline(ctx.channel());
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
            System.out.println("握手完成");
        }else if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;
            if(event.state()== IdleState.READER_IDLE){
                System.out.println("读空闲");
                //todo 用户下线

            }
        }
    }

    /**
     *用户下线的统一操作，包括了心跳下线和主动下线
     */
    private void userOffline(Channel channel){
        websSocketService.remove(channel);
        channel.close();
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        WSBaseReq wsBaseReq = JSONUtil.toBean(text , WSBaseReq.class);
        switch (WSReqTypeEnum.of(wsBaseReq.getType())){
            case AUTHORIZE:
                break;
            case HEARTBEAT:
                break;
            case LOGIN:
                websSocketService.handleLoginReq(ctx.channel());
        }
    }
}
