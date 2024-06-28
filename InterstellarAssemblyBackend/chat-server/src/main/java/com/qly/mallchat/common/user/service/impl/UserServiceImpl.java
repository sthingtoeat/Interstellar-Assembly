package com.qly.mallchat.common.user.service.impl;

import com.qly.mallchat.common.common.annotation.RedissonLock;
import com.qly.mallchat.common.common.event.UserBlackEvent;
import com.qly.mallchat.common.common.event.UserRegisterEvent;
import com.qly.mallchat.common.common.utils.AssertUtil;
import com.qly.mallchat.common.user.dao.BlackDao;
import com.qly.mallchat.common.user.dao.ItemConfigDao;
import com.qly.mallchat.common.user.dao.UserBackpackDao;
import com.qly.mallchat.common.user.dao.UserDao;
import com.qly.mallchat.common.user.domain.entity.*;
import com.qly.mallchat.common.user.domain.enums.BlackTypeEnum;
import com.qly.mallchat.common.user.domain.enums.ItemEnum;
import com.qly.mallchat.common.user.domain.enums.ItemTypeEnum;
import com.qly.mallchat.common.user.domain.vo.req.BlackReq;
import com.qly.mallchat.common.user.domain.vo.resp.BadgeResp;
import com.qly.mallchat.common.user.domain.vo.resp.UserInfoResp;
import com.qly.mallchat.common.user.service.UserService;
import com.qly.mallchat.common.user.service.adapter.UserAdapter;
import com.qly.mallchat.common.user.service.cache.ItemCache;
import io.micrometer.core.instrument.util.StringUtils;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserBackpackDao userBackpackDao;
    @Autowired
    private ItemCache itemCache;
    @Autowired
    private ItemConfigDao itemConfigDao;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private BlackDao blackDao;
    @Override
    @Transactional
    public Long register(User insert) {
        boolean save = userDao.save(insert);
        //注册完以后发放改名卡
        //用户的注册事件
        applicationEventPublisher.publishEvent(new UserRegisterEvent(this, insert));
        return insert.getId();
    }

    @Override
    public UserInfoResp getUserInfo(Long uid) {
        User user = userDao.getById(uid);
        Integer modifyNameCount = userBackpackDao.getCountByValidItemId(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        return UserAdapter.buildUserInfo(user,modifyNameCount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedissonLock(key = "#uid")
    public void modifyName(Long uid, String name) {
        User oldUser = userDao.getByName(name);
        AssertUtil.isEmpty(oldUser,"名称重复，请重新输入！");
        UserBackpack modifyNameItem = userBackpackDao.getFirstValidItem(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        AssertUtil.isNotEmpty(modifyNameItem,"没有改名卡了");
        //使用改名卡
        boolean success = userBackpackDao.useItem(modifyNameItem);
        if(success){
            //使用成功则可开始改名
            userDao.modifyName(uid , name);
        }
    }

    @Override
    public List<BadgeResp> badges(Long uid) {
        //查询所有徽章
        List<ItemConfig> itemConfigs = itemCache.getByType(ItemTypeEnum.BADGE.getType());
        //查询用户拥有的徽章
        List<UserBackpack> backpacks = userBackpackDao.getByItemIds(uid, itemConfigs.stream().map(ItemConfig::getId).collect(Collectors.toList()));
        //查询用户当前佩戴的徽章
        User user = userDao.getById(uid);
        return UserAdapter.buildBadgeResp(itemConfigs,backpacks,user);
    }

    @Override
    public void wearingBadge(Long uid, Long itemId) {
        //确保有徽章
        UserBackpack firstValidItem = userBackpackDao.getFirstValidItem(uid,itemId);
        AssertUtil.isNotEmpty(firstValidItem , "这个徽章你还没诶，快去获取吧");
        //确保这个物品时徽章
        ItemConfig itemConfig = itemConfigDao.getById(firstValidItem.getItemId());
        AssertUtil.equal(itemConfig.getType(),ItemTypeEnum.BADGE.getType(),"只有徽章才能佩戴");
        userDao.wearingBadge(uid,itemId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void black(BlackReq req) {
        Long uid = req.getUid();
        Black user = new Black();
        user.setType(BlackTypeEnum.UID.getType());
        blackDao.save(user);
        User byId = userDao.getById(uid);
        blackIp(Optional.ofNullable(byId.getIpInfo()).map(IpInfo::getCreateIp).orElse(null));
        blackIp(Optional.ofNullable(byId.getIpInfo()).map(IpInfo::getUpdateIp).orElse(null));
        applicationEventPublisher.publishEvent(new UserBlackEvent(this,byId));
    }

    private void blackIp(String ip) {
        if(StringUtils.isBlank(ip)){
            return;
        }
        try {
            Black insert = new Black();
            insert.setType(BlackTypeEnum.IP.getType());
            insert.setTarget(ip);
            blackDao.save(insert);
        }catch (Exception e){

        }

    }
}
