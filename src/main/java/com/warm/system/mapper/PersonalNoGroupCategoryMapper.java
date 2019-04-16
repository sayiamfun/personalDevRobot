package com.warm.system.mapper;

import com.warm.system.entity.PersonalNoGroupCategory;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
public interface PersonalNoGroupCategoryMapper extends BaseMapper<PersonalNoGroupCategory> {

    @Select("SELECT id,group_category_set_id,cname,up_limit,prefix,postfix,begin_index,current_index,assistant_ids,full_verify,cdescription,create_time FROM personal_no_group_category WHERE group_category_set_id = #{setId} ORDER BY id desc")
    List<PersonalNoGroupCategory> listBySetId(Integer setId);
}
