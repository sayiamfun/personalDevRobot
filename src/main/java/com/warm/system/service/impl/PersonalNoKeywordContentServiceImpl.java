package com.warm.system.service.impl;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoKeywordContent;
import com.warm.system.mapper.PersonalNoKeywordContentMapper;
import com.warm.system.service.db1.PersonalNoKeywordContentService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class PersonalNoKeywordContentServiceImpl extends ServiceImpl<PersonalNoKeywordContentMapper, PersonalNoKeywordContent> implements PersonalNoKeywordContentService {
    @Autowired
    private PersonalNoKeywordContentMapper keywordContentMapper;

    @Override
    public List<PersonalNoKeywordContent> listBySql(Sql sql) {
        return keywordContentMapper.listBySql(sql);
    }
}
