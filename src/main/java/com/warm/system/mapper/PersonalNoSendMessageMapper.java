package com.warm.system.mapper;

import com.warm.system.entity.PersonalNoSendMessage;
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
public interface PersonalNoSendMessageMapper extends BaseMapper<PersonalNoSendMessage> {

    @Select("SELECT * from personal_zc_01.personal_no_send_message where personal_wx_id = #{param1} and timing = #{param2} limit 0,1")
    PersonalNoSendMessage getMessageIdByWxIdAndTiming(String s, int i);
    @Select("SELECT * from personal_zc_01.personal_no_send_message where personal_wx_id = #{param1} and timing <> 0")
    List<PersonalNoSendMessage> listByPersonalWxId(String s);
    @Select("SELECT * from personal_zc_01.personal_no_send_message where id = #{personalNoSendMessageId}")
    PersonalNoSendMessage getById(Integer personalNoSendMessageId);
}
