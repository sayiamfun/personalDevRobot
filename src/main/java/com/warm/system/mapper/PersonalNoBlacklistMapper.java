package com.warm.system.mapper;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoBlacklist;
import com.baomidou.mybatisplus.mapper.BaseMapper;
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
public interface PersonalNoBlacklistMapper extends BaseMapper<PersonalNoBlacklist> {


    @Select("${sql}")
    List<String> listStringBySql(Sql sql);

    @Select("${sql}")
    PersonalNoBlacklist getBySql(Sql sql);
}
