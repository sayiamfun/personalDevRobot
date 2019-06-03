package com.warm.system.service.db1;

import com.baomidou.mybatisplus.service.IService;
import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoCategoryAndGroup;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dgd123
 * @since 2019-05-16
 */
public interface PersonalNoCategoryAndGroupService extends IService<PersonalNoCategoryAndGroup> {

    Integer add(PersonalNoCategoryAndGroup personalNoCategoryAndGroup);

    PersonalNoCategoryAndGroup getBySql(Sql sql);

    List<String> listStringBySql(Sql sql);

    void updateBySql(Sql sql);
}
