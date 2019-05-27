package com.warm.system.service.db1;

import com.warm.system.entity.PersonalNoPhoneTaskFinish;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liwenjie123
 * @since 2019-05-22
 */
public interface PersonalNoPhoneTaskFinishService extends IService<PersonalNoPhoneTaskFinish> {

    Integer add(PersonalNoPhoneTaskFinish personalNoPhoneTaskFinish);
}
