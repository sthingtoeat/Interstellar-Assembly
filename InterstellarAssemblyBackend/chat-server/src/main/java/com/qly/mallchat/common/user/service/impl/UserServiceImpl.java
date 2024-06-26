package com.qly.mallchat.common.user.service.impl;

import com.qly.mallchat.common.common.utils.AssertUtil;
import com.qly.mallchat.common.user.dao.ItemConfigDao;
import com.qly.mallchat.common.user.dao.UserBackpackDao;
import com.qly.mallchat.common.user.dao.UserDao;
import com.qly.mallchat.common.user.domain.entity.ItemConfig;
import com.qly.mallchat.common.user.domain.entity.User;
import com.qly.mallchat.common.user.domain.entity.UserBackpack;
import com.qly.mallchat.common.user.domain.enums.ItemEnum;
import com.qly.mallchat.common.user.domain.enums.ItemTypeEnum;
import com.qly.mallchat.common.user.domain.vo.resp.BadgeResp;
import com.qly.mallchat.common.user.domain.vo.resp.UserInfoResp;
import com.qly.mallchat.common.user.service.UserService;
import com.qly.mallchat.common.user.service.adapter.UserAdapter;
import com.qly.mallchat.common.user.service.cache.ItemCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    @Override
    @Transactional
    public Long register(User insert) {
        boolean save = userDao.save(insert);
        //todo 用户的注册事件
        userDao.save(insert);
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
}
