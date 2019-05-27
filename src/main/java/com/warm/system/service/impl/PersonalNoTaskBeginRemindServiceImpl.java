package com.warm.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoTask;
import com.warm.system.entity.PersonalNoTaskBeginRemind;
import com.warm.system.mapper.PersonalNoTaskBeginRemindMapper;
import com.warm.system.service.db1.PersonalNoTaskBeginRemindService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.utils.VerifyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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
public class PersonalNoTaskBeginRemindServiceImpl extends ServiceImpl<PersonalNoTaskBeginRemindMapper, PersonalNoTaskBeginRemind> implements PersonalNoTaskBeginRemindService {
    private static Log log = LogFactory.getLog(PersonalNoTaskBeginRemindServiceImpl.class);
    @Autowired
    private PersonalNoTaskBeginRemindMapper personalNoTaskBeginRemindMapper;

    @Override
    public List<PersonalNoTaskBeginRemind> listBySql(Sql sql) {
        return personalNoTaskBeginRemindMapper.listBySql(sql);
    }
}
