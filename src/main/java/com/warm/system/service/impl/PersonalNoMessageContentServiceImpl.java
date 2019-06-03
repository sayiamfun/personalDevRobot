package com.warm.system.service.impl;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoMessageContent;
import com.warm.system.mapper.PersonalNoMessageContentMapper;
import com.warm.system.service.db1.PersonalNoMessageContentService;
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
public class PersonalNoMessageContentServiceImpl extends ServiceImpl<PersonalNoMessageContentMapper, PersonalNoMessageContent> implements PersonalNoMessageContentService {

    @Autowired
    private PersonalNoMessageContentMapper messageContentMapper;


    @Override
    public List<PersonalNoMessageContent> listBySql(Sql sql) {
        return messageContentMapper.listBySql(sql);
    }
}
