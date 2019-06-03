package com.warm.system.service.db1;

import com.warm.system.entity.PersonalNoPhoneRequestException;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liwenjie123
 * @since 2019-05-29
 */
public interface PersonalNoPhoneRequestExceptionService extends IService<PersonalNoPhoneRequestException> {

    Integer add(PersonalNoPhoneRequestException phoneRequestException);
}
