package com.warm.system.service.impl;

import com.warm.common.DBTypeEnum;
import com.warm.common.DataSourceSwitch;
import com.warm.system.entity.PersonalNoRequestException;
import com.warm.system.mapper.PersonalNoRequestExceptionMapper;
import com.warm.system.service.db1.PersonalNoRequestExceptionService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public class PersonalNoRequestExceptionServiceImpl extends ServiceImpl<PersonalNoRequestExceptionMapper, PersonalNoRequestException> implements PersonalNoRequestExceptionService {

    @DataSourceSwitch(DBTypeEnum.db1)
    @Override
    public void insertRequestException(PersonalNoRequestException requestException) {
        baseMapper.insertRequestException(requestException.getMethod(),requestException.getUrl(),requestException.getRequestBody(),requestException.getStatusCode(),requestException.getCreateTime());
    }
}
