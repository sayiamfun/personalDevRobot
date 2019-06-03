package com.warm.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoPrismRecord;
import com.warm.system.mapper.PersonalNoPrismRecordMapper;
import com.warm.system.service.db1.PersonalNoPrismRecordService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.utils.VerifyUtils;
import net.bytebuddy.asm.Advice;
import org.apache.velocity.io.VelocityWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
public class PersonalNoPrismRecordServiceImpl extends ServiceImpl<PersonalNoPrismRecordMapper, PersonalNoPrismRecord> implements PersonalNoPrismRecordService {

    @Autowired
    private PersonalNoPrismRecordMapper prismRecordMapper;

    @Override
    public boolean add(PersonalNoPrismRecord robotPrismrecord) {
        if(VerifyUtils.isEmpty(robotPrismrecord.getId())){
            return prismRecordMapper.add(robotPrismrecord)>=0;
        }
        return prismRecordMapper.updateOne(robotPrismrecord)>=0;
    }

    @Override
    public PersonalNoPrismRecord getBySql(Sql sql) {
        return prismRecordMapper.getBySql(sql);
    }
}
