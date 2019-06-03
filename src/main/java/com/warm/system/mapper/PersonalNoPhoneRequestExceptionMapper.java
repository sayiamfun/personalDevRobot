package com.warm.system.mapper;

import com.warm.system.entity.PersonalNoPhoneRequestException;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liwenjie123
 * @since 2019-05-29
 */
public interface PersonalNoPhoneRequestExceptionMapper extends BaseMapper<PersonalNoPhoneRequestException> {

    Integer add(@Param("entity") PersonalNoPhoneRequestException entity);

    Integer updateOne(@Param("entity") PersonalNoPhoneRequestException entity);

}
