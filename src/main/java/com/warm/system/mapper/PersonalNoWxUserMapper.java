package com.warm.system.mapper;

import com.warm.system.entity.PersonalNoWxUser;
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
public interface PersonalNoWxUserMapper extends BaseMapper<PersonalNoWxUser> {

    @Select("SELECT id,wx_id,open_id,unionid,alias,is_assistant,nick_name,real_name,softbank_name,head_photo,wx_qrcode,sex,province,country,city,email,whats_up,con_remark,privilege,create_time,is_black,black_reason FROM personal_zc_01.personal_no_wx_user WHERE is_black = #{i}")
    List<PersonalNoWxUser> listByis_bask(int i);

    @Select("SELECT id,wx_id,open_id,unionid,alias,is_assistant,nick_name,real_name,softbank_name,head_photo,wx_qrcode,sex,province,country,city,email,whats_up,con_remark,privilege,create_time,is_black,black_reason FROM personal_zc_01.personal_no_wx_user WHERE is_assistant = #{i}")
    List<PersonalNoWxUser> listByis_assistant(int i);

    @Select("SELECT id,wx_id,open_id,unionid,alias,is_assistant,nick_name,real_name,softbank_name,head_photo,wx_qrcode,sex,province,country,city,email,whats_up,con_remark,privilege,create_time,is_black,black_reason FROM personal_zc_01.personal_no_wx_user WHERE open_id = #{openid}")
    List<PersonalNoWxUser> listByOpenId(String openid);
}
