package com.warm.system.mapper;

import com.warm.system.entity.PersonalNoPhoneTaskFinish;
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
public interface PersonalNoPhoneTaskFinishMapper extends BaseMapper<PersonalNoPhoneTaskFinish> {

    Integer add(@Param("entity") PersonalNoPhoneTaskFinish entity);

    Integer updateOne(@Param("entity")PersonalNoPhoneTaskFinish entity);

}
