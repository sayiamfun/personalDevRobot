package com.warm.system.service.db1;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoFriends;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
public interface PersonalNoFriendsService extends IService<PersonalNoFriends> {

    Integer add(PersonalNoFriends friends);

    PersonalNoFriends getBySql(Sql sql);

    void deleteBySql(Sql sql);

    List<PersonalNoFriends> listBySql(Sql sql);

    Long countBySql(Sql sql);
}
