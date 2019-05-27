package com.warm.system.service.impl;

import com.warm.system.entity.PersonalNoPhoneTaskGroupFinsh;
import com.warm.system.mapper.PersonalNoPhoneTaskGroupFinshMapper;
import com.warm.system.service.db1.PersonalNoPhoneTaskGroupFinshService;
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
public class PersonalNoPhoneTaskGroupFinshServiceImpl extends ServiceImpl<PersonalNoPhoneTaskGroupFinshMapper, PersonalNoPhoneTaskGroupFinsh> implements PersonalNoPhoneTaskGroupFinshService {

    @Autowired
    private PersonalNoPhoneTaskGroupFinshMapper phoneTaskGroupFinshMapper;


    @Override
    public Integer add(PersonalNoPhoneTaskGroupFinsh personalNoPhoneTaskGroupFinsh) {
        if(VerifyUtils.isEmpty(personalNoPhoneTaskGroupFinsh.getId())){
            return phoneTaskGroupFinshMapper.add(personalNoPhoneTaskGroupFinsh);
        }
        return phoneTaskGroupFinshMapper.updateOne(personalNoPhoneTaskGroupFinsh);
    }
}
