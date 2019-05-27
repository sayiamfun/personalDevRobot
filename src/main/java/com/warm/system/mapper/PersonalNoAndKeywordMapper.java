package com.warm.system.mapper;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoAndKeyword;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dgd123
 * @since 2019-04-17
 */
public interface PersonalNoAndKeywordMapper extends BaseMapper<PersonalNoAndKeyword> {

    @Select("${sql}")
    PersonalNoAndKeyword getBySql(Sql sql);
}
