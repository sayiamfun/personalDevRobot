package com.warm.system.service.impl;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoPeople;
import com.warm.system.mapper.PersonalNoPeopleMapper;
import com.warm.system.service.db1.PersonalNoPeopleService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.utils.VerifyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
@Service
public class PersonalNoPeopleServiceImpl extends ServiceImpl<PersonalNoPeopleMapper, PersonalNoPeople> implements PersonalNoPeopleService {

    private static Log log = LogFactory.getLog(PersonalNoPeopleServiceImpl.class);

    @Autowired
    private PersonalNoPeopleMapper taskPeopleMapper;


    @Override
    public void deleteBySql(Sql sql) {
        taskPeopleMapper.deleteBySql(sql);
    }

    @Override
    public PersonalNoPeople getBySql(Sql sql) {
        return taskPeopleMapper.getBySql(sql);
    }

    @Override
    public Integer add(PersonalNoPeople personalNoPeople) {
        if(VerifyUtils.isEmpty(personalNoPeople.getId())){
            return taskPeopleMapper.add(personalNoPeople);
        }
        return taskPeopleMapper.updateOne(personalNoPeople);
    }

    @Override
    public List<PersonalNoPeople> listBySql(Sql sql) {
        return taskPeopleMapper.listBySql(sql);
    }
}
