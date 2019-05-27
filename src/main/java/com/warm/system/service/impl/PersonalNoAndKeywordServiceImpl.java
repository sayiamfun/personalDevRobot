package com.warm.system.service.impl;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoAndKeyword;
import com.warm.system.mapper.PersonalNoAndKeywordMapper;
import com.warm.system.service.db1.PersonalNoAndKeywordService;
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
 * @since 2019-04-17
 */
@Service
public class PersonalNoAndKeywordServiceImpl extends ServiceImpl<PersonalNoAndKeywordMapper, PersonalNoAndKeyword> implements PersonalNoAndKeywordService {

    @Autowired
    private PersonalNoAndKeywordMapper personalNoAndKeywordMapper;

    @Override
    public PersonalNoAndKeyword getBySql(Sql sql) {
        return personalNoAndKeywordMapper.getBySql(sql);
    }
}
