package com.warm.system.mapper;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoOperationStockWechatAccount;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
public interface PersonalNoOperationStockWechatAccountMapper extends BaseMapper<PersonalNoOperationStockWechatAccount> {

    @Select("${sql}")
    PersonalNoOperationStockWechatAccount getBySql(Sql sql);

    Integer add(@Param("entity") PersonalNoOperationStockWechatAccount entity);

    Integer updateOne(@Param("entity") PersonalNoOperationStockWechatAccount entity);

    @Select("${sql}")
    List<PersonalNoOperationStockWechatAccount> listBySql(Sql sql);
}
