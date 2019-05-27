package com.warm.system.service.impl;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoPhoneTask;
import com.warm.system.entity.PersonalNoPhoneTaskFinish;
import com.warm.system.mapper.PersonalNoPhoneTaskMapper;
import com.warm.system.service.db1.PersonalNoPhoneTaskService;
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
 * @author dgd123
 * @since 2019-03-29
 */
@Service
public class PersonalNoPhoneTaskServiceImpl extends ServiceImpl<PersonalNoPhoneTaskMapper, PersonalNoPhoneTask> implements PersonalNoPhoneTaskService {
    @Autowired
    private PersonalNoPhoneTaskMapper taskMapper;

    @Override
    public List<PersonalNoPhoneTaskFinish> listFinshBySql(Sql sql) {
        return taskMapper.listFinshBySql(sql);
    }

    @Override
    public PersonalNoPhoneTask getBySql(Sql sql) {
        return taskMapper.getBySql(sql);
    }

    @Override
    public Integer add(PersonalNoPhoneTask byId) {
        if(VerifyUtils.isEmpty(byId.getId())){
            return taskMapper.add(byId);
        }
        return taskMapper.updateOne(byId);
    }

    @Override
    public Integer deleteBySql(Sql sql) {
        return taskMapper.deleteBySql(sql);
    }


}
