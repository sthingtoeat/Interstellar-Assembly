package com.qly.mallchat.common.user.dao;

import com.qly.mallchat.common.user.domain.entity.ItemConfig;
import com.qly.mallchat.common.user.mapper.ItemConfigMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 功能物品配置表 服务实现类
 * </p>
 *
 * @author qly
 * @since 2024-06-25
 */
@Service
public class ItemConfigDao extends ServiceImpl<ItemConfigMapper, ItemConfig> {

    public List<ItemConfig> getByType(Integer itemType) {
        return lambdaQuery()
                .eq(ItemConfig::getType,itemType)
                .list();
    }
}
