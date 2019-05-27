package com.warm.system.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.warm.system.entity.PassageVisitorRecord;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dgd123
 * @since 2019-05-09
 */
public interface PassageVisitorRecordMapper extends BaseMapper<PassageVisitorRecord> {

    @Select("SELECT * FROM personal_zc_01.passage_visitor_record where task_id = #{param1} and channel_id = #{param2} and union_id = #{param3} order by id desc limit 0,1")
    PassageVisitorRecord getByTaskIdAndChannelIdAndUnionId(Integer p_id, Integer channelId, String unionid);

    @Select("SELECT * FROM personal_zc_01.passage_visitor_record where union_id = #{unionid} order by id desc limit 0,1")
    PassageVisitorRecord getByUnionId(String unionid);

    @Update("UPDATE personal_zc_01.passage_visitor_record SET user_wx_id = #{param1} where id = #{param2}")
    void updateUserWxIdById(String wxId, Integer id);
}
