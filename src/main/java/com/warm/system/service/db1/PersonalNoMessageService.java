package com.warm.system.service.db1;

import com.warm.system.entity.PersonalNoMessage;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liwenjie123
 * @since 2019-05-06
 */
public interface PersonalNoMessageService extends IService<PersonalNoMessage> {

    PersonalNoMessage getById(Integer messageId);
}
