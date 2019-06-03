package com.warm.system.service.db1;

import com.baomidou.mybatisplus.service.IService;
import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoFriendsCirclePersonal;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
public interface PersonalNoFriendsCirclePersonalService extends IService<PersonalNoFriendsCirclePersonal> {

    void updateBySql(Sql sql);
}
