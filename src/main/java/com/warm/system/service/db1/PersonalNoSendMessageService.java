package com.warm.system.service.db1;

import com.warm.system.entity.PersonalNoSendMessage;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liwenjie123
 * @since 2019-05-06
 */
public interface PersonalNoSendMessageService extends IService<PersonalNoSendMessage> {

    PersonalNoSendMessage getMessageIdByWxIdAndTiming(String s,int i);

    List<PersonalNoSendMessage> listByPersonalWxId(String s);

    PersonalNoSendMessage getById(Integer personalNoSendMessageId);
}
