package com.warm.system.service.impl;

import com.warm.entity.DB;
import com.warm.entity.Sql;
import com.warm.system.entity.*;
import com.warm.system.mapper.PersonalNoTaskMapper;
import com.warm.system.service.db1.*;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.utils.DaoGetSql;
import com.warm.utils.VerifyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
@Service
public class PersonalNoTaskServiceImpl extends ServiceImpl<PersonalNoTaskMapper, PersonalNoTask> implements PersonalNoTaskService {
    private static Log log = LogFactory.getLog(PersonalNoTaskServiceImpl.class);
    @Autowired
    private PersonalNoTaskMapper noTaskMapper;
    @Autowired
    private PersonalNoTaskReplyContentService noTaskReplyContentService;
    @Autowired
    private PersonalNoTaskBeginRemindService noTaskBeginRemindService;
    @Autowired
    private PersonalNoTaskLableService personalNoTaskLableService;


    private String DBTask = DB.DBAndTable(DB.PERSONAL_ZC_01,DB.personal_no_task);
    private String DBTaskReplay = DB.DBAndTable(DB.PERSONAL_ZC_01,DB.personal_no_task_reply_content);
    private String DBTaskRmind = DB.DBAndTable(DB.PERSONAL_ZC_01,DB.personal_no_task_begin_remind);
    private String DBTaskLable = DB.DBAndTable(DB.PERSONAL_ZC_01,DB.personal_no_task_lable);

    @Override
    public PersonalNoTask getTaskMessageById(Integer taskId) {
        log.info("数据库ID查询任务");
        String getSql = DaoGetSql.getById(DBTask,taskId);
        Sql sql = new Sql(getSql);
        PersonalNoTask noTaskById = noTaskMapper.getBySql(sql);
        if(!VerifyUtils.isEmpty(noTaskById)){
            log.info("根据任务id号查询自动回复列表");
            getSql = DaoGetSql.getSql("SELECT * from "+DBTaskReplay+" where personal_no_task_id = ? and deleted = 0",taskId);
            sql.setSql(getSql);
            List<PersonalNoTaskReplyContent> replyContentList = noTaskReplyContentService.listBySql(sql);
            noTaskById.setNoTaskReplyContentList(replyContentList);
            log.info("根据任务id号查询开课提醒列表");
            getSql = DaoGetSql.getSql("SELECT * from "+DBTaskRmind+" where personal_no_task_id = ? and deleted = 0",taskId);
            sql.setSql(getSql);
            List<PersonalNoTaskBeginRemind> beginRemindList = noTaskBeginRemindService.listBySql(sql);
            noTaskById.setNoTaskBeginRemindList(beginRemindList);
            log.info("根据任务id查询任务标签列表");
            getSql = DaoGetSql.getSql("SELECT * from "+DBTaskLable+" where personal_no_task_id = ? and deleted = 0",taskId);
            sql.setSql(getSql);
            List<PersonalNoTaskLable> personalNoTaskLables = personalNoTaskLableService.listBysql(sql);
            noTaskById.setNoLableList(personalNoTaskLables);
            log.info("数据库ID查询任务结束");
        }
        return noTaskById;
    }
//    将任务渠道列表转换为渠道名称列表
    private List<String > getChannelNameList(List<PersonalNoTaskChannel> noTaskChannelList){
        log.info("将任务渠道列表转换为渠道名称列表");
        List<String> channelNameList = new ArrayList<>();
        if(!VerifyUtils.collectionIsEmpty(noTaskChannelList)){
            for (PersonalNoTaskChannel personalNoTaskChannel : noTaskChannelList) {
                channelNameList.add(personalNoTaskChannel.getChannelName());
            }
        }
        return channelNameList;
    }

}
