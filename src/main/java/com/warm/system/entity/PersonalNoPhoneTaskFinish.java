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
 * @author liwenjie123
 * @since 2019-05-22
 */
@Data
@TableName("personal_no_phone_task_finish")
public class PersonalNoPhoneTaskFinish extends Model<PersonalNoPhoneTaskFinish> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 任务组id
     */
    @TableField("task_group_id")
    private Integer taskGroupId;
    /**
     * 任务信息主图
     */
    private String tname;
    /**
     * 当前步数
     */
    private Integer step;
    /**
     * 好友wxid
     */
    @TableField("robot_id")
    private String robotId;
    /**
     * 状态
     */
    private String status;
    /**
     * 任务描述
     */
    private String tdescription;
    /**
     * 任务用到的json字符串
     */
    @TableField("task_json")
    private String taskJson;
    /**
     * 群池id
     */
    @TableField("group_pool_id")
    private Integer groupPoolId;
    /**
     * 群id
     */
    @TableField("wx_group_id")
    private Integer wxGroupId;
    /**
     *  任务结束标识
     */
    @TableField("task_finished_tag")
    private String taskFinishedTag;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 下发任务时间
     */
    @TableField("pickup_time")
    private Date pickupTime;
    /**
     * 任务请求时间
     */
    @TableField("is_client_feedback_received")
    private Integer isClientFeedbackReceived;
    @TableField("feedback_received_time")
    private Date feedbackReceivedTime;
    @TableField("is_client_feedback_finished")
    private Integer isClientFeedbackFinished;
    @TableField("feedback_finished_time")
    private Date feedbackFinishedTime;
    @TableField("failed_reason")
    private String failedReason;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 消息内容类型
     */
    @TableField("content_type")
    private String contentType;
    /**
     * 任务类型
     */
    @TableField("task_type")
    private Integer taskType;

    @TableField(exist = false)
    private String db;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "PersonalNoPhoneTaskFinish{" +
        "id=" + id +
        ", taskGroupId=" + taskGroupId +
        ", tname=" + tname +
        ", step=" + step +
        ", robotId=" + robotId +
        ", status=" + status +
        ", tdescription=" + tdescription +
        ", taskJson=" + taskJson +
        ", groupPoolId=" + groupPoolId +
        ", wxGroupId=" + wxGroupId +
        ", taskFinishedTag=" + taskFinishedTag +
        ", createTime=" + createTime +
        ", pickupTime=" + pickupTime +
        ", isClientFeedbackReceived=" + isClientFeedbackReceived +
        ", feedbackReceivedTime=" + feedbackReceivedTime +
        ", isClientFeedbackFinished=" + isClientFeedbackFinished +
        ", feedbackFinishedTime=" + feedbackFinishedTime +
        ", failedReason=" + failedReason +
        ", content=" + content +
        ", contentType=" + contentType +
        ", taskType=" + taskType +
        "}";
    }
}
