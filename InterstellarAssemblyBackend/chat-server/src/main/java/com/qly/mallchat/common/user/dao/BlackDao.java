package com.qly.mallchat.common.user.dao;

import com.qly.mallchat.common.user.domain.entity.Black;
import com.qly.mallchat.common.user.mapper.BlackMapper;
import com.qly.mallchat.common.user.service.IBlackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 黑名单 服务实现类
 * </p>
 *
 * @author qly
 * @since 2024-06-28
 */
@Service
public class BlackDao extends ServiceImpl<BlackMapper, Black> implements IBlackService {

}
