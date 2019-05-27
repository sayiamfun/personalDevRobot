package com.warm.system.service.impl;

import com.warm.system.entity.PersonalNoMessage;
import com.warm.system.entity.PersonalNoMessageContent;
import com.warm.system.mapper.PersonalNoMessageMapper;
import com.warm.system.service.db1.PersonalNoMessageContentService;
import com.warm.system.service.db1.PersonalNoMessageService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.utils.VerifyUtils;
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
public class PersonalNoMessageServiceImpl extends ServiceImpl<PersonalNoMessageMapper, PersonalNoMessage> implements PersonalNoMessageService {

    @Autowired
    private PersonalNoMessageMapper messageMapper;
    @Autowired
    private PersonalNoMessageContentService messageContentService;
    @Override
    public PersonalNoMessage getById(Integer messageId) {
        PersonalNoMessage message = messageMapper.getById(messageId);
        if(!VerifyUtils.isEmpty(message)){
            List<PersonalNoMessageContent> messageContentList = messageContentService.listByMessageId(messageId);
            message.setMessageContentList(messageContentList);
        }
        return message;
    }
}
