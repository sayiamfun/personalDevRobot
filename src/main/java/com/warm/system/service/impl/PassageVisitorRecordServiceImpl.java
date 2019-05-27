package com.warm.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.system.entity.PassageVisitorRecord;
import com.warm.system.mapper.PassageVisitorRecordMapper;
import com.warm.system.service.db1.PassageVisitorRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dgd123
 * @since 2019-05-09
 */
@Service
public class PassageVisitorRecordServiceImpl extends ServiceImpl<PassageVisitorRecordMapper, PassageVisitorRecord> implements PassageVisitorRecordService {

    @Autowired
    private PassageVisitorRecordMapper passageVisitorRecordMapper;
    @Override
    public PassageVisitorRecord getByTaskIdAndChannelIdAndUnionId(Integer p_id, Integer channelId, String unionid) {
        return passageVisitorRecordMapper.getByTaskIdAndChannelIdAndUnionId(p_id, channelId, unionid);
    }

    @Override
    public PassageVisitorRecord getByUnionId(String unionid) {
        return passageVisitorRecordMapper.getByUnionId(unionid);
    }

    @Override
    public void updateUserWxIdById(String wxId, Integer id) {
        passageVisitorRecordMapper.updateUserWxIdById(wxId, id);
    }
}
