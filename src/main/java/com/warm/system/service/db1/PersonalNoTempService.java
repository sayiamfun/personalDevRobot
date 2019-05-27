package com.warm.system.service.db1;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoTemp;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 存储用户的交互数据 服务类
 * </p>
 *
 * @author liwenjie123
 * @since 2019-05-22
 */
public interface PersonalNoTempService extends IService<PersonalNoTemp> {

    List<PersonalNoTemp> listBysql(Sql sql);

    Integer add(PersonalNoTemp personalNoTemp);

    PersonalNoTemp getBySql(Sql sql);
}
