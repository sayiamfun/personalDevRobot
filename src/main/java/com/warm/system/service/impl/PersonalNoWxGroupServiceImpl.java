package com.warm.system.service.impl;

import com.warm.system.entity.PersonalNoWxGroup;
import com.warm.system.mapper.PersonalNoWxGroupMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.system.service.db3.PersonalNoWxGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
@Service
public class PersonalNoWxGroupServiceImpl extends ServiceImpl<PersonalNoWxGroupMapper, PersonalNoWxGroup> implements PersonalNoWxGroupService {

    @Autowired
    private PersonalNoWxGroupMapper wxGroupMapper;

    @Override
    public String getByCategoryId(int flag, int parseInt) {
        String result = null;
        switch (flag){
            case 0:
                result = wxGroupMapper.getByCategoryId(parseInt);
                break;
            case 1:
                result = wxGroupMapper.getByCategoryIdFromQunLie01(parseInt);
                break;
        }
        return result;
    }
}
