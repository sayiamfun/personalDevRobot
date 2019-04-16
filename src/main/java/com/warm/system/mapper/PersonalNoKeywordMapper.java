package com.warm.system.mapper;

import com.warm.system.entity.PersonalNoKeyword;
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
public interface PersonalNoKeywordMapper extends BaseMapper<PersonalNoKeyword> {
    @Select("SELECT id,keyword,deleted from personal_zc_01.personal_no_keyword where id NOT in (1,2,3)")
    List<PersonalNoKeyword> listAll();
}
