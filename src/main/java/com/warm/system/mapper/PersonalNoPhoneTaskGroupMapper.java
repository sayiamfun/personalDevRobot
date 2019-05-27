package com.warm.system.mapper;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoPhoneTaskGroup;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.warm.system.entity.PersonalNoPhoneTaskGroupFinsh;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
public interface PersonalNoPhoneTaskGroupMapper extends BaseMapper<PersonalNoPhoneTaskGroup> {

    @Select("${sql}")
    PersonalNoPhoneTaskGroup getBySql(Sql sql);

    @Select("${sql}")
    List<PersonalNoPhoneTaskGroupFinsh> listFinshBySql(Sql sql);

    Integer add(@Param("entity") PersonalNoPhoneTaskGroup entity);

    Integer updateOne(@Param("entity")PersonalNoPhoneTaskGroup entity);

    @Select("${sql}")
    List<PersonalNoPhoneTaskGroup> listBySql(Sql sql);

    @Delete("${sql}")
    Integer deleteBySql(Sql sql);
}
