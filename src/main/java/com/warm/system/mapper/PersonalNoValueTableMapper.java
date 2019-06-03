package com.warm.system.mapper;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoValueTable;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
public interface PersonalNoValueTableMapper extends BaseMapper<PersonalNoValueTable> {

    @Select("${sql}")
    PersonalNoValueTable getBySql(Sql sql);

    @Select("${sql}")
    List<String> listBySql(Sql sql);

    @Select("${sql}")
    List<Integer> listIntegerBySql(Sql sql);
}
