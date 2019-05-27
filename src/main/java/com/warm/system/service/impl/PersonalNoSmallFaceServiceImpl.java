package com.warm.system.service.impl;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoSmallFace;
import com.warm.system.mapper.PersonalNoSmallFaceMapper;
import com.warm.system.service.db1.PersonalNoSmallFaceService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 表情库 服务实现类
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
@Service
public class PersonalNoSmallFaceServiceImpl extends ServiceImpl<PersonalNoSmallFaceMapper, PersonalNoSmallFace> implements PersonalNoSmallFaceService {
    @Autowired
    private PersonalNoSmallFaceMapper smallFaceMapper;

    @Override
    public List<Integer> listIntegerBySql(Sql sql) {
        return smallFaceMapper.listIntegerBySql(sql);
    }

    @Override
    public PersonalNoSmallFace getBySql(Sql sql) {
        return smallFaceMapper.getBySql(sql);
    }
}
