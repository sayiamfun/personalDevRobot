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
 * @since 2019-04-28
 */
@TableName("personal_no_task_and_keyword")
public class PersonalNoTaskAndKeyword extends Model<PersonalNoTaskAndKeyword> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("task_id")
    private Integer taskId;
    @TableField("task_name")
    private String taskName;
    @TableField("keyword_id")
    private Integer keywordId;
    @TableField("keyword_name")
    private String keywordName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(Integer keywordId) {
        this.keywordId = keywordId;
    }

    public String getKeywordName() {
        return keywordName;
    }

    public void setKeywordName(String keywordName) {
        this.keywordName = keywordName;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "PersonalNoTaskAndKeyword{" +
        "id=" + id +
        ", taskId=" + taskId +
        ", taskName=" + taskName +
        ", keywordId=" + keywordId +
        ", keywordName=" + keywordName +
        "}";
    }
}
