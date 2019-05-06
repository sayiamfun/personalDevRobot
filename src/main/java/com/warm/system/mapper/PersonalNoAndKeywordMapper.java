package com.warm.system.mapper;

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

    @Select("SELECT * from personal_zc_01.personal_no_and_keyword where personal_no_wx_id = #{param1} and keyword_name = #{param2}")
    PersonalNoAndKeyword listByWxIdAndKeyword(String s, String content);
}
