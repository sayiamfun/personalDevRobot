package com.warm.system.mapper;

import com.warm.system.entity.PersonalNoFriends;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
public interface PersonalNoFriendsMapper extends BaseMapper<PersonalNoFriends> {
    //根据个人号id和用户微信id查询用户好友信息
    @Select("SELECT id,personal_no_id,personal_no_wx_id,user_id,user_wx_id,be_friend_time,remarks,deleted FROM personal_zc_01.personal_no_friends WHERE personal_no_id = #{param1} and user_wx_id = #{param2} and deleted=0 LIMIT 0,1")
    PersonalNoFriends getByPersonalIdAndUserWxId(Integer logicId, String username);
    //根据个人号微信id查询所有的个人号好友
    @Select("SELECT id,personal_no_id,personal_no_wx_id,user_id,user_wx_id,be_friend_time,remarks,deleted FROM personal_zc_01.personal_no_friends WHERE personal_no_wx_id = #{username} and deleted=0")
    List<PersonalNoFriends> listByPersonalWxId(String username);
    //根据个人号微信id查询所有的好友微信id
    @Select("SELECT user_wx_id FROM personal_zc_01.personal_no_friends WHERE personal_no_wx_id = #{personalWxId} and deleted=0")
    List<String> listUserWxIdByPersonalWxId(String personalWxId);
    //根据个人号微信id和用户微信id查询个人号好友
    @Select("SELECT id,personal_no_id,personal_no_wx_id,user_id,user_wx_id,be_friend_time,remarks,deleted FROM personal_zc_01.personal_no_friends WHERE personal_no_wx_id = #{param1} and user_wx_id = #{param2} and deleted=0 LIMIT 0,1")
    PersonalNoFriends getByPersonalWxIdAndUserWxId(String personalNoWxId, String wxId);
    //根据个人号微信id和用户微信id查询所有的个人号好友id
    @Select("SELECT id FROM personal_zc_01.personal_no_friends WHERE personal_no_wx_id = #{param1} and user_wx_id = #{param2} and deleted=0")
    List<Integer> listByPersonalWxIdAndUserWxId(String wxId, String fromUsername);
    @Delete("DELETE FROM personal_zc_01.personal_no_friends WHERE id in #{param1}")
    void deleteByIds(StringBuffer temp);
    @Insert("INSERT INTO personal_zc_01.personal_no_friends   ( personal_no_wx_id,  user_id,  user_wx_id,  be_friend_time )  VALUES   (#{param1},#{param2},#{param3},#{param4})")
    void insertInfo(String personalNoWxId, Integer userId, String userWxId, Date beFriendTime);
    @Update({"update personal_zc_01.personal_no_friends set personal_no_wx_id = #{param1},user_id = #{param2},user_wx_id = #{param3},be_friend_time = #{param4} where id = #{param5}"})
    void updateInfoById(String personalNoWxId, Integer userId, String userWxId, Date beFriendTime, Integer id);
}
