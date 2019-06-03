package com.warm.system.service.impl;

import com.warm.system.entity.PersonalNoPhoneRequestException;
import com.warm.system.mapper.PersonalNoPhoneRequestExceptionMapper;
import com.warm.system.service.db1.PersonalNoPhoneRequestExceptionService;
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
 * @since 2019-05-29
 */
@Service
public class PersonalNoPhoneRequestExceptionServiceImpl extends ServiceImpl<PersonalNoPhoneRequestExceptionMapper, PersonalNoPhoneRequestException> implements PersonalNoPhoneRequestExceptionService {

    @Autowired
    private PersonalNoPhoneRequestExceptionMapper phoneRequestExceptionMapper;

    @Override
    public Integer add(PersonalNoPhoneRequestException phoneRequestException) {
        if(VerifyUtils.isEmpty(phoneRequestException.getId())){
            return phoneRequestExceptionMapper.add(phoneRequestException);
        }
        return phoneRequestExceptionMapper.updateOne(phoneRequestException);
    }
}
