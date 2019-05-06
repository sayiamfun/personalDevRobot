package com.warm.system.mapper;

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

    @Select("SELECT * from personal_zc_01.personal_no_task_and_keyword where task_id = #{param1}")
    List<PersonalNoTaskAndKeyword> listKeywordByTaskId(Integer personalTaskId);
    @Select("SELECT * from personal_zc_01.personal_no_task_and_keyword where task_id = #{param1} and keyword_name = #{param2}")
    PersonalNoTaskAndKeyword getByTaskIdAndKeywordName(Integer personalTaskId, String content);
}
