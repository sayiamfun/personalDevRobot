package com.warm.system.mapper;

import com.warm.system.entity.PersonalNoPhoneTaskGroupFinsh;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liwenjie123
 * @since 2019-05-22
 */
public interface PersonalNoPhoneTaskGroupFinshMapper extends BaseMapper<PersonalNoPhoneTaskGroupFinsh> {

    Integer add(@Param("entity") PersonalNoPhoneTaskGroupFinsh entity);

    Integer updateOne(@Param("entity")PersonalNoPhoneTaskGroupFinsh entity);

}
