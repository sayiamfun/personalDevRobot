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
@TableName("personal_no_message_content")
public class PersonalNoMessageContent extends Model<PersonalNoMessageContent> {

    private static final long serialVersionUID = 1L;

    /**
     * 消息内容表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 关键词id
     */
    @TableField("personal_no_keyword_id")
    private Integer personalNoKeywordId;
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

    public Integer getPersonalNoKeywordId() {
        return personalNoKeywordId;
    }

    public void setPersonalNoKeywordId(Integer personalNoKeywordId) {
        this.personalNoKeywordId = personalNoKeywordId;
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
        return "PersonalNoMessageContent{" +
        "id=" + id +
        ", personalNoKeywordId=" + personalNoKeywordId +
        ", contentType=" + contentType +
        ", content=" + content +
        ", deleted=" + deleted +
        "}";
    }
}
