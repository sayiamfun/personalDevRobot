package com.warm.system.service.impl;

import com.warm.system.entity.PersonalNoPhoneRequestTaskTime;
import com.warm.system.mapper.PersonalNoPhoneRequestTaskTimeMapper;
import com.warm.system.service.db1.PersonalNoPhoneRequestTaskTimeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.utils.VerifyUtils;
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
public class PersonalNoPhoneRequestTaskTimeServiceImpl extends ServiceImpl<PersonalNoPhoneRequestTaskTimeMapper, PersonalNoPhoneRequestTaskTime> implements PersonalNoPhoneRequestTaskTimeService {

    @Autowired
    private PersonalNoPhoneRequestTaskTimeMapper phoneRequestTaskTimeMapper;

    @Override
    public Integer add(PersonalNoPhoneRequestTaskTime phoneRequestTaskTime) {
        if(VerifyUtils.isEmpty(phoneRequestTaskTime.getId())){
            return phoneRequestTaskTimeMapper.add(phoneRequestTaskTime);
        }
        return phoneRequestTaskTimeMapper.updateOne(phoneRequestTaskTime);
    }
}
