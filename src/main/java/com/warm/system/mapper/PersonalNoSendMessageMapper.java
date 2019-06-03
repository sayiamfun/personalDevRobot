package com.warm.system.mapper;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoSendMessage;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liwenjie123
 * @since 2019-05-06
 */
public interface PersonalNoSendMessageMapper extends BaseMapper<PersonalNoSendMessage> {

    @Select("${sql}")
    PersonalNoSendMessage getBySql(Sql sql);

    @Select("${sql}")
    List<PersonalNoSendMessage> listBySql(Sql sql);

    @Update("${sql}")
    void updateBySql(Sql sql);
}
