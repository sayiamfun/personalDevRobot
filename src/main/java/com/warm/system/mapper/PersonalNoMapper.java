package com.warm.system.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.warm.entity.query.QueryPersonal;
import com.warm.system.entity.PersonalNo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
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
public interface PersonalNoMapper extends BaseMapper<PersonalNo> {
    @Select("SELECT id,wx_id,wx_name,qr_code,nickname,equipment_id,waiting_pass_num,friends_num,equipment_status,sales_group,personal_no_category AS personalNoCategoryName,head_portrait_url,create_time,super_id,remarks,deleted FROM personal_zc_01.personal_no WHERE wx_id = #{personalNoWxId} and deleted=0 limit 0,1")
    PersonalNo selectOneByWxId(String personalNoWxId);
    @Select("SELECT id,wx_id,wx_name,qr_code,nickname,equipment_id,waiting_pass_num,friends_num,equipment_status,sales_group,personal_no_category AS personalNoCategoryName,head_portrait_url,create_time,super_id,remarks,deleted FROM personal_zc_01.personal_no WHERE personal_no_category = #{category} and deleted=0 order by id desc")
    List<PersonalNo> listByCategory(String category);
    @Select("SELECT id,wx_id,wx_name,qr_code,nickname,equipment_id,waiting_pass_num,friends_num,equipment_status,sales_group,personal_no_category AS personalNoCategoryName,head_portrait_url,create_time,super_id,remarks,deleted FROM personal_zc_01.personal_no WHERE nickname like #{category} and deleted=0 order by id desc")
    List<PersonalNo> listLikeNickName(String nickName);
    @Select("SELECT wx_id FROM personal_zc_01.personal_no")
    List<String> listWxId();
    @Update("update personal_zc_01.personal_no set wx_id=#{param1},wx_name=#{param2},qr_code=#{param3},nickname=#{param4},head_portrait_url=#{param5},deleted=0 where id = #{param6}")
    void undateByLogicId(String wxId,String wxName,String qrCode,String nickName,String head,Integer id);
    @Insert("insert into personal_zc_01.personal_no (wx_id,wx_name,qr_code,nickname,waiting_pass_num,friends_num,equipment_status,personal_no_category,head_portrait_url,create_time,super_id,deleted) values (#{param1},#{param2},#{param3},#{param4},0,0,'线上','jiazhangjia',#{param5},#{param6},#{param7},0)")
    void insertInfo(String wxId, String wxName, String qrCode, String nickname, String headPortraitUrl, Date date, Integer superId);
    @Update("update personal_zc_01.personal_no set waiting_pass_num=#{param1},friends_num=#{param2} where id = #{param3}")
    void undateNumByLogicId(Integer watingNum, Integer friendsNum, Integer id);
}
