package com.warm.system.service.impl;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoTemp;
import com.warm.system.mapper.PersonalNoTempMapper;
import com.warm.system.service.db1.PersonalNoTempService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.utils.VerifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 存储用户的交互数据 服务实现类
 * </p>
 *
 * @author liwenjie123
 * @since 2019-05-22
 */
@Service
public class PersonalNoTempServiceImpl extends ServiceImpl<PersonalNoTempMapper, PersonalNoTemp> implements PersonalNoTempService {

    @Autowired
    private PersonalNoTempMapper tempMapper;

    @Override
    public List<PersonalNoTemp> listBysql(Sql sql) {
        return tempMapper.listBySql(sql);
    }

    @Override
    public Integer add(PersonalNoTemp personalNoTemp) {
        if(VerifyUtils.isEmpty(personalNoTemp.getId())){
            return tempMapper.add(personalNoTemp);
        }
        return tempMapper.updateOne(personalNoTemp);
    }

    @Override
    public PersonalNoTemp getBySql(Sql sql) {
        return tempMapper.getBySql(sql);
    }
}
