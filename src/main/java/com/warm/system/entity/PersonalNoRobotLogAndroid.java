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
 * 机器人上报的日志
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
@Data
@TableName("personal_no_robot_log_android")
public class PersonalNoRobotLogAndroid extends Model<PersonalNoRobotLogAndroid> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * logic_id
     */
    @TableField("operation_stock_wechat_account_id")
    private Integer operationStockWechatAccountId;
    @TableField("wx_id")
    private String wxId;
    private String content;
    @TableField("add_time")
    private Date addTime;
    @TableField("internet_ip")
    private String internetIp;

    @TableField(exist = false)
    private String db;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "PersonalNoRobotLogAndroid{" +
        "id=" + id +
        ", operationStockWechatAccountId=" + operationStockWechatAccountId +
        ", wxId=" + wxId +
        ", content=" + content +
        ", addTime=" + addTime +
        ", internetIp=" + internetIp +
        "}";
    }
}
