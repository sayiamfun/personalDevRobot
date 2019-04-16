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
@TableName("personal_no_lable_category")
public class PersonalNoLableCategory extends Model<PersonalNoLableCategory> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 标签类别名称
     */
    @TableField("category_name")
    private String categoryName;
    /**
     * 超级用户id
     */
    @TableField("super_id")
    private Integer superId;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 逻辑删除
     */
    private Integer deleted;

    @TableField(exist = false)
    private Integer lableNum;

    @TableField(exist = false)
    private Integer personalNum;

    @TableField(exist = false)
    private Integer userNum;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "PersonalNoLableCategory{" +
        "id=" + id +
        ", categoryName=" + categoryName +
        ", superId=" + superId +
        ", createTime=" + createTime +
        ", remarks=" + remarks +
        ", deleted=" + deleted +
        "}";
    }
}
