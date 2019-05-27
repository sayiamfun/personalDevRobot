package com.warm.system.service.impl;

import com.warm.system.entity.PersonalNoPhoneTaskFinish;
import com.warm.system.mapper.PersonalNoPhoneTaskFinishMapper;
import com.warm.system.service.db1.PersonalNoPhoneTaskFinishService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.utils.VerifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liwenjie123
 * @since 2019-05-22
 */
@Service
public class PersonalNoPhoneTaskFinishServiceImpl extends ServiceImpl<PersonalNoPhoneTaskFinishMapper, PersonalNoPhoneTaskFinish> implements PersonalNoPhoneTaskFinishService {

    @Autowired
    private PersonalNoPhoneTaskFinishMapper phoneTaskFinishMapper;

    @Override
    public Integer add(PersonalNoPhoneTaskFinish personalNoPhoneTaskFinish) {
        if(VerifyUtils.isEmpty(personalNoPhoneTaskFinish.getId())){
            return phoneTaskFinishMapper.add(personalNoPhoneTaskFinish);
        }
        return phoneTaskFinishMapper.updateOne(personalNoPhoneTaskFinish);
    }
}
