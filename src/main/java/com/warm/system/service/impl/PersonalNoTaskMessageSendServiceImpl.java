package com.warm.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.warm.entity.robot.common.SunTaskType;
import com.warm.system.entity.*;
import com.warm.system.mapper.PersonalNoTaskMessageSendMapper;
import com.warm.system.service.db1.*;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.utils.VerifyUtils;
import com.warm.utils.WebConst;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class PersonalNoTaskMessageSendServiceImpl extends ServiceImpl<PersonalNoTaskMessageSendMapper, PersonalNoTaskMessageSend> implements PersonalNoTaskMessageSendService {
    private static Log log = LogFactory.getLog(PersonalNoTaskMessageSendServiceImpl.class);
    @Autowired
    private PersonalNoTaskMessageSendMapper messageSendMapper;
    @Autowired
    private PersonalNoTaskMessageSendContentService personalNoTaskMessageSendContentService;
    @Autowired
    private PersonalNoTaskPersonalService taskPersonalService;
    @Autowired
    private PersonalNoPeopleService taskPeopleService;
    @Autowired
    private PersonalNoPhoneTaskGroupService taskGroupService;
    @Autowired
    private PersonalNoPhoneTaskService taskService;
    /*
     * 插入个人号任务消息
     */
    @Transactional
    @Override
    public boolean insertPersonalNoTaskMessage(PersonalNoTaskMessageSend personalNoTaskMessageSend , PersonalNoTask noTask) {
        Date sendTime = personalNoTaskMessageSend.getSendTime();
        log.info("数据库开始添加任务消息");
        log.info("将任务信息   添加到   任务消息中");
        personalNoTaskMessageSend.setPersonalNoTaskMessageName(noTask.getTaskName());
        personalNoTaskMessageSend.setPersonaNolTaskMessageStatus("0");
        List<PersonalNoTaskMessageSendContent> personalNoTaskMessageSendContentList = personalNoTaskMessageSend.getPersonalNoTaskMessageSendContentList();
        //做判断，生成格式：2（1条文字，1条邀请入群）
        log.info("开始生成任务消息内容预览");
        String contentShow = WebConst.getTaskContentShow(personalNoTaskMessageSendContentList);
        log.info("生成任务消息内容预览成功");
        personalNoTaskMessageSend.setPersonalNoTaskMessageContentShow(contentShow);
        int insert = 0;
        if(VerifyUtils.isEmpty(personalNoTaskMessageSend.getId())){
            log.info("数据库添加任务消息");
            if(VerifyUtils.isEmpty(sendTime)){
                personalNoTaskMessageSend.setSendTime(new Date());
            }
            insert = baseMapper.insert(personalNoTaskMessageSend);
        }else {
            log.info("数据库修改任务消息");
            insert = baseMapper.updateById(personalNoTaskMessageSend);
            log.info("修改任务消息，将任务表内相关的任务组和任务删除");
            log.info("根据任务消息id查询所有的任务组");
            List<PersonalNoPhoneTaskGroup> taskGroupList = taskGroupService.listByTaskMessageId(personalNoTaskMessageSend.getId());
            for (PersonalNoPhoneTaskGroup taskGroup : taskGroupList) {
                log.info("根据任务组id删除所有的子任务");
                taskService.deleteByTaskGrouPId(taskGroup.getId());
                taskGroupService.deleteById(taskGroup.getId());
            }
        }
        if(insert != 1){
            log.info("数据库添加任务消息失败");
            return false;
        }
        boolean b = personalNoTaskMessageSendContentService.batchSave(personalNoTaskMessageSend);
        log.info("数据库添加任务消息内容到数据库成功");
        log.info("数据库开始将任务消息转换为  手机将要下发的任务组和任务");
        log.info("任务相关个人号");
        List<PersonalNoTaskPersonal> personalNoTaskPersonals = taskPersonalService.listByTaskId(noTask.getId());
        log.info("任务和个人号下的粉丝微信id");
        Set<String> userWxIdSet = new HashSet<>();
        //根据任务id和个人号微信id查询好友微信id列表
        for (PersonalNoTaskPersonal personalNoTaskPersonal : personalNoTaskPersonals) {
            List<String> strings = taskPeopleService.listUserWxIdByTaskIdAndPersonalWxId(noTask.getId(), personalNoTaskPersonal.getPersonalNoWxId(), personalNoTaskMessageSend.getStartTime(), personalNoTaskMessageSend.getEndTime());
            userWxIdSet.addAll(strings);
        }
        log.info("开始转换任务组和任务");
        for (PersonalNoTaskPersonal personalNoTaskPersonal : personalNoTaskPersonals) {
            for (String s : userWxIdSet) {
                PersonalNoPhoneTaskGroup taskGroup = new PersonalNoPhoneTaskGroup();
                taskGroup.setTname(personalNoTaskPersonal.getPersonalNoWxId() + "发送任务消息组给" + s);
                taskGroup.setCurrentRobotId(personalNoTaskPersonal.getPersonalNoWxId());
                taskGroup.setTotalStep(personalNoTaskMessageSendContentList.size());
                taskGroup.setNextStep(1);
                taskGroup.setStatus("未下发");
                if(!VerifyUtils.isEmpty(sendTime)){
                    log.info("定时任务");
                    taskGroup.setCreateTime(personalNoTaskMessageSend.getSendTime());
                    taskGroup.setTaskOrder(1);
                }else {
                    log.info("即时任务");
                    taskGroup.setTaskOrder(0);
                }
                taskGroup.setTaskSendId(personalNoTaskMessageSend.getId());
                boolean save = taskGroupService.insert(taskGroup);
                if(!save){
                    throw new RuntimeException("插入" + taskGroup.getTname() + "组失败");
                }
                for (int i=0; i<personalNoTaskMessageSendContentList.size();i++) {
                    PersonalNoPhoneTask task = new PersonalNoPhoneTask();
                    task.setTaskGroupId(taskGroup.getId());
                    task.setStatus("未下发");
                    task.setCreateTime(new Date());
                    task.setRobotId(s);
                    task.setTname(personalNoTaskPersonal.getPersonalNoWxId() + "发送第"+(i+1)+"条标签消息组给" + s);
                    task.setStep(i+1);
                    task.setContentType(personalNoTaskMessageSendContentList.get(i).getContentType());
                    task.setContent(personalNoTaskMessageSendContentList.get(i).getContent());
                    //发送消息
                    task.setTaskType(SunTaskType.FRIEND_SEND_MSG);
                    boolean save1 = taskService.insert(task);
                    if(!save1){
                        throw new RuntimeException("插入" + personalNoTaskPersonal.getPersonalNoWxId() + "发送第"+(i+1)+"条标签消息组给" + s + "失败");
                    }
                }
            }
        }
        log.info("插入消息内容成功");
        return true;
    }
    /*
     * 分页查询任务消息
     */
    @Override
    public void pageQuery(Page<PersonalNoTaskMessageSend> page, Object o) {
        log.info("数据库分页查找任务消息开始");
        EntityWrapper<PersonalNoTaskMessageSend> entityWrapper = new EntityWrapper<>();
        entityWrapper.orderDesc(Arrays.asList(new String[]{"id"}));
        baseMapper.selectPage(page, entityWrapper);
        if(!VerifyUtils.collectionIsEmpty(page.getRecords())){
            log.info("查询每条任务消息的消息内容开始");
            for (PersonalNoTaskMessageSend record : page.getRecords()) {
                List<PersonalNoTaskMessageSendContent> taskMessageSendContentList =  personalNoTaskMessageSendContentService.listByTaskMessageContentId(record.getId());
                record.setPersonalNoTaskMessageSendContentList(taskMessageSendContentList);
                log.info("处理任务状态");
                Integer num = Integer.parseInt(record.getPersonaNolTaskMessageStatus())/taskMessageSendContentList.size();
                log.info("查找任务相关任务");
                Integer peopleNum = taskPeopleService.getPeopleCountByTaskId(record.getPersonaNoTaskId());
                record.setPersonaNolTaskMessageStatus(num.toString() + "/" + peopleNum.toString());
            }
            log.info("查询每条任务消息的消息内容结束");
        }
        log.info("数据库分页查找任务消息结束");
    }

    /**
     * 根据id查询任务消息
     * @param id
     * @return
     */
    @Override
    public PersonalNoTaskMessageSend getTaskMessageById(Long id) {
        log.info("数据库根据id查询任务消息开始");
        PersonalNoTaskMessageSend personalNoTaskMessageSend = baseMapper.selectById(id);
        if(!VerifyUtils.isEmpty(personalNoTaskMessageSend)){
            log.info("根据任务消息id查询任务消息内容");
            List<PersonalNoTaskMessageSendContent> taskMessageSendContentList = personalNoTaskMessageSendContentService.listByTaskMessageContentId(personalNoTaskMessageSend.getId());
            personalNoTaskMessageSend.setPersonalNoTaskMessageSendContentList(taskMessageSendContentList);
        }
        log.info("数据库根据id查询任务消息结束");
        return personalNoTaskMessageSend;
    }
}
