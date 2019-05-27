package com.warm.system.mapper;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoSmallFace;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 表情库 Mapper 接口
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
public interface PersonalNoSmallFaceMapper extends BaseMapper<PersonalNoSmallFace> {

    @Select("${sql}")
    List<Integer> listIntegerBySql(Sql sql);

    @Select("${sql}")
    PersonalNoSmallFace getBySql(Sql sql);
}
