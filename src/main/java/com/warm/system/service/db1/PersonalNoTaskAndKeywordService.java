package com.warm.system.service.db1;

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

    PersonalNoTaskAndKeyword getByTaskIdAndKeywordName(Integer personalTaskId, String content);
}
