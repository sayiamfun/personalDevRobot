package com.warm.system.service.impl;

import com.warm.common.DBTypeEnum;
import com.warm.common.DataSourceSwitch;
import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoOperationStockWechatAccount;
import com.warm.system.mapper.PersonalNoOperationStockWechatAccountMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.system.service.db2.PersonalNoOperationStockWechatAccountService;
import com.warm.utils.VerifyUtils;
import com.warm.utils.WebConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
public class PersonalNoOperationStockWechatAccountServiceImpl extends ServiceImpl<PersonalNoOperationStockWechatAccountMapper, PersonalNoOperationStockWechatAccount> implements PersonalNoOperationStockWechatAccountService {
    @Autowired
    private PersonalNoOperationStockWechatAccountMapper operationStockWechatAccountMapper;


    @Override
    public PersonalNoOperationStockWechatAccount getBySql(Sql sql) {
        return operationStockWechatAccountMapper.getBySql(sql);
    }

    @Override
    public Integer add(PersonalNoOperationStockWechatAccount personalNoOperationStockWechatAccount) {
        if(VerifyUtils.isEmpty(personalNoOperationStockWechatAccount.getId())){
            return operationStockWechatAccountMapper.add(personalNoOperationStockWechatAccount);
        }
        return operationStockWechatAccountMapper.updateOne(personalNoOperationStockWechatAccount);
    }

    @Override
    public List<PersonalNoOperationStockWechatAccount> listbySql(Sql sql) {
        return operationStockWechatAccountMapper.listBySql(sql);
    }
}
