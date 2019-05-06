package com.warm.system.mapper;

import com.warm.system.entity.PersonalNoTimingNo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liwenjie123
 * @since 2019-05-05
 */
public interface PersonalNoTimingNoMapper extends BaseMapper<PersonalNoTimingNo> {

    @Select("SELECT wx_id from personal_zc_01.personal_no_timing_no")
    List<String> listWxidList();
}
