package com.warm.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.warm.entity.Sql;
import com.warm.entity.robot.common.SunTaskType;
import com.warm.system.entity.*;
import com.warm.system.mapper.PersonalNoFriendsMapper;
import com.warm.system.service.db1.*;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.utils.VerifyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class PersonalNoFriendsServiceImpl extends ServiceImpl<PersonalNoFriendsMapper, PersonalNoFriends> implements PersonalNoFriendsService {
    private static Log log = LogFactory.getLog(PersonalNoFriendsServiceImpl.class);
    @Autowired
    private PersonalNoFriendsMapper friendsMapper;

    @Override
    public Integer add(PersonalNoFriends friends) {
        if(VerifyUtils.isEmpty(friends.getId())) {
            return friendsMapper.add(friends);
        }
        return friendsMapper.updateOne(friends);
    }

    @Override
    public PersonalNoFriends getBySql(Sql sql) {
        return friendsMapper.getBySql(sql);
    }

    @Override
    public void deleteBySql(Sql sql) {
        friendsMapper.deleteBySql(sql);
    }

    @Override
    public List<PersonalNoFriends> listBySql(Sql sql) {
        return friendsMapper.listBySql(sql);
    }

    @Override
    public Long countBySql(Sql sql) {
        return friendsMapper.countBySql(sql);
    }
}
