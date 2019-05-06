package com.warm.system.service.db1;

import com.warm.system.entity.PersonalNoInitiativePersonal;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liwenjie123
 * @since 2019-04-27
 */
public interface PersonalNoInitiativePersonalService extends IService<PersonalNoInitiativePersonal> {

    List<String> listWxIdList();
}
