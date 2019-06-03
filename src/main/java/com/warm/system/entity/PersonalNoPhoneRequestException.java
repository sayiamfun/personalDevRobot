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
 * @since 2019-05-29
 */
@Data
@TableName("personal_no_phone_request_exception")
public class PersonalNoPhoneRequestException extends Model<PersonalNoPhoneRequestException> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 方法类型
     */
    private String method;
    /**
     * 接口地址
     */
    private String url;
    /**
     * 请求参数
     */
    @TableField("request_body")
    private String requestBody;
    /**
     * 返回参数
     */
    @TableField("response_body")
    private String responseBody;
    /**
     * 返回的code值
     */
    @TableField("status_code")
    private Integer statusCode;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    @TableField(exist = false)
    private String db;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "PersonalNoPhoneRequestException{" +
        "id=" + id +
        ", method=" + method +
        ", url=" + url +
        ", requestBody=" + requestBody +
        ", responseBody=" + responseBody +
        ", statusCode=" + statusCode +
        ", createTime=" + createTime +
        "}";
    }
}
