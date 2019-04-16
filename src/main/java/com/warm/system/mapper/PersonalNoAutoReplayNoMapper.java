package com.warm.system.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.warm.system.entity.PersonalNoAutoReplayNo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dgd123
 * @since 2019-04-03
 */
public interface PersonalNoAutoReplayNoMapper extends BaseMapper<PersonalNoAutoReplayNo> {

    @Select("select wx_id from personal_zc_01.personal_no_auto_replay_no")
    List<String> listWxId();
}
