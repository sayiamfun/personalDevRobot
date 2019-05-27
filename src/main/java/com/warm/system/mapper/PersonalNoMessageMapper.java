package com.warm.system.mapper;

import com.warm.system.entity.PersonalNoMessage;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liwenjie123
 * @since 2019-05-06
 */
public interface PersonalNoMessageMapper extends BaseMapper<PersonalNoMessage> {

    @Select("SELECT * from personal_zc_01.personal_no_message where id = #{messageId}")
    PersonalNoMessage getById(Integer messageId);
}
