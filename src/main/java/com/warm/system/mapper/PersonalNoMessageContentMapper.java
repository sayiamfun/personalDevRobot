package com.warm.system.mapper;

import com.warm.system.entity.PersonalNoMessageContent;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liwenjie123
 * @since 2019-05-06
 */
public interface PersonalNoMessageContentMapper extends BaseMapper<PersonalNoMessageContent> {

    @Select("SELECT * from personal_zc_01.personal_no_message_content where message_id = #{param1}")
    List<PersonalNoMessageContent> listByMessageId(Integer messageId);
}
