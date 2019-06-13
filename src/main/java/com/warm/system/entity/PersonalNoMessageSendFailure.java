package com.warm.system.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author liwenjie123
 * @since 2019-06-07
 */
@Data
@TableName("personal_no_message_send_failure")
public class PersonalNoMessageSendFailure extends Model<PersonalNoMessageSendFailure> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 个人号微信id
     */
    @TableField("personal_wx_id")
    private String personalWxId;
    /**
     * 用户微信id
     */
    @TableField("user_wx_id")
    private String userWxId;
    /**
     * 发送失败次数
     */
    @TableField("failure_num")
    private Integer failureNum;
    /**
     * 逻辑删除状态
     */
    private Integer deleted;

    @TableField(exist = false)
    private String db;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "PersonalNoMessageSendFailure{" +
        "id=" + id +
        ", personalWxId=" + personalWxId +
        ", userWxId=" + userWxId +
        ", failureNum=" + failureNum +
        ", deleted=" + deleted +
        "}";
    }
}
