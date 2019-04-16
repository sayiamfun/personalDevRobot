package com.warm.system.mapper;

import com.warm.system.entity.PersonalNoPhoneRequestTaskTime;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;

import java.util.Date;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
public interface PersonalNoPhoneRequestTaskTimeMapper extends BaseMapper<PersonalNoPhoneRequestTaskTime> {

    @Insert("INSERT INTO personal_zc_01.personal_no_phone_request_task_time   ( wx_id,  nick_name,  request_time )  VALUES   ( #{param1},  #{param2}, #{param3} )")
    void insert(String wxId, String nickName, Date requestTime);
}
