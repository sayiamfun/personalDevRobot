package com.warm.system.service.impl;

import com.warm.entity.DB;
import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoMessage;
import com.warm.system.entity.PersonalNoMessageContent;
import com.warm.system.mapper.PersonalNoMessageMapper;
import com.warm.system.service.db1.PersonalNoMessageContentService;
import com.warm.system.service.db1.PersonalNoMessageService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.utils.DaoGetSql;
import com.warm.utils.VerifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

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

    private String DBMessage = DB.DBAndTable(DB.PERSONAL_ZC_01,DB.personal_no_message);
    private String DBMessageContent = DB.DBAndTable(DB.PERSONAL_ZC_01,DB.personal_no_message_content);

    @Override
    public PersonalNoMessage getById(Integer messageId) {
        String getSql = DaoGetSql.getSql(" SELECT * FROM "+DBMessage+" WHERE `id` = ? AND deleted = 0",messageId);
        Sql sql = new Sql(getSql);
        PersonalNoMessage message = messageMapper.getBySql(sql);
        if(!VerifyUtils.isEmpty(message)){
            getSql = DaoGetSql.getSql(" SELECT * FROM "+DBMessageContent+" WHERE `message_id` = ? AND deleted = 0",messageId);
            sql.setSql(getSql);
            List<PersonalNoMessageContent> messageContentList = messageContentService.listBySql(sql);
            message.setMessageContentList(messageContentList);
        }
        return message;
    }
}
