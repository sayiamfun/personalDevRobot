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
@TableName("personal_no_phone_task_group_finsh")
public class PersonalNoPhoneTaskGroupFinsh extends Model<PersonalNoPhoneTaskGroupFinsh> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 任务组主题
     */
    private String tname;
    /**
     * 任务总步数
     */
    @TableField("total_step")
    private Integer totalStep;
    /**
     * 下一步是第几步
     */
    @TableField("next_step")
    private Integer nextStep;
    /**
     * 当前的状态
     */
    private String status;
    /**
     * 要给他发送消息的wxid
     */
    @TableField("current_robot_id")
    private String currentRobotId;
    @TableField("group_pool_id")
    private Integer groupPoolId;
    @TableField("wx_group_id")
    private Integer wxGroupId;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 下发时间
     */
    @TableField("pickup_time")
    private Date pickupTime;
    /**
     * 结束时间
     */
    @TableField("finish_time")
    private Date finishTime;
    /**
     * 任务消息id
     */
    @TableField("task_send_id")
    private Integer taskSendId;
    /**
     * 标签消息id
     */
    @TableField("lable_send_id")
    private Integer lableSendId;
    /**
     * 任务优先级
     */
    @TableField("task_order")
    private Integer taskOrder;
    /**
     * 为1证明是任务消息或者是标签消息
     */
    @TableField("message_send_id")
    private Integer messageSendId;

    @TableField(exist = false)
    private String db;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "PersonalNoPhoneTaskGroupFinsh{" +
        "id=" + id +
        ", tname=" + tname +
        ", totalStep=" + totalStep +
        ", nextStep=" + nextStep +
        ", status=" + status +
        ", currentRobotId=" + currentRobotId +
        ", groupPoolId=" + groupPoolId +
        ", wxGroupId=" + wxGroupId +
        ", createTime=" + createTime +
        ", pickupTime=" + pickupTime +
        ", finishTime=" + finishTime +
        ", taskSendId=" + taskSendId +
        ", lableSendId=" + lableSendId +
        ", taskOrder=" + taskOrder +
        ", messageSendId=" + messageSendId +
        "}";
    }
}
