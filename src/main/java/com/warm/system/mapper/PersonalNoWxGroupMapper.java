package com.warm.system.mapper;

import com.warm.system.entity.PersonalNoWxGroup;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
public interface PersonalNoWxGroupMapper extends BaseMapper<PersonalNoWxGroup> {

    @Select("SELECT wx_group_office_id FROM personal_zc_wx_group.wx_group WHERE group_category_id = #{parseInt} AND tag = '活跃群' LIMIT 0,1")
    String getByCategoryId(int parseInt);
    @Select("SELECT wx_group_office_id FROM qunliebian_01.wx_group WHERE group_category_id = #{parseInt} AND tag = '活跃群' LIMIT 0,1")
    String getByCategoryIdFromQunLie01(int parseInt);
}
