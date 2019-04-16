package com.warm.system.service.db1;

import com.baomidou.mybatisplus.plugins.Page;
import com.warm.entity.requre.QueryRoad;
import com.warm.system.entity.PersonalNoRoad;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
public interface PersonalNoRoadService extends IService<PersonalNoRoad> {

    void pageQuery(Page<PersonalNoRoad> page, QueryRoad queryRoad);
}
