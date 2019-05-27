package com.warm.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoTask;
import com.warm.system.entity.PersonalNoTaskReplyContent;
import com.warm.system.mapper.PersonalNoTaskReplyContentMapper;
import com.warm.system.service.db1.PersonalNoTaskReplyContentService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.utils.VerifyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
@Service
public class PersonalNoTaskReplyContentServiceImpl extends ServiceImpl<PersonalNoTaskReplyContentMapper, PersonalNoTaskReplyContent> implements PersonalNoTaskReplyContentService {
    private static Log log = LogFactory.getLog(PersonalNoTaskReplyContentServiceImpl.class);

    @Autowired
    private PersonalNoTaskReplyContentMapper replyContentMapper;

    @Override
    public List<PersonalNoTaskReplyContent> listBySql(Sql sql) {
        return replyContentMapper.listBySql(sql);
    }
}
