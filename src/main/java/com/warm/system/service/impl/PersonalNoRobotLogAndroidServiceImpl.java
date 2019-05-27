package com.warm.system.service.impl;

import com.warm.system.entity.PersonalNoRobotLogAndroid;
import com.warm.system.mapper.PersonalNoRobotLogAndroidMapper;
import com.warm.system.service.db1.PersonalNoRobotLogAndroidService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.utils.VerifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 机器人上报的日志 服务实现类
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
@Service
public class PersonalNoRobotLogAndroidServiceImpl extends ServiceImpl<PersonalNoRobotLogAndroidMapper, PersonalNoRobotLogAndroid> implements PersonalNoRobotLogAndroidService {

    @Autowired
    private PersonalNoRobotLogAndroidMapper robotLogAndroidMapper;

    @Override
    public Integer add(PersonalNoRobotLogAndroid tempRL) {
        if(VerifyUtils.isEmpty(tempRL.getId())){
            return robotLogAndroidMapper.add(tempRL);
        }
        return robotLogAndroidMapper.updateOne(tempRL);
    }
}
