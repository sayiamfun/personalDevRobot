package com.warm.system.mapper;

import com.warm.system.entity.PersonalNoTemp;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 存储用户的交互数据 Mapper 接口
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
public interface PersonalNoTempMapper extends BaseMapper<PersonalNoTemp> {

    @Select("select id,personal_no_wx_id,user_wx_id,create_time,flag from personal_zc_01.personal_no_temp where personal_no_wx_id = #{param1} and user_wx_id= #{param2} limit 0,1")
    PersonalNoTemp getByPersonalIdAndUserWxId(String s, String fromUsername);
    @Select("select id,personal_no_wx_id,user_wx_id,create_time,flag from personal_zc_01.personal_no_temp where personal_no_wx_id = #{param1} and create_time < #{param2} and flag = 0")
    List<PersonalNoTemp> listByPersonalWxIdAndTimeAndFlag(String wxId, String toString, int i);
    @Update("UPDATE personal_zc_01.personal_no_temp SET flag = #{param1} where id = #{param2}")
    void updateFlagById(Integer flag, Integer id);
}
