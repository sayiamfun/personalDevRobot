package com.warm.system.mapper;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoFriends;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
public interface PersonalNoFriendsMapper extends BaseMapper<PersonalNoFriends> {

    Integer add(@Param("entity") PersonalNoFriends entity);

    Integer updateOne(@Param("entity") PersonalNoFriends entity);

    @Select("${sql}")
    PersonalNoFriends getBySql(Sql sql);

    @Select("${sql}")
    void deleteBySql(Sql sql);

    @Select("${sql}")
    List<PersonalNoFriends> listBySql(Sql sql);

    @Select("${sql}")
    Long countBySql(Sql sql);
}
