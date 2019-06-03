package com.warm.system.service.impl;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoValueTable;
import com.warm.system.mapper.PersonalNoValueTableMapper;
import com.warm.system.service.db1.PersonalNoValueTableService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.utils.VerifyUtils;
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
public class PersonalNoValueTableServiceImpl extends ServiceImpl<PersonalNoValueTableMapper, PersonalNoValueTable> implements PersonalNoValueTableService {
    @Autowired
    private PersonalNoValueTableMapper valueTableMapper;


    @Override
    public PersonalNoValueTable getBySql(Sql sql) {
        return valueTableMapper.getBySql(sql);
    }

    @Override
    public List<String> listBySql(Sql sql) {
        return valueTableMapper.listBySql(sql);
    }

    @Override
    public List<Integer> listStringBySql(Sql sql) {
        return valueTableMapper.listIntegerBySql(sql);
    }
}
