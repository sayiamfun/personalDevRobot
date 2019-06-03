package com.warm.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoTaskPersonal;
import com.warm.system.mapper.PersonalNoTaskPersonalMapper;
import com.warm.system.service.db1.PersonalNoTaskPersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
@Service
public class PersonalNoTaskPersonalServiceImpl extends ServiceImpl<PersonalNoTaskPersonalMapper, PersonalNoTaskPersonal> implements PersonalNoTaskPersonalService {

    @Autowired
    private PersonalNoTaskPersonalMapper taskPersonalMapper;


    @Override
    public void updateBySql(Sql sql) {
        taskPersonalMapper.updateBySql(sql);
    }
}
