package com.warm.system.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoFriendsCirclePersonal;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
public interface PersonalNoFriendsCirclePersonalMapper extends BaseMapper<PersonalNoFriendsCirclePersonal> {


    @Update("${sql}")
    void updateBySql(Sql sql);
}
