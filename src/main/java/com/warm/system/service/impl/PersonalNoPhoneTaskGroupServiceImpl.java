package com.warm.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.warm.common.DBTypeEnum;
import com.warm.common.DataSourceSwitch;
import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoPhoneTaskGroup;
import com.warm.system.entity.PersonalNoPhoneTaskGroupFinsh;
import com.warm.system.mapper.PersonalNoPhoneTaskGroupMapper;
import com.warm.system.service.db1.PersonalNoPhoneTaskGroupService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.utils.VerifyUtils;
import com.warm.utils.WebConst;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.SimpleAttributeSet;
import java.util.Date;
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
public class PersonalNoPhoneTaskGroupServiceImpl extends ServiceImpl<PersonalNoPhoneTaskGroupMapper, PersonalNoPhoneTaskGroup> implements PersonalNoPhoneTaskGroupService {
    private static Log log = LogFactory.getLog(PersonalNoPhoneTaskGroupServiceImpl.class);
    @Autowired
    private PersonalNoPhoneTaskGroupMapper taskGroupMapper;


    @Override
    public PersonalNoPhoneTaskGroup getBySql(Sql sql) {
        return taskGroupMapper.getBySql(sql);
    }

    @Override
    public List<PersonalNoPhoneTaskGroupFinsh> listFinshBySql(Sql sql) {
        return taskGroupMapper.listFinshBySql(sql);
    }

    @Override
    public List<PersonalNoPhoneTaskGroup> listBySql(Sql sql) {
        return taskGroupMapper.listBySql(sql);
    }

    @Override
    public Integer add(PersonalNoPhoneTaskGroup taskGroup) {
        if(VerifyUtils.isEmpty(taskGroup.getId())){
            return taskGroupMapper.add(taskGroup);
        }
        return taskGroupMapper.updateOne(taskGroup);
    }

    @Override
    public Integer deleteBySql(Sql sql) {
        return taskGroupMapper.deleteBySql(sql);
    }
}
