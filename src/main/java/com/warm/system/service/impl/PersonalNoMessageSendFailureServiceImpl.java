package com.warm.system.service.impl;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoMessageSendFailure;
import com.warm.system.mapper.PersonalNoMessageSendFailureMapper;
import com.warm.system.service.db1.PersonalNoMessageSendFailureService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.utils.VerifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liwenjie123
 * @since 2019-06-07
 */
@Service
public class PersonalNoMessageSendFailureServiceImpl extends ServiceImpl<PersonalNoMessageSendFailureMapper, PersonalNoMessageSendFailure> implements PersonalNoMessageSendFailureService {

    @Autowired
    private PersonalNoMessageSendFailureMapper messageSendFailureMapper;
    @Override
    public Integer add(PersonalNoMessageSendFailure messageSendFailure) {
        if(VerifyUtils.isEmpty(messageSendFailure.getId())){
            return messageSendFailureMapper.add(messageSendFailure);
        }
        return messageSendFailureMapper.updateOne(messageSendFailure);
    }

    @Override
    public PersonalNoMessageSendFailure getBySql(Sql sql) {
        return messageSendFailureMapper.getBySql(sql);
    }
}
