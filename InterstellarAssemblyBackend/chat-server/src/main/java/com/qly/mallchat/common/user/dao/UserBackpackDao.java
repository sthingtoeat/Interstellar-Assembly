package com.qly.mallchat.common.user.dao;

import com.qly.mallchat.common.common.domain.enums.YesOrNoEnum;
import com.qly.mallchat.common.user.domain.entity.UserBackpack;
import com.qly.mallchat.common.user.mapper.UserBackpackMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户背包表 服务实现类
 * </p>
 *
 * @author qly
 * @since 2024-06-25
 */
@Service
public class UserBackpackDao extends ServiceImpl<UserBackpackMapper, UserBackpack> {

    public Integer getCountByValidItemId(Long uid, Long itemId) {
        return lambdaQuery()
                .eq(UserBackpack::getUid,uid)
                .eq(UserBackpack::getItemId,itemId)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .count();

    }

    public UserBackpack getFirstValidItem(Long uid, Long itemId) {
        return lambdaQuery()
                .eq(UserBackpack::getUid,uid)
                .eq(UserBackpack::getItemId,itemId)
                .eq(UserBackpack::getStatus,YesOrNoEnum.NO.getStatus())
                .orderByAsc(UserBackpack::getId)
                .last("limit 1")
                .one();
    }

    public boolean useItem(UserBackpack item) {
        return lambdaUpdate()
                .eq(UserBackpack::getId,item.getId())
                .eq(UserBackpack::getStatus,YesOrNoEnum.NO.getStatus())
                .set(UserBackpack::getStatus,YesOrNoEnum.YES.getStatus())
                .update();

    }

    public List<UserBackpack> getByItemIds(Long uid, List<Long> itemId) {
        return lambdaQuery()
                .eq(UserBackpack::getUid , uid)
                .eq(UserBackpack::getStatus,YesOrNoEnum.NO.getStatus())
                .eq(UserBackpack::getItemId,itemId)
                .list();
    }
}
