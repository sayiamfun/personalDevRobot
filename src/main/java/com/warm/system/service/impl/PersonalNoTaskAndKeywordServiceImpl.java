package com.warm.system.service.impl;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoTaskAndKeyword;
import com.warm.system.mapper.PersonalNoTaskAndKeywordMapper;
import com.warm.system.service.db1.PersonalNoTaskAndKeywordService;
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
 * @since 2019-04-28
 */
@Service
public class PersonalNoTaskAndKeywordServiceImpl extends ServiceImpl<PersonalNoTaskAndKeywordMapper, PersonalNoTaskAndKeyword> implements PersonalNoTaskAndKeywordService {

    @Autowired
    private PersonalNoTaskAndKeywordMapper taskAndKeywordMapper;

    @Override
    public PersonalNoTaskAndKeyword getBySql(Sql sql) {
        return taskAndKeywordMapper.getBySql(sql);
    }
}
