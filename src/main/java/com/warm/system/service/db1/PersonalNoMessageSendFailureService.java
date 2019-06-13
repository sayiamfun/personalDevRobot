package com.warm.system.service.db1;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoMessageSendFailure;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liwenjie123
 * @since 2019-06-07
 */
public interface PersonalNoMessageSendFailureService extends IService<PersonalNoMessageSendFailure> {

    Integer add(PersonalNoMessageSendFailure messageSendFailure);

    PersonalNoMessageSendFailure getBySql(Sql sql);
}
