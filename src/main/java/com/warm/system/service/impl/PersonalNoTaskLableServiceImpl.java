package com.warm.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoTask;
import com.warm.system.entity.PersonalNoTaskLable;
import com.warm.system.mapper.PersonalNoTaskLableMapper;
import com.warm.system.service.db1.PersonalNoTaskLableService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.utils.VerifyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
@Service
public class PersonalNoTaskLableServiceImpl extends ServiceImpl<PersonalNoTaskLableMapper, PersonalNoTaskLable> implements PersonalNoTaskLableService {
    private static Log log = LogFactory.getLog(PersonalNoTaskLableServiceImpl.class);
    @Autowired
    private PersonalNoTaskLableMapper taskLableMapper;

    @Override
    public List<PersonalNoTaskLable> listBysql(Sql sql) {
        return taskLableMapper.listBySql(sql);
    }
}
