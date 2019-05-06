package com.warm.system.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author liwenjie123
 * @since 2019-05-05
 */
@TableName("personal_no_timing_no")
public class PersonalNoTimingNo extends Model<PersonalNoTimingNo> {

    private static final long serialVersionUID = 1L;

    /**
     * 定时消息个人号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 个人号微信id
     */
    @TableField("wx_id")
    private String wxId;
    /**
     * 个人号昵称
     */
    @TableField("nick_name")
    private String nickName;
    /**
     * 逻辑删除
     */
    private Integer deleted;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "PersonalNoTimingNo{" +
        "id=" + id +
        ", wxId=" + wxId +
        ", nickName=" + nickName +
        ", deleted=" + deleted +
        "}";
    }
}
