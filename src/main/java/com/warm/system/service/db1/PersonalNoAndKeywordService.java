package com.warm.system.service.db1;

import com.warm.system.entity.PersonalNoAndKeyword;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dgd123
 * @since 2019-04-17
 */
public interface PersonalNoAndKeywordService extends IService<PersonalNoAndKeyword> {

    PersonalNoAndKeyword listByWxIdAndKeyword(String s, String content);
}
