package com.warm.system.service.db1;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoPhoneTask;
import com.baomidou.mybatisplus.service.IService;
import com.warm.system.entity.PersonalNoPhoneTaskFinish;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
public interface PersonalNoPhoneTaskService extends IService<PersonalNoPhoneTask> {

    List<PersonalNoPhoneTaskFinish> listFinshBySql(Sql sql);

    PersonalNoPhoneTask getBySql(Sql sql);

    Integer add(PersonalNoPhoneTask byId);

    Integer deleteBySql(Sql sql);
}
