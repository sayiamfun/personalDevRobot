package com.warm.system.mapper;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoTemp;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 存储用户的交互数据 Mapper 接口
 * </p>
 *
 * @author liwenjie123
 * @since 2019-05-22
 */
public interface PersonalNoTempMapper extends BaseMapper<PersonalNoTemp> {

    @Select("${sql}")
    List<PersonalNoTemp> listBySql(Sql sql);

    Integer add(@Param("entity") PersonalNoTemp entity);

    Integer updateOne(@Param("entity")PersonalNoTemp entity);

    @Select("${sql}")
    PersonalNoTemp getBySql(Sql sql);
}
