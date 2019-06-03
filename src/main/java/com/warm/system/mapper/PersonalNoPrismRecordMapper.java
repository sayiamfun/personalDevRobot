package com.warm.system.mapper;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoPrismRecord;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
public interface PersonalNoPrismRecordMapper extends BaseMapper<PersonalNoPrismRecord> {

    Integer add(@Param("entity") PersonalNoPrismRecord entity);

    Integer updateOne(@Param("entity") PersonalNoPrismRecord entity);

    @Select("${sql}")
    PersonalNoPrismRecord getBySql(Sql sql);
}
