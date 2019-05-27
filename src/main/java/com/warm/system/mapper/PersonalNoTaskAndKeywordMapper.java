package com.warm.system.mapper;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoTaskAndKeyword;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liwenjie123
 * @since 2019-04-28
 */
public interface PersonalNoTaskAndKeywordMapper extends BaseMapper<PersonalNoTaskAndKeyword> {

    @Select("${sql}")
    PersonalNoTaskAndKeyword getBySql(Sql sql);
}
