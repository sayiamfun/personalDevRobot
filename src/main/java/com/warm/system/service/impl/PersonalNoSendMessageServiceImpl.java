package com.warm.system.service.impl;

import com.warm.system.entity.PersonalNoSendMessage;
import com.warm.system.mapper.PersonalNoSendMessageMapper;
import com.warm.system.service.db1.PersonalNoSendMessageService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liwenjie123
 * @since 2019-05-06
 */
@Service
public class PersonalNoSendMessageServiceImpl extends ServiceImpl<PersonalNoSendMessageMapper, PersonalNoSendMessage> implements PersonalNoSendMessageService {

    @Autowired
    private PersonalNoSendMessageMapper sendMessageMapper;

    @Override
    public PersonalNoSendMessage getMessageIdByWxIdAndTiming(String s, int i) {
        return sendMessageMapper.getMessageIdByWxIdAndTiming(s,0);
    }

    @Override
    public List<PersonalNoSendMessage> listByPersonalWxId(String s) {
        return sendMessageMapper.listByPersonalWxId(s);
    }

    @Override
    public PersonalNoSendMessage getById(Integer personalNoSendMessageId) {
        return sendMessageMapper.getById(personalNoSendMessageId);
    }
}
