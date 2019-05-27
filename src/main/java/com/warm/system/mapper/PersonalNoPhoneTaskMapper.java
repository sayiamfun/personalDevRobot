package com.warm.system.mapper;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoPhoneTask;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.warm.system.entity.PersonalNoPhoneTaskFinish;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
public interface PersonalNoPhoneTaskMapper extends BaseMapper<PersonalNoPhoneTask> {

    @Select("${sql}")
    List<PersonalNoPhoneTaskFinish> listFinshBySql(Sql sql);

    @Select("${sql}")
    PersonalNoPhoneTask getBySql(Sql sql);

    Integer add(@Param("entity") PersonalNoPhoneTask entity);

    Integer updateOne(@Param("entity")PersonalNoPhoneTask entity);

    @Delete("${sql}")
    Integer deleteBySql(Sql sql);
}
