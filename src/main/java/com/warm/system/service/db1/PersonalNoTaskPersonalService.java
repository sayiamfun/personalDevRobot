package com.warm.system.service.db1;

import com.baomidou.mybatisplus.service.IService;
import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoTaskPersonal;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
public interface PersonalNoTaskPersonalService extends IService<PersonalNoTaskPersonal> {

    void updateBySql(Sql sql);
}
