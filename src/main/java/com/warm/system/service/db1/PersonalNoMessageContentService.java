package com.warm.system.service.db1;

import com.warm.system.entity.PersonalNoMessageContent;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liwenjie123
 * @since 2019-05-06
 */
public interface PersonalNoMessageContentService extends IService<PersonalNoMessageContent> {

    List<PersonalNoMessageContent> listByMessageId(Integer messageId);
}
