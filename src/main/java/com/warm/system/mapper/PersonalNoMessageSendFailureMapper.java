package com.warm.system.mapper;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoMessageSendFailure;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liwenjie123
 * @since 2019-06-07
 */
public interface PersonalNoMessageSendFailureMapper extends BaseMapper<PersonalNoMessageSendFailure> {

    @Select("${sql}")
    PersonalNoMessageSendFailure getBySql(Sql sql);

    Integer add(@Param("entity") PersonalNoMessageSendFailure entity);

    Integer updateOne(@Param("entity")PersonalNoMessageSendFailure entity);

}
