package com.warm.system.mapper;

import com.warm.system.entity.PersonalNoRobotLogAndroid;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 机器人上报的日志 Mapper 接口
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
public interface PersonalNoRobotLogAndroidMapper extends BaseMapper<PersonalNoRobotLogAndroid> {

    Integer add(@Param("entity") PersonalNoRobotLogAndroid entity);

    Integer updateOne(@Param("entity")PersonalNoRobotLogAndroid entity);

}
