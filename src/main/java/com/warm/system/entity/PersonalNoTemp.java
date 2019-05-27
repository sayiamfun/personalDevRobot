package com.warm.system.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 存储用户的交互数据
 * </p>
 *
 * @author liwenjie123
 * @since 2019-05-22
 */
@Data
@TableName("personal_no_temp")
public class PersonalNoTemp extends Model<PersonalNoTemp> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "Id", type = IdType.AUTO)
    private Integer Id;
    /**
     * 个人号微信id
     */
    @TableField("personal_no_wx_id")
    private String personalNoWxId;
    /**
     * 用户微信id
     */
    @TableField("user_wx_id")
    private String userWxId;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 回复状态
     */
    private Integer greetings;
    /**
     * 定时消息：0：未发，1：已发
     */
    private Integer timing;
    /**
     * 定时时间
     */
    @TableField("timing_time")
    private Date timingTime;
    /**
     * 隔时消息：0：未发，1：已发
     */
    private Integer ever;
    /**
     * 隔时时间
     */
    @TableField("ever_time")
    private String everTime;
    /**
     * 个人号发送消息表id
     */
    @TableField("personal_no_send_message_id")
    private Integer personalNoSendMessageId;

    @TableField(exist = false)
    private String db;

    @Override
    protected Serializable pkVal() {
        return this.Id;
    }

    @Override
    public String toString() {
        return "PersonalNoTemp{" +
        "Id=" + Id +
        ", personalNoWxId=" + personalNoWxId +
        ", userWxId=" + userWxId +
        ", createTime=" + createTime +
        ", greetings=" + greetings +
        ", timing=" + timing +
        ", timingTime=" + timingTime +
        ", ever=" + ever +
        ", everTime=" + everTime +
        ", personalNoSendMessageId=" + personalNoSendMessageId +
        "}";
    }
}
