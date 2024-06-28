package com.qly.mallchat.common.user.service;

import com.qly.mallchat.common.user.domain.enums.IdempotentEnum;

/**
 * <p>
 * 用户背包表 服务类
 * </p>
 *
 * @author qly
 * @since 2024-06-25
 */
public interface IUserBackpackService {
    /***
     * 给用户发放一个物品
     * @param uid               用户id
     * @param itemId            物品id
     * @param idempotentEnum    幂等类型
     * @param businessId        幂等唯一标识
     */
    void acquireItem(Long uid , Long itemId, IdempotentEnum idempotentEnum,String businessId);
}
