package com.warm.system.mapper;

import com.warm.system.entity.PersonalNoRequestException;
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
public interface PersonalNoRequestExceptionMapper extends BaseMapper<PersonalNoRequestException> {
    @Insert("INSERT INTO personal_zc_01.personal_no_request_exception   ( `method`,url,request_body,status_code,create_time ) VALUES ( #{param1},#{param2},#{param3},#{param4},#{param5} )")
    void insertRequestException(String method, String url, String requestBody, Integer statusCode, Date createTime);
}
