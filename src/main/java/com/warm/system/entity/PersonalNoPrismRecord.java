package com.warm.system.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
@Data
@TableName("personal_no_prism_record")
public class PersonalNoPrismRecord extends Model<PersonalNoPrismRecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("beta_type")
    private Integer betaType;
    @TableField("by_owner")
    private Integer byOwner;
    @TableField("by_qrcode")
    private Integer byQrcode;

    private String chatroom;

    private String content;
    @TableField("create_time")
    private Integer createTime;
    @TableField("ext_content")
    private String extContent;
    @TableField("from_nickname")
    private String fromNickname;
    @TableField("is_reporter_mentioned")
    private Boolean reporterMentioned;
    @TableField("is_robot_mentioned")
    private Boolean robotMentioned;
    @TableField("is_sent")
    private Boolean sent;
    @TableField("logic_id")
    private Integer logicId;

    private String md5;
    @TableField("package_id")
    private Integer packageId;
    @TableField("to_user_type")
    private Integer toUserType;
    @TableField("to_user_name_list")
    private String toUserNameList;

    private Integer type;

    private Integer unit;
    @TableField("we_chatmsg_type")
    private Integer weChatmsgType;

    private String whatever;
    @TableField("from_username")
    private String fromUsername;
    @TableField("report_logic_id")
    private Integer reportLogicId;
    @TableField("report_internet_ip")
    private String reportInternetIp;

    private Date reportTime;
    @TableField("get_logic_id")
    private Integer getLogicId;

    @ApiModelProperty(value = "获取该id的时候，请求客户端的ip")
    @TableField("get_internet_ip")
    private String getInternetIp;

    @ApiModelProperty(value = "获取该ip的时间")
    private Date getTime;

    @TableField(exist = false)
    private List<String> toUsernames;

    @TableField(exist = false)
    private String db;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "PersonalNoPrismRecord{" +
        "id=" + id +
        ", betaType=" + betaType +
        ", byOwner=" + byOwner +
        ", byQrcode=" + byQrcode +
        ", chatroom=" + chatroom +
        ", content=" + content +
        ", createTime=" + createTime +
        ", extContent=" + extContent +
        ", fromNickname=" + fromNickname +
        ", isReporterMentioned=" + reporterMentioned +
        ", isRobotMentioned=" + robotMentioned +
        ", isSent=" + sent +
        ", logicId=" + logicId +
        ", md5=" + md5 +
        ", packageId=" + packageId +
        ", toUserType=" + toUserType +
        ", toUserNameList=" + toUserNameList +
        ", type=" + type +
        ", unit=" + unit +
        ", weChatmsgType=" + weChatmsgType +
        ", whatever=" + whatever +
        ", fromUsername=" + fromUsername +
        ", reportLogicId=" + reportLogicId +
        ", reportInternetIp=" + reportInternetIp +
        ", reportTime=" + reportTime +
        ", getLogicId=" + getLogicId +
        ", getInternetIp=" + getInternetIp +
        ", getTime=" + getTime +
        "}";
    }
}
