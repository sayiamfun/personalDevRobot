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
 * 
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
@Data
@TableName("personal_no_friends")
public class PersonalNoFriends extends Model<PersonalNoFriends> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 个人号Id
     */
    @TableField("personal_no_id")
    private Integer personalNoId;
    /**
     * 个人号微信id
     */
    @TableField("personal_no_wx_id")
    private String personalNoWxId;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 用户微信id
     */
    @TableField("user_wx_id")
    private String userWxId;
    /**
     * 加好友时间
     */
    @TableField("be_friend_time")
    private Date beFriendTime;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 逻辑删除
     */
    private Integer deleted;

    @TableField(exist = false)
    private String db;

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "PersonalNoFriends{" +
        "id=" + id +
        ", personalNoId=" + personalNoId +
        ", personalNoWxId=" + personalNoWxId +
        ", userId=" + userId +
        ", userWxId=" + userWxId +
        ", beFriendTime=" + beFriendTime +
        ", remarks=" + remarks +
        ", deleted=" + deleted +
        "}";
    }
}
