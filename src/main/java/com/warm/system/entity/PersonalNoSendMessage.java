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
 * @since 2019-05-06
 */
@TableName("personal_no_send_message")
public class PersonalNoSendMessage extends Model<PersonalNoSendMessage> {

    private static final long serialVersionUID = 1L;

    /**
     * 个人号发送消息设置
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 个人号微信id
     */
    @TableField("personal_wx_id")
    private String personalWxId;
    /**
     * 个人号昵称
     */
    @TableField("nick_name")
    private String nickName;
    /**
     * 0:立即，1：隔时，2：定时
     */
    private Integer timing;
    /**
     * 格式时间或者是定时时间
     */
    @TableField("timing_time")
    private String timingTime;
    /**
     * 是否根据用户回复情况发送
     */
    @TableField("initiative_flag")
    private Integer initiativeFlag;
    /**
     * 消息id
     */
    @TableField("message_id")
    private Integer messageId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPersonalWxId() {
        return personalWxId;
    }

    public void setPersonalWxId(String personalWxId) {
        this.personalWxId = personalWxId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getTiming() {
        return timing;
    }

    public void setTiming(Integer timing) {
        this.timing = timing;
    }

    public String getTimingTime() {
        return timingTime;
    }

    public void setTimingTime(String timingTime) {
        this.timingTime = timingTime;
    }

    public Integer getInitiativeFlag() {
        return initiativeFlag;
    }

    public void setInitiativeFlag(Integer initiativeFlag) {
        this.initiativeFlag = initiativeFlag;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "PersonalNoSendMessage{" +
        "id=" + id +
        ", personalWxId=" + personalWxId +
        ", nickName=" + nickName +
        ", timing=" + timing +
        ", timingTime=" + timingTime +
        ", initiativeFlag=" + initiativeFlag +
        ", messageId=" + messageId +
        "}";
    }
}
