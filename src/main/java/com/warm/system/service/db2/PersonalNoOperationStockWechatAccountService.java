package com.warm.system.service.db2;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoOperationStockWechatAccount;
import com.baomidou.mybatisplus.service.IService;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
public interface PersonalNoOperationStockWechatAccountService extends IService<PersonalNoOperationStockWechatAccount> {

    PersonalNoOperationStockWechatAccount getBySql(Sql sql);

    Integer add(PersonalNoOperationStockWechatAccount personalNoOperationStockWechatAccount);

    List<PersonalNoOperationStockWechatAccount> listbySql(Sql sql);

}
