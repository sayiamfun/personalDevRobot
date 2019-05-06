package com.warm.system.service.db1;

import com.warm.system.entity.PersonalNoTimingNo;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liwenjie123
 * @since 2019-05-05
 */
public interface PersonalNoTimingNoService extends IService<PersonalNoTimingNo> {

    List<String> listWxidList();
}
