package com.warm.system.mapper;

import com.warm.system.entity.PersonalNoSmallFace;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 表情库 Mapper 接口
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
public interface PersonalNoSmallFaceMapper extends BaseMapper<PersonalNoSmallFace> {

    @Select("SELECT COUNT(1) FROM personal_zc_01.personal_no_small_face")
    int getCount();
    @Select("SELECT Id,face FROM personal_zc_01.personal_no_small_face where id = #{i}")
    PersonalNoSmallFace getById(int i);
    @Select("SELECT Id FROM personal_zc_01.personal_no_small_face")
    List<Integer> listId();
}
