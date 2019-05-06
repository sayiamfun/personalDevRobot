package com.warm.system.service.impl;

import com.warm.system.entity.PersonalNoInitiativePersonal;
import com.warm.system.mapper.PersonalNoInitiativePersonalMapper;
import com.warm.system.service.db1.PersonalNoInitiativePersonalService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liwenjie123
 * @since 2019-04-27
 */
@Service
public class PersonalNoInitiativePersonalServiceImpl extends ServiceImpl<PersonalNoInitiativePersonalMapper, PersonalNoInitiativePersonal> implements PersonalNoInitiativePersonalService {

    @Autowired
    private PersonalNoInitiativePersonalMapper initiativePersonalMapper;
    @Override
    public List<String> listWxIdList() {
        return initiativePersonalMapper.listWxIdList();
    }
}
