package com.warm.system.service.db1;

import com.baomidou.mybatisplus.service.IService;
import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoTaskRemindFlag;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dgd123
 * @since 2019-04-10
 */
public interface PersonalNoTaskRemindFlagService extends IService<PersonalNoTaskRemindFlag> {


    Integer add(PersonalNoTaskRemindFlag remindFlag);

    PersonalNoTaskRemindFlag getBySql(Sql sql);
}
