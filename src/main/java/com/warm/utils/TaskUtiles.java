package com.warm.utils;


import com.warm.entity.DB;
import com.warm.entity.Sql;
import com.warm.system.entity.*;
import com.warm.system.service.db1.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TaskUtiles {
    public static Log log = LogFactory.getLog(TaskUtiles.class);
    private static String DBTaskGroup = DB.DBAndTable(DB.PERSONAL_ZC_01,DB.personal_no_phone_task_group);
    private static String DBTask = DB.DBAndTable(DB.PERSONAL_ZC_01,DB.personal_no_phone_task);
    private static String DBRemidFlag = DB.DBAndTable(DB.PERSONAL_ZC_01,DB.personal_no_task_remind_flag);

    //下发任务
    public static Map<String, Object> getMap(PersonalNoPeopleService peopleService, PersonalNoPhoneTaskGroupService taskGroupService, PersonalNoTaskService noTaskService, PersonalNoPhoneTaskService taskService,
                                             PersonalNoMessageService messageService) {
        Map<String, Object> map = new HashMap<>();
        map.put("peopleService", peopleService);
        map.put("taskGroupService", taskGroupService);
        map.put("noTaskService", noTaskService);
        map.put("TaskService", taskService);
        map.put("messageService", messageService);
        return map;
    }

    //将回复消息转换为任务组
    public static void toTask(Map<String, Object> map, String personalWxId, String userWxId, Integer taskId, Integer time) {
        PersonalNoPhoneTaskGroupService taskGroupService = (PersonalNoPhoneTaskGroupService) map.get("taskGroupService");
        String getsql = "SELECT * FROM "+DBTaskGroup+" where tname LIKE '%"+personalWxId+"发送回复消息"+userWxId+"%' and create_time > '"+WebConst.getNowDate(new Date(new Date().getTime()-600000))+"' and status = '未下发' order by id desc limit 0,1";
        PersonalNoPhoneTaskGroup taskGroup = taskGroupService.getBySql(new Sql(getsql));
        if(VerifyUtils.isEmpty(taskGroup)) {
            insertTaskGroup(personalWxId, userWxId, map, taskId, time);
        }
    }

    private static void insertTaskGroup(String personalWxId, String userWxId, Map<String, Object> map, Integer taskId, Integer time) {
        PersonalNoPhoneTaskGroupService taskGroupService = (PersonalNoPhoneTaskGroupService) map.get("taskGroupService");
        PersonalNoTaskService noTaskService = (PersonalNoTaskService) map.get("noTaskService");
        PersonalNoPhoneTaskService taskService = (PersonalNoPhoneTaskService) map.get("TaskService");
        PersonalNoTask taskById = noTaskService.getTaskMessageById(taskId);
        if (!VerifyUtils.isEmpty(taskById)) {
            log.info("将回复信息转换为任务任务组");
            PersonalNoPhoneTaskGroup taskGroup = new PersonalNoPhoneTaskGroup();
            taskGroup.setNextStep(1);
            taskGroup.setTaskOrder(9);
            taskGroup.setStatus("未下发");
            taskGroup.setCreateTime(new Date(new Date().getTime() + time.longValue()));
            taskGroup.setTname(personalWxId + "发送回复消息" + userWxId);
            taskGroup.setCurrentRobotId(personalWxId);
            taskGroup.setTotalStep(taskById.getNoTaskReplyContentList().size());
            taskGroup.setTaskSendId(taskId);
            taskGroup.setDb(DBTaskGroup);
            boolean save = taskGroupService.add(taskGroup)>0;
            if (save) {
                log.info("开始添加任务");
                for (int j = 0; j < taskById.getNoTaskReplyContentList().size(); j++) {
                    PersonalNoPhoneTask task = new PersonalNoPhoneTask();
                    if ("邀请入群".equals(taskById.getNoTaskReplyContentList().get(j).getContentType())) {
                        task.setContent(taskById.getNoTaskReplyContentList().get(j).getContent());
                        task.setContentType(taskById.getNoTaskReplyContentList().get(j).getContentType());
                        task.setStep(j + 1);
                        task.setRobotId(userWxId);
                        task.setCreateTime(new Date());
                        task.setStatus("未下发");
                        task.setTaskType(1102);
                        task.setTname(personalWxId + "发送入群邀请给" + userWxId);
                        task.setTaskGroupId(taskGroup.getId());
                    } else {
                        task.setTname(personalWxId + "发送自动回复消息给" + userWxId);
                        task.setTaskGroupId(taskGroup.getId());
                        task.setContent(taskById.getNoTaskReplyContentList().get(j).getContent());
                        task.setContentType(taskById.getNoTaskReplyContentList().get(j).getContentType());
                        task.setStep(j + 1);
                        task.setStatus("未下发");
                        task.setTaskType(100);
                        task.setRobotId(userWxId);
                        task.setCreateTime(new Date());
                    }
                    task.setDb(DBTask);
                    boolean save1 = taskService.add(task)>0;
                    if (!save1) {
                        log.info("插入任务失败");
                        throw new RuntimeException("插入任务失败");
                    }
                }
            }
        }
    }
    //开课提醒任务
    public static void toRemindTask(Map<String, Object> map,PersonalNoTaskRemindFlagService remindFlagService, String personalNoWxId, String personalFriendWxId, Integer personalTaskId, int i) {
        PersonalNoTaskService noTaskService = (PersonalNoTaskService) map.get("noTaskService");
        PersonalNoTask taskById = noTaskService.getTaskMessageById(personalTaskId);
        log.info("判断是否需要下发");
        if (taskById == null || "0".equals(taskById.getAutoRemind())) {
            return;
        }
        String getSql = DaoGetSql.getSql("select * from "+DBRemidFlag+" where personal_no_wx_id = ? and user_wx_id = ? and personal_no_task_id = ? limit 0,1",personalNoWxId, personalFriendWxId, personalTaskId);
        Sql sql = new Sql(getSql);
        PersonalNoTaskRemindFlag remindFlag = remindFlagService.getBySql(sql);
        if(!VerifyUtils.isEmpty(remindFlag)) {
            return;
        }
        if (new Date().getTime() - taskById.getTaskStartTime().getTime() > 0) {
            return;
        }
        if (!VerifyUtils.collectionIsEmpty(taskById.getNoTaskBeginRemindList())) {
            PersonalNoPhoneTaskGroupService taskGroupService = (PersonalNoPhoneTaskGroupService) map.get("taskGroupService");
            PersonalNoPhoneTaskService taskService = (PersonalNoPhoneTaskService) map.get("TaskService");
            PersonalNoPhoneTaskGroup taskGroup = new PersonalNoPhoneTaskGroup();
            taskGroup.setCreateTime(new Date(taskById.getTaskStartTime().getTime() - Integer.parseInt(taskById.getAutoRemind()) * 60 * 1000));
            taskGroup.setNextStep(1);
            taskGroup.setTaskOrder(0);
            taskGroup.setStatus("未下发");
            taskGroup.setTname(personalNoWxId + "发送开课提醒消息" + personalFriendWxId);
            taskGroup.setCurrentRobotId(personalNoWxId);
            taskGroup.setTotalStep(taskById.getNoTaskBeginRemindList().size());
            taskGroup.setDb(DBTaskGroup);
            boolean save = taskGroupService.add(taskGroup)>0;
            if (save) {
                log.info("开始添加任务");
                for (int j = 0; j < taskById.getNoTaskBeginRemindList().size(); j++) {
                    PersonalNoPhoneTask task = new PersonalNoPhoneTask();
                    if ("邀请入群".equals(taskById.getNoTaskBeginRemindList().get(j).getContentType())) {
                        task.setContent(taskById.getNoTaskBeginRemindList().get(j).getContent());
                        task.setContentType(taskById.getNoTaskBeginRemindList().get(j).getContentType());
                        task.setCreateTime(new Date());
                        task.setRobotId(personalFriendWxId);
                        task.setStatus("未下发");
                        task.setStep(j + 1);
                        task.setTaskType(1102);
                        task.setTname(personalNoWxId + "发送入群邀请给" + personalFriendWxId);
                        task.setTaskGroupId(taskGroup.getId());
                    } else {
                        task.setTname(personalNoWxId + "发送消息给" + personalFriendWxId);
                        task.setTaskGroupId(taskGroup.getId());
                        task.setContent(taskById.getNoTaskBeginRemindList().get(j).getContent());
                        task.setContentType(taskById.getNoTaskBeginRemindList().get(j).getContentType());
                        task.setStep(j + 1);
                        task.setTaskType(100);
                        task.setRobotId(personalFriendWxId);
                        task.setCreateTime(new Date());
                        task.setStatus("未下发");
                    }
                    task.setDb(DBTask);
                    boolean save1 = taskService.add(task)>0;
                    if (!save1) {
                        log.info("插入任务失败");
                        throw new RuntimeException("插入任务失败");
                    }
                }
            }
            remindFlag = new PersonalNoTaskRemindFlag();
            remindFlag.setPersonalNoWxId(personalNoWxId);
            remindFlag.setUserWxId(personalFriendWxId);
            remindFlag.setPersonalNoTaskId(personalTaskId);
            remindFlag.setDb(DBRemidFlag);
            remindFlagService.add(remindFlag);
        }
    }
    public static void toMessageTask(Map<String, Object> map, String s, String fromUsername, Integer messageId, int i) {
        if(VerifyUtils.isEmpty(messageId)){
            return;
        }
        PersonalNoPhoneTaskGroupService taskGroupService = (PersonalNoPhoneTaskGroupService) map.get("taskGroupService");
        PersonalNoMessageService messageService = (PersonalNoMessageService) map.get("messageService");
        PersonalNoPhoneTaskService taskService = (PersonalNoPhoneTaskService) map.get("TaskService");
        PersonalNoMessage message = messageService.getById(messageId);
        if (VerifyUtils.isEmpty(message) || message.getDeleted() == 1) {
            return;
        }
        log.info("将个人号对应消息转换为任务任务组");
        PersonalNoPhoneTaskGroup taskGroup = new PersonalNoPhoneTaskGroup();
        taskGroup.setNextStep(1);
        taskGroup.setCreateTime(new Date(new Date().getTime() + i));
        taskGroup.setTaskOrder(9);
        taskGroup.setStatus("未下发");
        taskGroup.setTname(s + "发送个人号消息" + fromUsername);
        taskGroup.setCurrentRobotId(s);
        taskGroup.setTotalStep(message.getMessageContentList().size());
        taskGroup.setDb(DBTaskGroup);
        boolean save = taskGroupService.add(taskGroup)>0;
        if (save) {
            log.info("开始添加消息任务");
            for (int j = 0; j < message.getMessageContentList().size(); j++) {
                PersonalNoPhoneTask task = new PersonalNoPhoneTask();
                if ("邀请入群".equals(message.getMessageContentList().get(j).getContentType())) {
                    task.setContent(message.getMessageContentList().get(j).getContent());
                    task.setContentType(message.getMessageContentList().get(j).getContentType());
                    task.setCreateTime(new Date());
                    task.setStatus("未下发");
                    task.setStep(j + 1);
                    task.setRobotId(fromUsername);
                    task.setTaskType(1102);
                    task.setTname(s + "发送入群邀请给" + fromUsername);
                    task.setTaskGroupId(taskGroup.getId());
                } else {
                    task.setTname(s + "发送个人号消息" + fromUsername);
                    task.setTaskGroupId(taskGroup.getId());
                    task.setContent(message.getMessageContentList().get(j).getContent());
                    task.setContentType(message.getMessageContentList().get(j).getContentType());
                    task.setStep(j + 1);
                    task.setTaskType(100);
                    task.setStatus("未下发");
                    task.setRobotId(fromUsername);
                    task.setCreateTime(new Date());
                }
                task.setDb(DBTask);
                boolean save1 = taskService.add(task)>0;
                if (!save1) {
                    log.info("插入任务失败");
                    throw new RuntimeException("插入任务失败");
                }
            }
        }
    }
}
