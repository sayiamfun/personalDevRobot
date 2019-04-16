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
 * @author dgd123
 * @since 2019-03-29
 */
@TableName("personal_no_lable_message_send_content")
public class PersonalNoLableMessageSendContent extends Model<PersonalNoLableMessageSendContent> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 标签消息id
     */
    @TableField("personal_no_lable_message_send_id")
    private Integer personalNoLableMessageSendId;
    /**
     * 内容类型
     */
    @TableField("content_type")
    private String contentType;
    /**
     * 内容
     */
    private String content;
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

    public Integer getPersonalNoLableMessageSendId() {
        return personalNoLableMessageSendId;
    }

    public void setPersonalNoLableMessageSendId(Integer personalNoLableMessageSendId) {
        this.personalNoLableMessageSendId = personalNoLableMessageSendId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        return "PersonalNoLableMessageSendContent{" +
        "id=" + id +
        ", personalNoLableMessageSendId=" + personalNoLableMessageSendId +
        ", contentType=" + contentType +
        ", content=" + content +
        ", deleted=" + deleted +
        "}";
    }
}
