package com.warm.system.mapper;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoPeople;
import com.baomidou.mybatisplus.mapper.BaseMapper;
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
public interface PersonalNoPeopleMapper extends BaseMapper<PersonalNoPeople> {

    @Update("${sql}")
    void deleteBySql(Sql sql);

    @Select("${sql}")
    PersonalNoPeople getBySql(Sql sql);

    Integer add(@Param("entity") PersonalNoPeople entity);

    Integer updateOne(@Param("entity")PersonalNoPeople entity);

    @Select("${sql}")
    List<PersonalNoPeople> listBySql(Sql sql);
}
