package com.warm.system.mapper;

import com.warm.system.entity.PersonalNoOperationStockWechatAccount;
import com.baomidou.mybatisplus.mapper.BaseMapper;
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
public interface PersonalNoOperationStockWechatAccountMapper extends BaseMapper<PersonalNoOperationStockWechatAccount> {

    @Select("SELECT id,asset_id,wx_buy_id,wx_id,wx_id_bie_ming,assign_phone,email,id_card,yin_hang_ka,nick_name,sex,province,country,city,area,whats_up,qr_code,avatar,is_have_realname,is_slient_download,operation_project_instance_id,wx_class,phone_stock_id,status,remark,password,wx_login_time,project_instance_reg_time,last_request_job_time,last_finished_job_time,last_connect_time,last_update_local_ip,last_connect_internet_ip,click_id,current_client_wehook_version,current_client_wechat_version FROM oa.operation_stock_wechat_account where id = #{param1} and operation_project_instance_id = #{param2} order by id desc limit 0,1")
    PersonalNoOperationStockWechatAccount getByLogicIdAndInstanceId(Integer currRobotLogicId, int ms_operation_project_instance_id);
    @Select("SELECT id,asset_id,wx_buy_id,wx_id,wx_id_bie_ming,assign_phone,email,id_card,yin_hang_ka,nick_name,sex,province,country,city,area,whats_up,qr_code,avatar,is_have_realname,is_slient_download,operation_project_instance_id,wx_class,phone_stock_id,status,remark,password,wx_login_time,project_instance_reg_time,last_request_job_time,last_finished_job_time,last_connect_time,last_update_local_ip,last_connect_internet_ip,click_id,current_client_wehook_version,current_client_wechat_version FROM oa.operation_stock_wechat_account where wx_id = #{param1} and operation_project_instance_id = #{param2} order by id desc limit 0,1")
    PersonalNoOperationStockWechatAccount getByWxIdAndInstanceId(String currRobotWxid, int ms_operation_project_instance_id);
    @Select("SELECT id,asset_id,wx_buy_id,wx_id,wx_id_bie_ming,assign_phone,email,id_card,yin_hang_ka,nick_name,sex,province,country,city,area,whats_up,qr_code,avatar,is_have_realname,is_slient_download,operation_project_instance_id,wx_class,phone_stock_id,status,remark,password,wx_login_time,project_instance_reg_time,last_request_job_time,last_finished_job_time,last_connect_time,last_update_local_ip,last_connect_internet_ip,click_id,current_client_wehook_version,current_client_wechat_version FROM oa.operation_stock_wechat_account where nick_name = #{param1} and operation_project_instance_id = #{param2} order by id desc")
    List<PersonalNoOperationStockWechatAccount> listByNickNameAndInstanceId(String nickname, int ms_operation_project_instance_id);
    @Select("SELECT id,asset_id,wx_buy_id,wx_id,wx_id_bie_ming,assign_phone,email,id_card,yin_hang_ka,nick_name,sex,province,country,city,area,whats_up,qr_code,avatar,is_have_realname,is_slient_download,operation_project_instance_id,wx_class,phone_stock_id,status,remark,password,wx_login_time,project_instance_reg_time,last_request_job_time,last_finished_job_time,last_connect_time,last_update_local_ip,last_connect_internet_ip,click_id,current_client_wehook_version,current_client_wechat_version FROM oa.operation_stock_wechat_account where operation_project_instance_id = #{param1} order by id desc")
    List<PersonalNoOperationStockWechatAccount> listByInstanceId(int ms_operation_project_instance_id);
    @Select("SELECT id,asset_id,wx_buy_id,wx_id,wx_id_bie_ming,assign_phone,email,id_card,yin_hang_ka,nick_name,sex,province,country,city,area,whats_up,qr_code,avatar,is_have_realname,is_slient_download,operation_project_instance_id,wx_class,phone_stock_id,status,remark,password,wx_login_time,project_instance_reg_time,last_request_job_time,last_finished_job_time,last_connect_time,last_update_local_ip,last_connect_internet_ip,click_id,current_client_wehook_version,current_client_wechat_version FROM oa.operation_stock_wechat_account where last_request_job_time < #{param1} and operation_project_instance_id = #{param2} order by id desc")
    List<PersonalNoOperationStockWechatAccount> listByRequestTaskTimeAndInstanceId(String toString, int ms_operation_project_instance_id);
    @Update("UPDATE oa.operation_stock_wechat_account  SET wx_id=#{param1},wx_id_bie_ming=#{param2},assign_phone=#{param3},nick_name=#{param4},area=#{param5},whats_up=#{param6},qr_code=#{param7},is_have_realname=#{param8},is_slient_download=#{param9},operation_project_instance_id=#{param10},wx_class=#{param11},`password`=#{param12},project_instance_reg_time=#{param13},last_request_job_time=#{param14},last_connect_time=#{param15},last_update_local_ip=#{param16},last_connect_internet_ip=#{param17},click_id=#{param18},current_client_wehook_version=#{param19},  current_client_wechat_version=#{param20}  WHERE id=#{param21}")
    boolean updateByLogicId(String wxId, String wxIdBieMing, String assignPhone, String nickName, String area, String whatsUp, String qrCode, Integer isHaveRealname, Integer isSlientDownload, Integer operationProjectInstanceId, String wxClass, String password, Date projectInstanceRegTime, Date lastRequestJobTime, Date lastConnectTime, String lastUpdateLocalIp, String lastConnectInternetIp, String clickId, String currentClientWehookVersion, String currentClientWechatVersion, Integer id);
    @Select("SELECT id,asset_id,wx_buy_id,wx_id,wx_id_bie_ming,assign_phone,email,id_card,yin_hang_ka,nick_name,sex,province,country,city,area,whats_up,qr_code,avatar,is_have_realname,is_slient_download,operation_project_instance_id,wx_class,phone_stock_id,status,remark,password,wx_login_time,project_instance_reg_time,last_request_job_time,last_finished_job_time,last_connect_time,last_update_local_ip,last_connect_internet_ip,click_id,current_client_wehook_version,current_client_wechat_version FROM oa.operation_stock_wechat_account where id = #{logicId}")
    PersonalNoOperationStockWechatAccount getByLogicId(Integer logicId);
}
