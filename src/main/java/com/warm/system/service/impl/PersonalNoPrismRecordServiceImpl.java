package com.warm.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.warm.system.entity.PersonalNo;
import com.warm.system.entity.PersonalNoPrismRecord;
import com.warm.system.mapper.PersonalNoPrismRecordMapper;
import com.warm.system.service.db1.PersonalNoPrismRecordService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.utils.VerifyUtils;
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
    /**
     * 根据参数判空
     * @param currPR
     * @return
     */
    @Override
    public boolean getByPrismRecordService(PersonalNoPrismRecord currPR) {
        boolean flag = false;
        EntityWrapper<PersonalNoPrismRecord> entityWrapper = new EntityWrapper<>();
        entityWrapper.orderDesc(Arrays.asList(new String[]{"id"}));
        if(!VerifyUtils.isEmpty(currPR.getChatroom())){
            entityWrapper.eq("chatroom",  currPR.getChatroom());
        }
        if(!VerifyUtils.isEmpty(currPR.getCreateTime())){
            entityWrapper.eq("create_time", currPR.getCreateTime());
        }
        if(!VerifyUtils.isEmpty(currPR.getMd5())){
            entityWrapper.eq("md5", currPR.getMd5());
        }
        if(!VerifyUtils.isEmpty(currPR.getContent())){
            entityWrapper.eq("content", currPR.getContent().replace("'", "\'"));
        }
        List<PersonalNoPrismRecord> prismRecordList = baseMapper.selectList(entityWrapper);
        if(!VerifyUtils.collectionIsEmpty(prismRecordList)){
            flag = true;
        }
        return flag;
    }
}
