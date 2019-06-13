package com.warm.system.service.impl;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoUser;
import com.warm.system.mapper.PersonalNoUserMapper;
import com.warm.system.service.db1.PersonalNoUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.utils.VerifyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public class PersonalNoUserServiceImpl extends ServiceImpl<PersonalNoUserMapper, PersonalNoUser> implements PersonalNoUserService {
    private static Log log = LogFactory.getLog(PersonalNoUserServiceImpl.class);
    @Autowired
    private PersonalNoUserMapper userMapper;


    @Override
    public Integer add(PersonalNoUser user) {
        if(VerifyUtils.isEmpty(user.getId())){
            return userMapper.add(user);
        }
        return userMapper.updateOne(user);
    }

    @Override
    public PersonalNoUser getBySql(Sql sql) {
        return userMapper.getBySql(sql);
    }

    @Override
    public Integer updateBySql(Sql sql) {
        return userMapper.updateBySql(sql);
    }

}
