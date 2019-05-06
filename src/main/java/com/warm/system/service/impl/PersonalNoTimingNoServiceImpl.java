package com.warm.system.service.impl;

import com.warm.system.entity.PersonalNoTimingNo;
import com.warm.system.mapper.PersonalNoTimingNoMapper;
import com.warm.system.service.db1.PersonalNoTimingNoService;
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
 * @since 2019-05-05
 */
@Service
public class PersonalNoTimingNoServiceImpl extends ServiceImpl<PersonalNoTimingNoMapper, PersonalNoTimingNo> implements PersonalNoTimingNoService {

    @Autowired
    private PersonalNoTimingNoMapper timingNoMapper;
    @Override
    public List<String> listWxidList() {
        return timingNoMapper.listWxidList();
    }
}
