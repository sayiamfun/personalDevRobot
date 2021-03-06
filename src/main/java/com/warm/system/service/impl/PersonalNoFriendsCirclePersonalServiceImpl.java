package com.warm.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoFriendsCirclePersonal;
import com.warm.system.mapper.PersonalNoFriendsCirclePersonalMapper;
import com.warm.system.service.db1.PersonalNoFriendsCirclePersonalService;
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
public class PersonalNoFriendsCirclePersonalServiceImpl extends ServiceImpl<PersonalNoFriendsCirclePersonalMapper, PersonalNoFriendsCirclePersonal> implements PersonalNoFriendsCirclePersonalService {
    private static Log log = LogFactory.getLog(PersonalNoFriendsCirclePersonalServiceImpl.class);
    @Autowired
    private PersonalNoFriendsCirclePersonalMapper noFriendsCirclePersonalMapper;

    @Override
    public void updateBySql(Sql sql) {
        noFriendsCirclePersonalMapper.updateBySql(sql);
    }
}
