package com.warm.system.mapper;

import com.warm.system.entity.PersonalNoInitiativePersonal;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liwenjie123
 * @since 2019-04-27
 */
public interface PersonalNoInitiativePersonalMapper extends BaseMapper<PersonalNoInitiativePersonal> {

    @Select("select wx_id from personal_zc_01.personal_no_initiative_personal")
    List<String> listWxIdList();
}
