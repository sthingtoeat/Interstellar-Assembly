package com.qly.mallchat.common.websocket.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.qly.mallchat.common.user.dao.UserDao;
import com.qly.mallchat.common.user.domain.entity.User;
import com.qly.mallchat.common.user.service.LoginService;
import com.qly.mallchat.common.websocket.domain.dto.WSChannelExtraDTO;
import com.qly.mallchat.common.websocket.domain.vo.resp.WSBaseResp;
import com.qly.mallchat.common.websocket.service.WebSocketService;
import com.qly.mallchat.common.websocket.service.adapter.WebSocketAdapter;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.SneakyThrows;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    private UserDao userDao;
    @Autowired
    @Lazy
    private WxMpService wxMpService;
    @Autowired
    private LoginService loginService;
    /**
     * 存放所有用户的链接，包括登录的用户和未登录的游客
     */
    private static final ConcurrentHashMap<Channel , WSChannelExtraDTO> ONLINE_WS_MAP = new ConcurrentHashMap<>();

    public static final Duration DURATION = Duration.ofHours(1);
    public static final int MAXIMUM_SIZE = 1000;
    /**
     * 扫码登录时也会开通一个通道用于登录，如果一直没能处理，会导致内存溢出，咖啡因可以并发也可以用来限制登录时长（登录会过期）
     * 这里是临时保存随机码code和通道channel的映射关系
     */
    private static final Cache<Integer,Channel> WAIT_LOGIN_MAP = Caffeine.newBuilder()
            .maximumSize(MAXIMUM_SIZE)
            .expireAfterWrite(DURATION)
            .build();
    @Override
    public void connect(Channel channel) {
        ONLINE_WS_MAP.put(channel,new WSChannelExtraDTO());
    }

    @SneakyThrows
    @Override
    public void handleLoginReq(Channel channel) {
        //生成随机码
        Integer code = generateLoginCode(channel);
        //把随机码发给微信平台转成含参的二维码            ,这里可能会报错，但是忽略就行
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(code, (int) DURATION.getSeconds());
        //把转成的二维码发送给前端
        sendMsg(channel , WebSocketAdapter.buildResp(wxMpQrCodeTicket));

    }

    @Override
    public void remove(Channel channel) {
        ONLINE_WS_MAP.remove(channel);
        //todo 用户下线以后的收尾工作，例如通知
    }

    @Override
    public void scanLoginSuccess(Integer code, Long uid) {
        //确认链接在机器上
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        if(Objects.isNull(channel)){
            return;
        }
        User user = userDao.getById(uid);
        //移除code
        WAIT_LOGIN_MAP.invalidate(code);
        //调用登录模块获取token
        String token =loginService.login(uid);
        //登录
        loginSuccess(channel,user,token);
    }

    @Override
    public void waitAuthorize(Integer code) {
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        if(Objects.isNull(channel)){
            return;
        }
        sendMsg(channel,WebSocketAdapter.buildWaitAuthorizeResp());
    }

    @Override
    public void authorize(Channel channel, String token) {
        Long validUid = loginService.getValidUid(token);
        if(Objects.nonNull(validUid)){
            User user = userDao.getById(validUid);
            loginSuccess(channel,user,token);

        }else{
            sendMsg(channel,WebSocketAdapter.buildInvalidTokenResp());
        }
    }

    private void loginSuccess(Channel channel, User user, String token) {
        //保存channel的对应uid
        WSChannelExtraDTO wsChannelExtraDTO = ONLINE_WS_MAP.get(channel);
        wsChannelExtraDTO.setUid(user.getId());
        //todo 用户上线成功的一些事件
        //推送登录成功的消息
        sendMsg(channel,WebSocketAdapter.buildResp(user,token));
    }

    private void sendMsg(Channel channel, WSBaseResp<?> resp) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(resp)));
    }

    //生成随机码的同时
    private Integer generateLoginCode(Channel channel) {
        Integer code;
        do{
            code = RandomUtil.randomInt(Integer.MIN_VALUE);
        }while(Objects.nonNull(WAIT_LOGIN_MAP.asMap().putIfAbsent(code , channel)));

        return code;
    }
}
