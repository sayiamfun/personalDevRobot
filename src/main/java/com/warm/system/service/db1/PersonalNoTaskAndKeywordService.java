package com.warm.system.service.db1;

import com.warm.entity.Sql;
import com.warm.system.entity.PersonalNoTaskAndKeyword;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liwenjie123
 * @since 2019-04-28
 */
public interface PersonalNoTaskAndKeywordService extends IService<PersonalNoTaskAndKeyword> {

    PersonalNoTaskAndKeyword getBySql(Sql sql);
}
