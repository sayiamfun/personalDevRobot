package com.warm.system.service.db1;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoPhoneTaskGroup;
import com.baomidou.mybatisplus.service.IService;
import com.warm.system.entity.PersonalNoPhoneTaskGroupFinsh;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
public interface PersonalNoPhoneTaskGroupService extends IService<PersonalNoPhoneTaskGroup> {

    PersonalNoPhoneTaskGroup getBySql(Sql sql);

    List<PersonalNoPhoneTaskGroupFinsh> listFinshBySql(Sql sql);

    List<PersonalNoPhoneTaskGroup> listBySql(Sql sql);

    Integer add(PersonalNoPhoneTaskGroup taskGroup);

    Integer deleteBySql(Sql sql);

    Long countBySql(Sql sql);
}
