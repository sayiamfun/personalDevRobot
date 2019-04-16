package com.warm.system.service.db1;

import com.warm.system.entity.PersonalNoTemp;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 存储用户的交互数据 服务类
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
public interface PersonalNoTempService extends IService<PersonalNoTemp> {

    PersonalNoTemp getByPersonalIdAndUserWxId(String s, String fromUsername);

    void insertPersonalNoTemp(PersonalNoTemp personalNoTemp);

    List<PersonalNoTemp> listByPersonalWxIdAndTimeAndFlag(String wxId, int i);

    void updateFlagById(PersonalNoTemp personalNoTemp);
}
