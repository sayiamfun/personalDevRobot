package com.warm.system.service.db1;

import com.baomidou.mybatisplus.service.IService;
import com.warm.system.entity.PassageVisitorRecord;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dgd123
 * @since 2019-05-09
 */
public interface PassageVisitorRecordService extends IService<PassageVisitorRecord> {

    PassageVisitorRecord getByTaskIdAndChannelIdAndUnionId(Integer p_id, Integer channelId, String unionid);

    PassageVisitorRecord getByUnionId(String unionid);

    void updateUserWxIdById(String wxId, Integer id);
}
