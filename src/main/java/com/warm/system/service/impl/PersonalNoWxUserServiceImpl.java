package com.warm.system.service.impl;

import com.warm.system.entity.PersonalNoWxUser;
import com.warm.system.mapper.PersonalNoWxUserMapper;
import com.warm.system.service.db1.PersonalNoWxUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
@Service
public class PersonalNoWxUserServiceImpl extends ServiceImpl<PersonalNoWxUserMapper, PersonalNoWxUser> implements PersonalNoWxUserService {

    @Autowired
    private PersonalNoWxUserMapper wxUserMapper;
    @Override
    public List<PersonalNoWxUser> listByis_bask(int i) {
        return wxUserMapper.listByis_bask(i);
    }

    @Override
    public List<PersonalNoWxUser> listByis_assistant(int i) {
        return wxUserMapper.listByis_assistant(i);
    }

    @Override
    public List<PersonalNoWxUser> listByOpenId(String openid) {
        return wxUserMapper.listByOpenId(openid);
    }
}
