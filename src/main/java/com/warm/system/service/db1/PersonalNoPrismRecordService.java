package com.warm.system.service.db1;

import com.warm.system.entity.PersonalNoPrismRecord;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
public interface PersonalNoPrismRecordService extends IService<PersonalNoPrismRecord> {

    boolean getByPrismRecordService(PersonalNoPrismRecord currPR);
}
