package com.warm.system.controller;

import com.aliyun.oss.common.auth.ServiceSignature;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.warm.entity.DB;
import com.warm.entity.R;
import com.warm.entity.Sql;
import com.warm.entity.TuLingEntity.TuLingResult;
import com.warm.entity.robot.G;
import com.warm.entity.robot.common.SunTaskType;
import com.warm.entity.robot.common.WeChatMsgType;
import com.warm.entity.robot.requestInfo.*;
import com.warm.entity.robot.responseInfo.*;
import com.warm.system.entity.*;
import com.warm.system.service.db1.*;
import com.warm.system.service.db2.PersonalNoOperationStockWechatAccountService;
import com.warm.system.service.db3.PersonalNoWxGroupService;
import com.warm.utils.*;
import com.warm.utils.tulingUtil.JSONUtils;
import com.warm.utils.tulingUtil.TuLingParam;
import io.swagger.annotations.Api;
import net.bytebuddy.asm.Advice;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.stream.Stream;

@CrossOrigin //跨域
@Api(description = "手机微信端个人号的管理")
@RestController
@RequestMapping("/robot")
public class RobotController {

    private static boolean toGroup = false;
    private SunTask toGroupSunTask = new SunTask();
    private static Date taskDate = new Date();
    private static Date theDate = new Date();
    public static Log log = LogFactory.getLog(RobotController.class);

    @Autowired
    private PersonalNoTaskRemindFlagService remindFlagService;
    @Autowired
    private PersonalNoSmallFaceService personalNoSmallFaceService;
    @Autowired
    private PersonalNoPrismRecordService prismrecordService;
    @Autowired
    private PersonalNoOperationStockWechatAccountService wechatAccountService;
    @Autowired
    private PersonalNoPhoneTaskGroupService taskGroupService;
    @Autowired
    private PersonalNoPhoneTaskService taskService;
    @Autowired
    private PersonalNoRobotLogAndroidService logAndroidService;
    @Autowired
    private PersonalNoWxGroupService wxGroupService;
    @Autowired
    private PersonalNoTaskService noTaskService;
    @Autowired
    private PersonalNoPeopleService taskPeopleService;
    @Autowired
    private PersonalNoUserService userService;
    @Autowired
    private PersonalNoFriendsService friendsService;
    @Autowired
    private PersonalNoKeywordContentService keywordContentServicec;
    @Autowired
    private PersonalNoRequestExceptionService requestExceptionService;
    @Autowired
    private PersonalNoBlacklistService blacklistService;
    @Autowired
    private PersonalNoValueTableService valueTableService;
    @Autowired
    private PersonalNoPhoneRequestTaskTimeService phoneRequestTaskTimeService;
    @Autowired
    private PersonalNoTempService tempService;
    @Autowired
    private PersonalNoAndKeywordService personalNoAndKeywordService;
    @Autowired
    private PersonalNoTaskAndKeywordService taskAndKeywordService;
    @Autowired
    private PersonalNoSendMessageService sendMessageService;
    @Autowired
    private PersonalNoMessageService messageService;
    @Autowired
    private PassageVisitorRecordService passageVisitorRecordService;
    @Autowired
    private PersonalNoPhoneTaskGroupFinshService taskGroupFinshService;
    @Autowired
    private PersonalNoPhoneTaskFinishService taskFinishService;
    @Autowired
    private PersonalNoTaskLableService taskLableService;
    @Autowired
    private PersonalNoPhoneRequestExceptionService phoneRequestExceptionService;
    @Autowired
    private PersonalNoCategoryAndGroupService noCategoryAndGroupService;
    @Autowired
    private PersonalNoFriendsCirclePersonalService friendsCirclePersonalService;
    @Autowired
    private PersonalNoTaskPersonalService taskPersonalService;
    @Autowired
    private PersonalNoMessageSendFailureService sendFailureService;

    private String DBWeChat = DB.DBAndTable(DB.OA, DB.operation_stock_wechat_account);
    private String DBRequestException = DB.DBAndTable(DB.PERSONAL_ZC_01, DB.personal_no_request_exception);
    private String DBPhoneRequestException = DB.DBAndTable(DB.PERSONAL_ZC_01, DB.personal_no_phone_request_exception);
    private String DBPeople = DB.DBAndTable(DB.PERSONAL_ZC_01, DB.personal_no_people);
    private String DBFriends = DB.DBAndTable(DB.PERSONAL_ZC_01, DB.personal_no_friends);
    private String DBValueTable = DB.DBAndTable(DB.PERSONAL_ZC_01, DB.personal_no_value_table);
    private String DBBlack = DB.DBAndTable(DB.PERSONAL_ZC_01, DB.personal_no_blacklist);
    private String DBTaskGroup = DB.DBAndTable(DB.PERSONAL_ZC_01, DB.personal_no_phone_task_group);
    private String DBTaskGroupFinish = DB.DBAndTable(DB.PERSONAL_ZC_01, DB.personal_no_phone_task_group_finish);
    private String DBTask = DB.DBAndTable(DB.PERSONAL_ZC_01, DB.personal_no_phone_task);
    private String DBTaskFinish = DB.DBAndTable(DB.PERSONAL_ZC_01, DB.personal_no_phone_task_finish);
    private String DBUser = DB.DBAndTable(DB.PERSONAL_ZC_01, DB.personal_no_user);
    private String DBTemp = DB.DBAndTable(DB.PERSONAL_ZC_01, DB.personal_no_temp);
    private String DBTaskAndKeyword = DB.DBAndTable(DB.PERSONAL_ZC_01, DB.personal_no_task_and_keyword);
    private String DBNoAndKeyword = DB.DBAndTable(DB.PERSONAL_ZC_01, DB.personal_no_and_keyword);
    private String DBKeywordContent = DB.DBAndTable(DB.PERSONAL_ZC_01, DB.personal_no_keyword_content);
    private String DBRobotLog = DB.DBAndTable(DB.PERSONAL_ZC_01, DB.personal_no_robot_log_android);
    private String DBPhoneRequestTime = DB.DBAndTable(DB.PERSONAL_ZC_01, DB.personal_no_phone_request_task_time);
    private String DBSmallFace = DB.DBAndTable(DB.PERSONAL_ZC_01, DB.personal_no_small_face);
    private String DBTaskLable = DB.DBAndTable(DB.PERSONAL_ZC_01, DB.personal_no_task_lable);
    private String DBRobotPrismrecord = DB.DBAndTable(DB.PERSONAL_ZC_01, DB.personal_no_prism_record);
    private String DBSendMessage = DB.DBAndTable(DB.PERSONAL_ZC_01, DB.personal_no_send_message);
    private String DBNoAndGroupCategory = DB.DBAndTable(DB.PERSONAL_ZC_01, DB.personal_no_category_and_group);
    private String DBFriendsCirclePersonal = DB.DBAndTable(DB.PERSONAL_ZC_01, DB.personal_no_friends_circle_personal);
    private String DBTaskPersonal = DB.DBAndTable(DB.PERSONAL_ZC_01, DB.personal_no_task_personal);
    private String DBMessageSendFail = DB.DBAndTable(DB.PERSONAL_ZC_01, DB.personal_no_message_send_failure);


    @GetMapping("getID")
    public Object getID(HttpServletRequest request) {
        try {
            List<Long> tempLongList = new LinkedList<Long>();

            GetIDInfo getIDInfo = new GetIDInfo();
            getIDInfo.n = Integer.parseInt(request.getParameter("n"));
            // 还有一种方式是 使用事务， 这样取到的是连续的，而且效率可能会更高。但是容易死锁
            // 事务有时候不可靠，因为更新的条目太多
            for (int i = 0; i < getIDInfo.n; i++) {
                PersonalNoPrismRecord robotPrismrecord = new PersonalNoPrismRecord();
                robotPrismrecord.setGetLogicId(Integer.parseInt(request.getHeader("logicId")));
                robotPrismrecord.setGetInternetIp(request.getRemoteHost());
                robotPrismrecord.setGetTime(new Date());
                robotPrismrecord.setDb(DBRobotPrismrecord);
                boolean b = prismrecordService.add(robotPrismrecord);
                if (!b) {
                    throw new RuntimeException("数据库更新wechat失败");
                }
                tempLongList.add(robotPrismrecord.getId().longValue());
            }
            return tempLongList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 手机端网络日志
    @PostMapping("addRobotLog.do")
    public SunApiResponse addRobotLog(@RequestBody AddLogInfo logInfo, HttpServletRequest request) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
            this.updateLastConnectInfo(logInfo.logic_id, logInfo.wx_id, request);
            if (G.ms_IS_LOG_ROBOT_INFO_TO_DB) {
                PersonalNoRobotLogAndroid tempRL = new PersonalNoRobotLogAndroid();
                tempRL.setOperationStockWechatAccountId(Integer.parseInt(logInfo.logic_id));
                tempRL.setWxId(logInfo.wx_id);
                tempRL.setContent(logInfo.content);
                tempRL.setAddTime(new Date());
                tempRL.setInternetIp(request.getRemoteHost());
                tempRL.setDb(DBRobotLog);
                boolean b = logAndroidService.add(tempRL) > 0;
                if (!b) {
                    throw new RuntimeException("数据库更新wechat失败");
                }
            }

            if (G.ms_IS_LOG_ROBOT_INFO_TO_FILE) {
                String tempTag = logInfo.nick_name + "_" + logInfo.wx_id;
                FileWriter fw = null;
                // 如果文件存在，则追加内容；如果文件不存在，则创建文件
                File f = new File(".\\logs\\log_" + tempTag + ".txt");
                fw = new FileWriter(f, true);
                PrintWriter pw = new PrintWriter(fw);
                pw.println(logInfo.content);
                pw.flush();
                fw.flush();
                pw.close();
                fw.close();
            }
            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            return tempSunApiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            tempSunApiResponse.setCode(SunApiResponse.CODE_SYS_ERROR);
            return tempSunApiResponse;
        }
    }

    // 每次启动都会调用这个, 试图注册当前微信号 ,如果已经注册了, 就返回当前logicId
    // logicId其实robot表的主键
    @PostMapping("newRegister.do")
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.REPEATABLE_READ, timeout = 30)
    public IntegerResponse newRegister(@RequestBody NewRegisterInfo newRegisterInfo, HttpServletRequest request, HttpServletResponse response) {
        IntegerResponse tempIntegerResponse = new IntegerResponse();
        log.info("开始注册新个人号信息");
        try {
            log.info("根据个人号昵称和工程实例查找个人号列表");
            String getSql = DaoGetSql.getSql("SELECT * from " + DBWeChat + " where wx_id = ? and operation_project_instance_id = ? limit 0,1", newRegisterInfo.username, G.ms_OPERATION_PROJECT_INSTANCE_ID);
            Sql sql = new Sql(getSql);
            PersonalNoOperationStockWechatAccount wechatAccount = wechatAccountService.getBySql(sql);
            if (!VerifyUtils.isEmpty(wechatAccount)) {
                if (wechatAccount.getOperationProjectInstanceId() == G.ms_OPERATION_PROJECT_INSTANCE_ID) {
                    wechatAccount.setCurrentClientWehookVersion(newRegisterInfo.version);
                    wechatAccount.setCurrentClientWechatVersion(newRegisterInfo.wechatVersion);
                    wechatAccount.setArea(newRegisterInfo.alias);
                    wechatAccount.setProjectInstanceRegTime(new Date());
                    wechatAccount.setQrCode(newRegisterInfo.qrCode);
                    wechatAccount.setLastUpdateLocalIp(newRegisterInfo.ip);
                    wechatAccount.setLastConnectTime(new Date());
                    wechatAccount.setClickId(newRegisterInfo.clickId);
                    wechatAccount.setNickName(newRegisterInfo.nickname);
                    wechatAccount.setDb(DBWeChat);
                    wechatAccountService.add(wechatAccount);
                    PersonalNoPhoneTaskGroup taskGroup = new PersonalNoPhoneTaskGroup();
                    taskGroup.setStatus("未下发");
                    taskGroup.setTaskOrder(9);
                    taskGroup.setTotalStep(1);
                    taskGroup.setNextStep(1);
                    taskGroup.setTname(wechatAccount.getWxId() + "获取自身二维码");
                    taskGroup.setCreateTime(new Date());
                    taskGroup.setCurrentRobotId(wechatAccount.getWxId());
                    taskGroup.setDb(DBTaskGroup);
                    boolean insert = taskGroupService.add(taskGroup) > 0;
                    if (insert) {
                        PersonalNoPhoneTask task = new PersonalNoPhoneTask();
                        task.setStatus("未下发");
                        task.setStep(1);
                        task.setTaskGroupId(taskGroup.getId());
                        task.setTname(wechatAccount.getWxId() + "获取自身二维码");
                        task.setCreateTime(new Date());
                        task.setTaskType(SunTaskType.UPDATE_SELF_QRCODE);
                        task.setRobotId(wechatAccount.getWxId());
                        task.setDb(DBTask);
                        taskService.add(task);
                    }
                    log.info("修改个人号关键词表的昵称");
                    getSql = DaoGetSql.getSql("UPDATE " + DBNoAndKeyword + " SET `personal_no_nick_name` = ? WHERE `personal_no_wx_id` = ?", wechatAccount.getNickName(), wechatAccount.getWxId());
                    sql.setSql(getSql);
                    personalNoAndKeywordService.updateBySql(sql);
                    log.info("修改个人号和类别任务组表的昵称");
                    getSql = DaoGetSql.getSql("UPDATE " + DBNoAndGroupCategory + " SET `nick_name` = ? WHERE `personal_no_wx_id` = ?", wechatAccount.getNickName(), wechatAccount.getWxId());
                    sql.setSql(getSql);
                    noCategoryAndGroupService.updateBySql(sql);
                    log.info("修改朋友圈个人号表昵称");
                    getSql = DaoGetSql.getSql("UPDATE " + DBFriendsCirclePersonal + " SET `personal_no_name` = ? WHERE `personal_no_wx_id` = ?", wechatAccount.getNickName(), wechatAccount.getWxId());
                    sql.setSql(getSql);
                    friendsCirclePersonalService.updateBySql(sql);
                    log.info("修改非通道消息表的昵称");
                    getSql = DaoGetSql.getSql("UPDATE " + DBSendMessage + " SET `nick_name` = ? WHERE `personal_wx_id` = ?", wechatAccount.getNickName(), wechatAccount.getWxId());
                    sql.setSql(getSql);
                    sendMessageService.updateBySql(sql);
                    log.info("修改任务个人号表的昵称");
                    getSql = DaoGetSql.getSql("UPDATE " + DBTaskPersonal + " SET `personal_no_name` = ? WHERE `personal_no_wx_id` = ?", wechatAccount.getNickName(), wechatAccount.getWxId());
                    sql.setSql(getSql);
                    taskPersonalService.updateBySql(sql);
                    log.info("修改个人号消息表的昵称");
                    getSql = DaoGetSql.getSql("UPDATE " + DBSendMessage + " SET `nick_name` = ? WHERE `personal_wx_id` = ?", wechatAccount.getNickName(), wechatAccount.getWxId());
                    sql.setSql(getSql);
                    sendMessageService.updateBySql(sql);
                    tempIntegerResponse.code = (SunApiResponse.CODE_SUCCESS);
                    tempIntegerResponse.msg = "修改个人号信息成功";
                    tempIntegerResponse.data = wechatAccount.getId();
                    return tempIntegerResponse;
                } else {
                    tempIntegerResponse.code = SunApiResponse.CODE_SYS_ERROR;
                    tempIntegerResponse.msg = "已经在别的实例下存在";
                    tempIntegerResponse.data = -1;
                    G.requestException(DBRequestException, requestExceptionService, request, JsonObjectUtils.objectToJson(newRegisterInfo), "已经在别的实例下存在", JsonObjectUtils.objectToJson(tempIntegerResponse), SunApiResponse.CODE_SYS_ERROR);
                    return tempIntegerResponse;
                }
            }
            getSql = DaoGetSql.getSql("SELECT * from " + DBWeChat + " where `wx_id_bie_ming` = ? and operation_project_instance_id = ? limit 0,1", newRegisterInfo.alias, G.ms_OPERATION_PROJECT_INSTANCE_ID);
            sql.setSql(getSql);
            wechatAccount = wechatAccountService.getBySql(sql);
            if (!VerifyUtils.isEmpty(wechatAccount)) {
                wechatAccount.setCurrentClientWehookVersion(newRegisterInfo.version);
                wechatAccount.setCurrentClientWechatVersion(newRegisterInfo.wechatVersion);
                wechatAccount.setArea(newRegisterInfo.alias);
                wechatAccount.setProjectInstanceRegTime(new Date());
                if (newRegisterInfo.qrCode.contains("http://")) {
                    wechatAccount.setQrCode(newRegisterInfo.qrCode);
                } else {
                    wechatAccount.setQrCode(G.qrurl + newRegisterInfo.qrCode);
                }
                wechatAccount.setLastUpdateLocalIp(newRegisterInfo.ip);
                wechatAccount.setLastConnectTime(new Date());
                wechatAccount.setClickId(newRegisterInfo.clickId);
                wechatAccount.setNickName(newRegisterInfo.nickname);
                wechatAccount.setWxId(newRegisterInfo.username);
                wechatAccount.setWxIdBieMing(newRegisterInfo.alias);
                wechatAccount.setStatus("正常");
                wechatAccount.setDb(DBWeChat);
                wechatAccountService.add(wechatAccount);
                log.info("下发任务更新自身二维码");
                PersonalNoPhoneTaskGroup taskGroup = new PersonalNoPhoneTaskGroup();
                taskGroup.setStatus("未下发");
                taskGroup.setTaskOrder(9);
                taskGroup.setTotalStep(1);
                taskGroup.setNextStep(1);
                taskGroup.setTname(wechatAccount.getWxId() + "获取自身二维码");
                taskGroup.setCreateTime(new Date());
                taskGroup.setCurrentRobotId(wechatAccount.getWxId());
                taskGroup.setDb(DBTaskGroup);
                boolean insert = taskGroupService.add(taskGroup) > 0;
                if (insert) {
                    PersonalNoPhoneTask task = new PersonalNoPhoneTask();
                    task.setStatus("未下发");
                    task.setStep(1);
                    task.setTaskGroupId(taskGroup.getId());
                    task.setTname(wechatAccount.getWxId() + "获取自身二维码");
                    task.setCreateTime(new Date());
                    task.setTaskType(SunTaskType.UPDATE_SELF_QRCODE);
                    task.setRobotId(wechatAccount.getWxId());
                    task.setDb(DBTask);
                    taskService.add(task);
                }
            } else {
                tempIntegerResponse.code = (SunApiResponse.CODE_SYS_ERROR);
                G.requestException(DBRequestException, requestExceptionService, request, JsonObjectUtils.objectToJson(newRegisterInfo), "没有此昵称", JsonObjectUtils.objectToJson(tempIntegerResponse), SunApiResponse.CODE_SYS_ERROR);
                return tempIntegerResponse;
            }
            tempIntegerResponse.code = (SunApiResponse.CODE_SUCCESS);
            tempIntegerResponse.msg = "修改个人号信息成功";
            tempIntegerResponse.data = wechatAccount.getId();
            return tempIntegerResponse;
        } catch (Exception e) {
            e.printStackTrace();
            tempIntegerResponse.code = (SunApiResponse.CODE_SYS_ERROR);
            G.requestException(DBRequestException, requestExceptionService, request, JsonObjectUtils.objectToJson(newRegisterInfo), "登录出现异常", e.getMessage(), SunApiResponse.CODE_SYS_ERROR);
            return tempIntegerResponse;
        }
    }

    // 心跳链接
    // 微信端每隔10秒钟请求一次状态码，如果状态码有变化，就重新请求DeviceConf对象
    @PostMapping("status.do")
    public IntegerResponse status(@RequestBody StatusInfo statusInfo, HttpServletRequest request) {
        IntegerResponse tempIR = new IntegerResponse();
        try {
            String wxid = statusInfo.username;
            this.updateLastConnectInfo(null, wxid, request);
            Integer statusCode = G.ms_robotStatusCodeMap.get(wxid);
            if (statusCode == null) {
                statusCode = G.ms_ROBOT_STATUS_START_CODE;
                G.ms_robotStatusCodeMap.put(wxid, statusCode);
            }
            tempIR.code = (SunApiResponse.CODE_SUCCESS);
            tempIR.data = statusCode;
            return tempIR;
        } catch (Exception e) {
            tempIR.code = (SunApiResponse.CODE_SYS_ERROR);
            G.requestException(DBRequestException, requestExceptionService, request, JsonObjectUtils.objectToJson(statusInfo), "登录出现异常", e.getMessage(), SunApiResponse.CODE_SYS_ERROR);
            return tempIR;
        }
    }

    // 从服务器获取当前配置信息, 黑白名单
    @PostMapping("queryConf.do")
    public SunApiResponse queryConf(@RequestBody QueryConfInfo queryConfInfo, HttpServletRequest request) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
            DeviceConf tempDeviceConf = new DeviceConf();
            Set<String> blockUsernames = new HashSet<String>();
            // 此处查询添加黑名单成员
            // ........
//           List<WxUser> tempWUList = new WxUser().dao().find("select * from wx_user where is_black=1");
            String getSql = DaoGetSql.getSql("SELECT wx_id from " + DBBlack);
            Sql sql = new Sql(getSql);
            List<String> blackList = blacklistService.listStringBySql(sql);
            blockUsernames.addAll(blackList);
            getSql = DaoGetSql.getSql("SELECT * from " + DBWeChat + " where id = ? and operation_project_instance_id = ? limit 0,1", queryConfInfo.logicId, G.ms_OPERATION_PROJECT_INSTANCE_ID);
            sql.setSql(getSql);
            PersonalNoOperationStockWechatAccount tempOSWA = wechatAccountService.getBySql(sql);
            // 如果机器人id不存在，或者不是当前实例的微信号，或者类型不明等等，就不让他请求任务
            if (null == tempOSWA) {
                tempDeviceConf = DeviceConf.noServiceConf();
                //throw new Exception("queryConf函数无法从数据库找到logiId=" + queryConfInfo.logicId + "的记录");
                G.i(Integer.parseInt(request.getHeader("logicId")), request.getRequestURL().toString(), "queryConf函数无法从数据库找到logiId=" + queryConfInfo.logicId + "的记录   rawInfo=" + queryConfInfo);
            } else if (tempOSWA.getOperationProjectInstanceId() != G.ms_OPERATION_PROJECT_INSTANCE_ID) {
                tempDeviceConf = DeviceConf.noServiceConf();
                throw new RuntimeException("logiId=" + queryConfInfo.logicId + "的微信号是其他工程实例的微信号！");
            } else {
                tempOSWA.setLastConnectInternetIp(request.getRemoteHost());
                tempOSWA.setLastConnectTime(new Date());
                tempOSWA.setDb(DBWeChat);
                Integer b = wechatAccountService.add(tempOSWA);
                // 上传信息
                tempDeviceConf.setChatroomDownloadPic(1);
                tempDeviceConf.setIndividualDownloadPic(1);
                tempDeviceConf.setUpChatroom(1);
                tempDeviceConf.setUpNormalMsg(1);
                tempDeviceConf.setUpOtherOptMsg(1);
                tempDeviceConf.setUpSelfOptMsg(1);
                tempDeviceConf.setUpSelfSendMsg(1);

            }
            tempDeviceConf.setBlockUsernames(blockUsernames);
            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            tempSunApiResponse.setData(tempDeviceConf);
            return tempSunApiResponse;
        } catch (Exception e) {
            tempSunApiResponse.setCode(SunApiResponse.CODE_SYS_ERROR);
            return tempSunApiResponse;
        }
    }

    // 更新当前微信信息
    @PostMapping("updateWeChatInfo.do")
    public SunApiResponse updateWeChatInfo(@RequestBody UpdateWeChatInfo updateWeChatInfo, HttpServletRequest request) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
            // ...获取并更新微信信息
            String getSql = DaoGetSql.getSql("SELECT * from " + DBWeChat + " where wx_id = ? and operation_project_instance_id = ? limit 0,1", updateWeChatInfo.username, G.ms_OPERATION_PROJECT_INSTANCE_ID);
            Sql sql = new Sql(getSql);
            PersonalNoOperationStockWechatAccount tempOSWA = wechatAccountService.getBySql(sql);
            if (tempOSWA != null) {
                tempOSWA.setWxIdBieMing(updateWeChatInfo.alias);
                tempOSWA.setNickName(updateWeChatInfo.nickname);
                tempOSWA.setAssignPhone(updateWeChatInfo.phone);
                tempOSWA.setEmail(updateWeChatInfo.email);
                tempOSWA.setWhatsUp(updateWeChatInfo.whatsUp);
                tempOSWA.setArea(updateWeChatInfo.region);
                tempOSWA.setIsHaveRealname(updateWeChatInfo.realName ? 1 : 0);
                tempOSWA.setIsSlientDownload(updateWeChatInfo.silentDownload ? 1 : 0);
                tempOSWA.setLastConnectInternetIp(request.getRemoteHost());
                tempOSWA.setLastConnectTime(new Date());
                tempOSWA.setDb(DBWeChat);
                wechatAccountService.add(tempOSWA);
            } else {
                throw new RuntimeException("微信号库存表中当前工程实例下不存在的wxid请求了updateWeChatInfo接口：wxid=" + updateWeChatInfo.username);
            }
            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            return tempSunApiResponse;
        } catch (Exception e) {
            tempSunApiResponse.setCode(SunApiResponse.CODE_SYS_ERROR);
            return tempSunApiResponse;
        }
    }

    // 查询所有机器人的wx_id
    // 返回当前工程实例的 所有u助手，库存微信号
    @PostMapping("queryRobotUsernames.do")
    public SunApiResponse queryRobotUsernames() {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
            log.info("查询u助手和库存微信号");
            String getSql = DaoGetSql.getSql("SELECT * from " + DBWeChat + " where operation_project_instance_id = ? limit 0,1", G.ms_OPERATION_PROJECT_INSTANCE_ID);
            Sql sql = new Sql(getSql);
            List<PersonalNoOperationStockWechatAccount> tempOSWAList = wechatAccountService.listbySql(sql);
            List<String> allRobotUsernames = new LinkedList<String>();
            for (int i = 0; i < tempOSWAList.size(); i++) {
                allRobotUsernames.add(tempOSWAList.get(i).getWxId());
            }

            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            tempSunApiResponse.setData(allRobotUsernames);
            return tempSunApiResponse;
        } catch (Exception e) {
            tempSunApiResponse.setCode(SunApiResponse.CODE_SYS_ERROR);
            return tempSunApiResponse;
        }
    }

    // 状态描述
    @PostMapping("statusDesc.do")
    public SunApiResponse statusDesc(@RequestBody StatusDescInfo statusDescInfo, HttpServletRequest request) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        log.info("开始执行状态描述方法");
        String tempStr = "";
        try {
            // 根据username查询当前微信号的状态描述
            String getSql = DaoGetSql.getSql("SELECT * from " + DBWeChat + " where wx_id = ? and operation_project_instance_id = ? limit 0,1", statusDescInfo.username, G.ms_OPERATION_PROJECT_INSTANCE_ID);
            Sql sql = new Sql(getSql);
            PersonalNoOperationStockWechatAccount tempOSWA = wechatAccountService.getBySql(sql);
            tempStr = "当前工程实例：[" + G.ms_currProjectInstanceName + "]\r\n";
            if (null == tempOSWA) {
                log.info("当前实例下不存在该微信号");
                tempStr += "当前工程实例下找不到该微信号。wx_id=" + statusDescInfo.username;
                throw new RuntimeException("当前工程实例下找不到该微信号。wx_id=" + statusDescInfo.username);
            } else {
                log.info("开始修改个人号机器人状态信息");
                tempStr += "设备号ClickId:" + tempOSWA.getClickId();
                tempStr += "\r\nlogicId:" + tempOSWA.getId();
                tempStr += "\r\n当前微信号:" + tempOSWA.getWxId();
                tempStr += "\r\n角色:" + tempOSWA.getWxClass();
                tempStr += "\r\n当前运行状态:" + tempOSWA.getStatus();
                tempStr += "\r\n微信号登陆时间：" + tempOSWA.getWxLoginTime();
                tempStr += "\r\n在当前工程实例的注册时间：" + tempOSWA.getProjectInstanceRegTime();
                tempStr += "\r\n最后请求任务时间：" + tempOSWA.getLastRequestJobTime();
                tempStr += "\r\n最后完成任务时间：" + tempOSWA.getLastFinishedJobTime();
                tempStr += "\r\n最后链接服务器时间：" + tempOSWA.getLastConnectTime();
                tempStr += "\r\n最后上报服务器的局域网IP：" + tempOSWA.getLastUpdateLocalIp();
                tempStr += "\r\n最后链接服务器的外网IP：" + tempOSWA.getLastConnectInternetIp();
                tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);

                tempOSWA.setLastConnectInternetIp(request.getRemoteHost());
                tempOSWA.setLastConnectTime(new Date());
                tempOSWA.setDb(DBWeChat);
                wechatAccountService.add(tempOSWA);
            }
            tempSunApiResponse.setData(tempStr);
            return tempSunApiResponse;
        } catch (Exception e) {
            log.error("发生未知错误，无法解决");
            tempSunApiResponse.setData(tempStr);
            tempSunApiResponse.setCode(SunApiResponse.CODE_SYS_ERROR);
            return tempSunApiResponse;
        }
    }

    // 更新基本信息_微信版本号ClickId等
    @PostMapping("updateBasicInfo.do")
    public SunApiResponse updateBasicInfo(@RequestBody UpdateBasicInfo ubi, HttpServletRequest request) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        log.info("开始更细微信版本的基本信息");
        try {
//            OperationStockWechatAccount tempOSWA = new OperationStockWechatAccount().dao().findFirst("select * from operation_stock_wechat_account where operation_project_instance_id=" + G.ms_OPERATION_PROJECT_INSTANCE_ID + " and id='" + ubi.logicId + "'");
            log.info("根据id和势力id查询对应的个人号机器人");
            String getSql = DaoGetSql.getSql("SELECT * from " + DBWeChat + " where id = ? and operation_project_instance_id = ? limit 0,1", ubi.logicId, G.ms_OPERATION_PROJECT_INSTANCE_ID);
            Sql sql = new Sql(getSql);
            PersonalNoOperationStockWechatAccount tempOSWA = wechatAccountService.getBySql(sql);
            if (tempOSWA == null) {
                log.info("未找到对应的个人号机器人");
                throw new RuntimeException("updateBasicInfo 查询不到logicId=" + ubi.logicId + " operation_project_instance_id=" + G.ms_OPERATION_PROJECT_INSTANCE_ID);
            }
            log.info("开始修改个人号机器人的相关信息");
            tempOSWA.setLastUpdateLocalIp(ubi.ip);
            tempOSWA.setCurrentClientWehookVersion(ubi.version);
            tempOSWA.setCurrentClientWechatVersion(ubi.wechatVersion);

            tempOSWA.setEmail(ubi.deviceWeChatInfo.email);
            tempOSWA.setNickName(ubi.deviceWeChatInfo.nickname);
            tempOSWA.setAssignPhone(ubi.deviceWeChatInfo.phone);
            tempOSWA.setArea(ubi.deviceWeChatInfo.region);
            tempOSWA.setWhatsUp(ubi.deviceWeChatInfo.whatsUp);

            tempOSWA.setLastConnectTime(new Date());
            tempOSWA.setLastConnectInternetIp(request.getRemoteHost());
            tempOSWA.setDb(DBWeChat);
            wechatAccountService.add(tempOSWA);
            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            return tempSunApiResponse;
        } catch (Exception e) {
            log.error("updateBasicInfo出现异常");
            tempSunApiResponse.setCode(SunApiResponse.CODE_SYS_ERROR);
            return tempSunApiResponse;
        }
    }

    // 从服务器获取SunTask
    // 下发任务的同时, 要根据给当前任务存一个标志, 以供后期完成任务的时候能从任务表里标定任务. 完成任务后需要把这个tag给去掉
    @PostMapping("pickTask.do")
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.REPEATABLE_READ, timeout = 30)
    public SunApiResponse pickTask(@RequestBody PickupTaskInfo pickupTaskInfo, HttpServletRequest request, HttpServletResponse response) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
            log.info("开始执行手机请求任务的操作");
            List<SunTask> tempSunTaskList = new LinkedList<SunTask>();
            Integer currRobotLogicId = pickupTaskInfo.logicId;
            String currRobotWxid = pickupTaskInfo.username;
            Map<String, Object> map = TaskUtiles.getMap(taskPeopleService, taskGroupService, noTaskService, taskService, messageService);
            // 更新机器人最后获取任务的时间
            log.info("去数据库查找该个人号机器人");
            PersonalNoOperationStockWechatAccount tempOSWA = null;
            String getSql = "";
            Sql sql = new Sql();
            if ((null != currRobotLogicId) && (!"".equals(currRobotLogicId))) {
                getSql = DaoGetSql.getSql("SELECT * from " + DBWeChat + " where id = ? and operation_project_instance_id = ? limit 0,1", pickupTaskInfo.logicId, G.ms_OPERATION_PROJECT_INSTANCE_ID);
                sql.setSql(getSql);
                tempOSWA = wechatAccountService.getBySql(sql);
            } else if (tempOSWA == null && null != currRobotWxid && (!"".equals(currRobotWxid))) {
                getSql = DaoGetSql.getSql("SELECT * from " + DBWeChat + " where wx_id = ? and operation_project_instance_id = ? limit 0,1", pickupTaskInfo.username, G.ms_OPERATION_PROJECT_INSTANCE_ID);
                sql.setSql(getSql);
                tempOSWA = wechatAccountService.getBySql(sql);
            }
            if (null == tempOSWA) {
                G.i(Integer.parseInt(request.getHeader("logicId")), request.getRequestURL().toString(), "当前工程实例中不存在该微信号：logicId=" + currRobotLogicId + ",wx_id=" + currRobotWxid + "   rawInfo=" + pickupTaskInfo);
                tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
                return tempSunApiResponse;
            } else {
                log.info("更新个人号机器人最后请求任务的时间和链接时间");
                tempOSWA.setLastRequestJobTime(new Date());
                tempOSWA.setLastConnectTime(new Date());
                tempOSWA.setLastConnectInternetIp(request.getRemoteHost());
                tempOSWA.setDb(DBWeChat);
                wechatAccountService.add(tempOSWA);
            }

            log.info("将当前手机请求任务的时间添加到数据库");
            PersonalNoPhoneRequestTaskTime phoneRequestTaskTime = new PersonalNoPhoneRequestTaskTime();
            phoneRequestTaskTime.setWxId(tempOSWA.getWxId());
            phoneRequestTaskTime.setNickName(tempOSWA.getNickName());
            phoneRequestTaskTime.setRequestTime(new Date());
            phoneRequestTaskTime.setDb(DBPhoneRequestTime);
            phoneRequestTaskTimeService.add(phoneRequestTaskTime);
            SunTask sunTask = null;
            PersonalNoPhoneTaskGroup taskGroup = null;
            PersonalNoPhoneTask byId = null;
            log.info("先找order为10的添加好友任务");
            getSql = DaoGetSql.getSql("SELECT * from " + DBTaskGroup + " where current_robot_id = ? and create_time < ? and status = '未下发' and task_order = ?", tempOSWA.getWxId(), WebConst.getNowDate(new Date()), 10);
            sql.setSql(getSql);
            List<PersonalNoPhoneTaskGroup> wxid_o72bs8evoigc22 = taskGroupService.listBySql(sql);
            if (!VerifyUtils.collectionIsEmpty(wxid_o72bs8evoigc22)) {
                log.info("有添加好友任务");
                taskGroup = wxid_o72bs8evoigc22.get(0);
                log.info("找到要执行的添加好友任务");
                getSql = DaoGetSql.getSql("SELECT * FROM " + DBTask + " WHERE task_group_id = ? and step = ? limit 0,1", taskGroup.getId(), taskGroup.getNextStep());
                sql.setSql(getSql);
                byId = taskService.getBySql(sql);
                if (!VerifyUtils.isEmpty(byId) && "未下发".equals(byId.getStatus())) {
                    if (!VerifyUtils.isEmpty(taskGroup.getTaskSendId())) {

                    }
                    log.info("根据个人号微信id和好友微信id确认是哪一个个人号下的哪一个任务粉丝");
                    getSql = DaoGetSql.getSql("SELECT * from " + DBPeople + " where personal_no_wx_id = ? and personal_friend_wx_id = ? and personal_task_id = ? order by be_friend_time desc limit 0,1", taskGroup.getCurrentRobotId(), byId.getRobotId(), taskGroup.getTaskSendId());
                    sql.setSql(getSql);
                    PersonalNoPeople personalNoPeople = taskPeopleService.getBySql(sql);
                    if (!VerifyUtils.isEmpty(personalNoPeople)) {
                        personalNoPeople.setFlag(2);
                        personalNoPeople.setDb(DBPeople);
                        taskPeopleService.add(personalNoPeople);
                        log.info("处理渠道和用户信息关系对应表，插入用户wxid");
                        PassageVisitorRecord passageVisitorRecord = passageVisitorRecordService.getByUnionId(personalNoPeople.getPersonalFriendNickName());
                        if (!VerifyUtils.isEmpty(passageVisitorRecord) && VerifyUtils.isEmpty(passageVisitorRecord.getUserWxId())) {
                            passageVisitorRecordService.updateUserWxIdById(byId.getRobotId(), passageVisitorRecord.getId());
                        }
                    }
                    log.info("下发十分钟后的任务回复消息");
                    getSql = DaoGetSql.getById(DBValueTable, 7);
                    sql.setSql(getSql);
                    PersonalNoValueTable valueTable7 = valueTableService.getBySql(sql);
                    Integer time = 600000;
                    if (!VerifyUtils.isEmpty(valueTable7)) {
                        time = Integer.parseInt(valueTable7.getValue()) * 1000;
                    }
                    TaskUtiles.toTask(map, taskGroup.getCurrentRobotId(), byId.getRobotId(), taskGroup.getTaskSendId(), time);
                    log.info("添加任务开课提醒");
                    TaskUtiles.toRemindTask(map, remindFlagService, taskGroup.getCurrentRobotId(), byId.getRobotId(), taskGroup.getTaskSendId(), 0);
                    log.info("根据任务id获取要发送的任务标签信息");
                    getSql = DaoGetSql.getSql("SELECT * FROM " + DBTaskLable + " WHERE `personal_no_task_id` = ? AND `deleted` = 0", taskGroup.getTaskSendId());
                    sql.setSql(getSql);
                    List<PersonalNoTaskLable> taskLableList = taskLableService.listBysql(sql);
                    log.info("给微信好友添加标签");
                    toAddFriendLableTask(byId.getRobotId(), taskLableList, tempOSWA, time);
                    sunTask = SunTaskHelper.getTask_personOP(SunTaskType.FRIEND_ACCEPT_REQUEST, null, null, null, null, byId.getTaskJson());
                    tempSunTaskList.add(sunTask);
                }
            } else {
                getSql = DaoGetSql.getSql("SELECT * from " + DBTaskGroup + " where current_robot_id = ? and create_time < ? and status = '未下发' and task_order = ?", tempOSWA.getWxId(), WebConst.getNowDate(new Date()), 9);
                sql.setSql(getSql);
                wxid_o72bs8evoigc22 = taskGroupService.listBySql(sql);
                if (VerifyUtils.collectionIsEmpty(wxid_o72bs8evoigc22)) {
                    log.info("没有将要执行的自动回复任务");
                    getSql = DaoGetSql.getSql("SELECT * from " + DBTaskGroup + " where current_robot_id = ? and create_time < ? and status = '未下发' and task_order = ?", tempOSWA.getWxId(), WebConst.getNowDate(new Date()), 0);
                    sql.setSql(getSql);
                    wxid_o72bs8evoigc22 = taskGroupService.listBySql(sql);
                    if (VerifyUtils.collectionIsEmpty(wxid_o72bs8evoigc22)) {
                        log.info("没有将要执行的普通任务");
                    }
                }
            }
            log.info("将一条任务下发给手机");
            List<SunTask> sunTasks1 = gettempSunTaskList(wxid_o72bs8evoigc22);
            tempSunTaskList.addAll(sunTasks1);
            log.info("根据当前时间查看是否有此时间段的定时任务");
            getSql = DaoGetSql.getSql("SELECT * from " + DBTaskGroup + " where current_robot_id = ? and create_time < ? and task_order = ?", tempOSWA.getWxId(), WebConst.getNowDate(new Date()), 1);
            sql.setSql(getSql);
            List<PersonalNoPhoneTaskGroup> taskGroups = taskGroupService.listBySql(sql);
            if (!VerifyUtils.collectionIsEmpty(taskGroups)) {
                List<SunTask> sunTasks = gettempSunTaskList(taskGroups);
                log.info("将定时任务一起发送给手机");
                tempSunTaskList.addAll(sunTasks);
            }
            if (toGroup) {
                tempSunTaskList.add(toGroupSunTask);
            }
            log.info("判断是否需要主动回复消息隔时和定时");
            getSql = DaoGetSql.getSql("SELECT * FROM " + DBTemp + " where personal_no_wx_id = ? and create_time = ? and greetings = 0", tempOSWA.getWxId(), WebConst.getNowDate(new Date()));
            sql.setSql(getSql);
            List<PersonalNoTemp> tempList = tempService.listBysql(sql);
            for (PersonalNoTemp temp : tempList) {
                getSql = DaoGetSql.getSql("SELECT * from " + DBSendMessage + " where id = ? limit 0,1", temp.getPersonalNoSendMessageId());
                sql.setSql(getSql);
                PersonalNoSendMessage sendMessage = sendMessageService.getBySql(sql);
                if (!VerifyUtils.isEmpty(temp.getEverTime())) {
                    if (new Date().getTime() - temp.getCreateTime().getTime() > Integer.parseInt(temp.getEverTime()) * 1000) {
                        TaskUtiles.toMessageTask(map, tempOSWA.getWxId(), temp.getUserWxId(), sendMessage.getMessageId(), 0);
                        temp.setGreetings(1);
                        temp.setEver(1);
                        temp.setDb(DBTemp);
                        tempService.add(temp);
                    }
                } else if (!VerifyUtils.isEmpty(temp.getTimingTime())) {
                    if (temp.getTimingTime().getTime() - new Date().getTime() < 0) {
                        TaskUtiles.toMessageTask(map, tempOSWA.getWxId(), temp.getUserWxId(), sendMessage.getMessageId(), 0);
                        temp.setGreetings(1);
                        temp.setTiming(1);
                        temp.setDb(DBTemp);
                        tempService.add(temp);
                    }
                }
            }
            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            tempSunApiResponse.setData(tempSunTaskList);
            return tempSunApiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            tempSunApiResponse.setCode(SunApiResponse.CODE_SYS_ERROR);
            G.requestException(DBRequestException, requestExceptionService, request, JsonObjectUtils.objectToJson(pickupTaskInfo), "手机请求任务报错", JsonObjectUtils.objectToJson(tempSunApiResponse), SunApiResponse.CODE_SYS_ERROR);
            return tempSunApiResponse;
        }
    }

    //将任务组集合转换为发给手机的任务集合
    private List<SunTask> gettempSunTaskList(List<PersonalNoPhoneTaskGroup> wxid_o72bs8evoigc22) {
        List<SunTask> tempSunTaskList = new ArrayList<>();
        PersonalNoPhoneTask byId = null;
        SunTask sunTask = null;
        if (!VerifyUtils.collectionIsEmpty(wxid_o72bs8evoigc22)) {
//            for (PersonalNoPhoneTaskGroup personalNoPhoneTaskGroup : wxid_o72bs8evoigc22) {
            PersonalNoPhoneTaskGroup taskGroup1 = wxid_o72bs8evoigc22.get(0);
            log.info("找到该任务组要执行的任务");
            String getSql = DaoGetSql.getSql("SELECT * FROM " + DBTask + " WHERE task_group_id = ? and step = ? limit 0,1", taskGroup1.getId(), taskGroup1.getNextStep());
            Sql sql = new Sql(getSql);
            byId = taskService.getBySql(sql);
            if (!VerifyUtils.isEmpty(byId) && "未下发".equals(byId.getStatus())) {
                sunTask = taskToSunTask(byId);
                log.info("开始下发任务");
                tempSunTaskList.add(sunTask);
                log.info("下发完更新任务状态");
                byId.setStatus("执行中");
                byId.setDb(DBTask);
                boolean b = taskService.add(byId) > 0;
                if (!b) {
                    throw new RuntimeException("更新任务失败");
                }
            }
            if (taskGroup1.getNextStep() < taskGroup1.getTotalStep()) {
                taskGroup1.setNextStep(taskGroup1.getNextStep() + 1);
            } else {
                taskGroup1.setStatus("已完成");
            }
            taskGroup1.setDb(DBTaskGroup);
            boolean update = taskGroupService.add(taskGroup1) > 0;
            if (!update) {
                throw new RuntimeException("更新任务组失败");
            }
//            }
        }
        return tempSunTaskList;
    }

    //将任务转换为手机任务
    private SunTask taskToSunTask(PersonalNoPhoneTask byId) {
        toGroup = false;
        SunTask sunTask = new SunTask();
        if (VerifyUtils.isEmpty(byId.getTaskType())) {
            log.info("没有任务类型,返回空任务");
            return sunTask;
        }
        if (byId.getTaskType() == SunTaskType.CHATROOM_INVITE_GT40_DIRECT) {
            log.info("发送邀请入群");
            toGroup = true;
            List<String> toUsernames = new ArrayList<>();
            toUsernames.add(byId.getRobotId());
            String[] split = byId.getContent().split("/");
            String groupWxId = "16768948111@chatroom";
            if (split.length > 1) {
                groupWxId = wxGroupService.getByCategoryId(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                toGroupSunTask = SunTaskHelper.getTask_chatroomOP(SunTaskType.CHATROOM_INVITE_LT40_DIRECT, groupWxId, toUsernames, null);
            }
            toGroupSunTask = SunTaskHelper.getTask_chatroomOP(SunTaskType.CHATROOM_INVITE_LT40_DIRECT, groupWxId, toUsernames, null);
            sunTask = SunTaskHelper.getTask_chatroomOP(SunTaskType.CHATROOM_INVITE_GT40_DIRECT, groupWxId, toUsernames, null);
        } else if (byId.getTaskType() == SunTaskType.UPLOAD_FRIEND_LIST) {
            log.info("上传好友列表");
            sunTask = SunTaskHelper.getTask_personOP(SunTaskType.UPLOAD_FRIEND_LIST, null, null, null, null, null);
        } else if (byId.getTaskType() == SunTaskType.ADD_FRIEND) {
            log.info("添加好友");
            List<String> list = new ArrayList<>();
            list.add("wxid_3xnlqw7q9tiu22");
            sunTask = SunTaskHelper.getTask_personOP(SunTaskType.ADD_FRIEND, list, null, byId.getContent(), null, byId.getContentType());
        } else if (byId.getTaskType() == SunTaskType.DELETE_FRIEND) {
            log.info("删除好友");
            List<String> list = new ArrayList<>();
            list.add(byId.getRobotId());
            sunTask = SunTaskHelper.getTask_personOP(SunTaskType.DELETE_FRIEND, list, null, null, null, null);
        } else if (byId.getTaskType() == SunTaskType.UPDATE_SELF_QRCODE) {
            log.info("更新自身二维码");
            sunTask = SunTaskHelper.getTask_personOP(SunTaskType.UPDATE_SELF_QRCODE, null, null, null, byId.getRobotId(), null);
        } else if (byId.getTaskType() == SunTaskType.FRIEND_ADD_LABEL) {
            log.info("给好友贴标签");
            List<String> list = new ArrayList<>();
            list.add(byId.getRobotId());
            sunTask = SunTaskHelper.getTask_personOP(SunTaskType.FRIEND_ADD_LABEL, list, byId.getContent(), null, null, null);
        } else if (byId.getTaskType() == SunTaskType.TIMELINE_NORMAL) {
            log.info("发送朋友圈");
            sunTask.setType(byId.getTaskType());
            sunTask.setContent(byId.getContent());
            List<String> strings = JsonObjectUtils.jsonToList(byId.getTaskJson(), String.class);
            sunTask.setContentList(strings);
        } else if (byId.getTaskType() == SunTaskType.FRIEND_SEND_MSG) {
            log.info("发送文字或图片消息");
            sunTask.setType(byId.getTaskType());
            if ("文字".equals(byId.getContentType())) {
                if (byId.getContent().split("&&").length > 1) {
                    String[] split = byId.getContent().split("&&");
                    byId.setContent(split[1]);
                } else {
                    log.info("获取表情库的所有id");
                    String getSql = DaoGetSql.getSql("SELECT id FROM  " + DBSmallFace);
                    Sql sql = new Sql(getSql);
                    List<Integer> idList = personalNoSmallFaceService.listIntegerBySql(sql);
                    log.info("随机取一个表情id，根据id获取表情");
                    int index = idList.get(new Random().nextInt(idList.size()));
                    getSql = DaoGetSql.getById(DBSmallFace, index);
                    sql.setSql(getSql);
                    PersonalNoSmallFace smallFace = personalNoSmallFaceService.getBySql(sql);
                    if (!VerifyUtils.isEmpty(smallFace)) {
                        log.info("将表情拼接到文字前边");
                        byId.setContent(smallFace.getFace() + " " + byId.getContent());
                    }
                }
                String getSql = DaoGetSql.getSql("SELECT * FROM  " + DBUser + "  WHERE  wx_id = ? limit 0,1", byId.getRobotId());
                Sql sql = new Sql(getSql);
                PersonalNoUser byWxId = userService.getBySql(sql);
                if (!VerifyUtils.isEmpty(byWxId) && !VerifyUtils.isEmpty(byWxId.getNickName()) && !VerifyUtils.isEmpty(byId.getContent())) {
                    byId.setContent(byId.getContent().replaceFirst("####", "亲爱的" + byWxId.getNickName() + ","));
                } else if (!VerifyUtils.isEmpty(byId.getContent())) {
                    byId.setContent(byId.getContent().replaceFirst("####", ""));
                }
                sunTask.setWeChatMsgType(WeChatMsgType.WECHAT_MESSAGE_TYPE_TEXT);
            } else if ("图片".equals(byId.getContentType())) {
                sunTask.setWeChatMsgType(WeChatMsgType.WECHAT_MESSAGE_TYPE_IMG);
            } else if ("名片".equals(byId.getContentType())) {
                sunTask.setWeChatMsgType(WeChatMsgType.WECHAT_MESSAGE_TYPE_CONTACT_CARD);
            } else if ("小程序".equals(byId.getContentType())) {
                if (byId.getContent().contains("<thumburl>") && byId.getContent().contains("</thumburl>")) {
                    String substring = byId.getContent().substring(byId.getContent().indexOf("<thumburl>") + 10, byId.getContent().indexOf("</thumburl>"));
                    sunTask.setExt(substring);
                }
                sunTask.setWeChatMsgType(WeChatMsgType.WECHAT_MESSAGE_TYPE_MSG_CARD);
            } else if ("文件".equals(byId.getContentType())) {
                sunTask.setWeChatMsgType(WeChatMsgType.WECHAT_MESSAGE_TYPE_IMG);
            } else if ("语音消息".equals(byId.getContentType())) {
                sunTask.setWeChatMsgType(WeChatMsgType.WECHAT_MESSAGE_TYPE_VOICE);
            }
            sunTask.setContent(byId.getContent());
            List<String> toUserLisd = new ArrayList<>();
            toUserLisd.add(byId.getRobotId());
            sunTask.setToUsernames(toUserLisd);
        }
        return sunTask;
    }

    // 告诉服务器已收到SunTask
    // 回馈了所有任务的唯一id
    @PostMapping("feedbackReceived.do")
    public SunApiResponse feedbackReceived(@RequestBody FeedBackReceivedInfo feedBackReceivedInfo, HttpServletRequest request) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            tempSunApiResponse.setData(feedBackReceivedInfo.taskIdList.size()); // 返回接受到的任务id的数目
            return tempSunApiResponse;
        } catch (Exception e) {
            tempSunApiResponse.setCode(SunApiResponse.CODE_SYS_ERROR);
            return tempSunApiResponse;
        }
    }

    // 完成SunTask之后通知服务器 //回馈的是任务id, 直接返回ok就行了
    // 建群等异步请求不能在这里更新任务状态
    // 其他可以
    @PostMapping("feedbackTask.do")
    public SunApiResponse feedbackTask(@RequestBody FeedbackTaskInfo feedbackTaskInfo, HttpServletRequest request) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
            // 更新任务状态
            for (int i = 0; i < feedbackTaskInfo.taskIdList.size(); i++) {
                Long tempTaskId = feedbackTaskInfo.taskIdList.get(i);
//                Task tempTask = new Task().dao().findById(tempTaskId);
                PersonalNoPhoneTask tempTask = taskService.selectById(tempTaskId);
                if (tempTask == null) {
                    // 说明这个任务已经被处理过了
                    continue;
                }
                //tempTask.setStatus("已完成");
                tempTask.setIsClientFeedbackFinished(1);
                tempTask.setFeedbackFinishedTime(new Date());
                tempTask.setDb(DBTask);
                boolean b = taskService.add(tempTask) > 0;
                if (!b) {
                    throw new RuntimeException("数据库更新失败");
                }
            }
            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            return tempSunApiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return tempSunApiResponse;
        }
    }

    // 任务失败后通知服务器
    @PostMapping("feedbackFailed.do")
    public SunApiResponse feedbackFailed(@RequestBody FeedbackFailedInfo feedbackFailedInfo, HttpServletRequest request) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            return tempSunApiResponse;
        } catch (Exception e) {
            tempSunApiResponse.setCode(SunApiResponse.CODE_SYS_ERROR);
            return tempSunApiResponse;
        }
    }

    // 更新聊天室二维码
    @PostMapping("updateQrCodeChatroomRel.do")
    public SunApiResponse updateQrCodeChatroomRel(@RequestBody UpdateQrCodeChatroomRelInfo updateQrCodeChatroomRelInfo, HttpServletRequest request) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
            return tempSunApiResponse;
        } catch (Exception e) {
            tempSunApiResponse.setCode(SunApiResponse.CODE_SYS_ERROR);
            return tempSunApiResponse;
        }
    }

    // queryChameleon 是否混淆微信端文件名
    @PostMapping("queryChameleon.do")
    public BooleanResponse queryChameleon() {
        BooleanResponse tempBooleanResponse = new BooleanResponse();
        try {
            tempBooleanResponse.code = SunApiResponse.CODE_SUCCESS;
            tempBooleanResponse.data = true;
            return tempBooleanResponse;
        } catch (Exception e) {
            tempBooleanResponse.code = SunApiResponse.CODE_SYS_ERROR;
            return tempBooleanResponse;
        }
    }

    // 更新上报机器人本地IP
    @PostMapping("updateRobotIp.do")
    public SunApiResponse updateRobotIp(@RequestBody UpdateRobotIpInfo updateRobotIpInfo, HttpServletRequest request) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
            String getSql = DaoGetSql.getSql("SELECT * from " + DBWeChat + " where id = ? and operation_project_instance_id = ? limit 0,1", updateRobotIpInfo.logicId, G.ms_OPERATION_PROJECT_INSTANCE_ID);
            Sql sql = new Sql(getSql);
            PersonalNoOperationStockWechatAccount tempOSWA = wechatAccountService.getBySql(sql);
            if (tempOSWA == null) {
                throw new RuntimeException("updateRobotIp 数据库里的查不到该robot,logicId=" + updateRobotIpInfo.logicId);
            }
            tempOSWA.setLastUpdateLocalIp(updateRobotIpInfo.ip);
            tempOSWA.setLastConnectInternetIp(request.getRemoteHost());
            tempOSWA.setLastConnectTime(new Date());
            tempOSWA.setDb(DBWeChat);
            Integer b = wechatAccountService.add(tempOSWA);
            if (b < 0) {
                throw new RuntimeException("更新微信号信息失败" + updateRobotIpInfo.logicId);
            }
            tempSunApiResponse.setCode(SunApiResponse.CODE_UNREALIZATION);
            return tempSunApiResponse;
        } catch (Exception e) {
            tempSunApiResponse.setCode(SunApiResponse.CODE_SYS_ERROR);
            return tempSunApiResponse;
        }
    }

    // 更新聊天室好友信息 //建新群的时候把群成员的基本信息都传送过来
    @PostMapping("updateWeChatFriend.do")
    public SunApiResponse updateWeChatFriend(@RequestBody UpdateWeChatFriendInfo updateWeChatFriendInfo, HttpServletRequest request) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            return tempSunApiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            tempSunApiResponse.setCode(SunApiResponse.CODE_SYS_ERROR);
            return tempSunApiResponse;
        }
    }

    // 更新好友昵称
    @PostMapping("updateFriendNickname.do")
    public R updateFriendNickname() {
        try {
            return R.ok().data("updateFriendNickname");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error().message("网页走丢了，请刷新后重试。。。");
        }
    }

    // updateUsernameChatroomRel_自定义群昵称
    @PostMapping("updateUsernameChatroomRel.do")
    public R updateUsernameChatroomRel() {
        try {
            return R.ok().data("updateUsernameChatroomRel");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error().message("网页走丢了，请刷新后重试。。。");
        }
    }

    // ==========================================================================================================
    // 下面的都是有待实现的接口
    // 注意！！！ 会重复调用
    // 更新聊天室信息_20181207
    @PostMapping("updateWeChatroom.do")
    public R updateWeChatroom() {
        try {
            return R.ok().data("updateWeChatroom");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error().message("网页走丢了，请刷新后重试。。。");
        }
    }

    // 这个是客户端的日志, 记录了客户端的所有操作行为
    // 接受图片应该是在这里面
    //用户接受的微信消息和文件图片， 包含用户的微信id
    public static List<Long> ms_PrismRecordIdList = new LinkedList<Long>();

    @PostMapping("writePrismRecord.do")
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.REPEATABLE_READ, timeout = 30)
    public SunApiResponse writePrismRecord(@RequestBody WritePrismRecordInfo writePrismRecordInfo, HttpServletRequest request, HttpServletResponse response) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
            boolean b = true;
            log.info("将操作记录插入到数据库");
            PersonalNoPrismRecord currPR = writePrismRecordInfo.recordList.get(0);
            String getSql = "";
            Sql sql = new Sql();
            if (!VerifyUtils.isEmpty(currPR) && !VerifyUtils.isEmpty(currPR.getContent())) {
                currPR.setDb(DBRobotPrismrecord);
                prismrecordService.add(currPR);
            }
            getSql = DaoGetSql.getSql("SELECT * from " + DBWeChat + " where id = ? and operation_project_instance_id = ?", currPR.getLogicId(), G.ms_OPERATION_PROJECT_INSTANCE_ID);
            sql.setSql(getSql);
            PersonalNoOperationStockWechatAccount wechatAccount = wechatAccountService.getBySql(sql);
            log.info("判断个人号是否属于当前实例");
            if (VerifyUtils.isEmpty(wechatAccount)) {
                tempSunApiResponse.setCode(SunApiResponse.CODE_SYS_ERROR);
                G.requestException(DBRequestException, requestExceptionService, request, JsonObjectUtils.objectToJson(writePrismRecordInfo), "该实例下不存在当前微信号", JsonObjectUtils.objectToJson(tempSunApiResponse), SunApiResponse.CODE_SYS_ERROR);
                return tempSunApiResponse;
            }
            log.info("封装消息发送时需要的service对象");
            Map<String, Object> map = TaskUtiles.getMap(taskPeopleService, taskGroupService, noTaskService, taskService, messageService);
            if (!VerifyUtils.isEmpty(currPR) && VerifyUtils.collectionIsEmpty(currPR.getToUsernames())) {
                if (currPR.getType() == 40) {
                    getSql = DaoGetSql.getSql("SELECT * FROM " + DBMessageSendFail + " WHERE `personal_wx_id` = ? AND  `user_wx_id` = ? AND deleted = 0 ORDER BY id DESC LIMIT 0,1", wechatAccount.getWxId(), currPR.getFromUsername());
                    sql.setSql(getSql);
                    PersonalNoMessageSendFailure messageSendFailure = sendFailureService.getBySql(sql);
                    if (VerifyUtils.isEmpty(messageSendFailure)) {
                        getSql = "SELECT * FROM " + DBTaskGroup + " where tname = '" + wechatAccount.getWxId() + "发送回复消息" + currPR.getFromUsername() + "' and `task_order` = 9 order by id desc limit 0,1";
                        sql.setSql(getSql);
                        PersonalNoPhoneTaskGroup taskGroup = taskGroupService.getBySql(sql);
                        int taskId = -1;
                        if (!VerifyUtils.isEmpty(taskGroup)) {
                            taskId = taskGroup.getTaskSendId();
                        }
                        messageSendFailure = new PersonalNoMessageSendFailure();
                        messageSendFailure.setDeleted(0);
                        messageSendFailure.setFailureNum(1);
                        messageSendFailure.setPersonalWxId(wechatAccount.getWxId());
                        messageSendFailure.setUserWxId(currPR.getFromUsername());
                        TaskUtiles.toTask(map, wechatAccount.getWxId(), currPR.getFromUsername(), taskId, 15 * 60 * 1000);
                    } else {
                        messageSendFailure.setFailureNum(2);
                        messageSendFailure.setDeleted(1);
                        log.info("处理删除好友任务");
                        toDeleteFriendTask(currPR, wechatAccount);
                    }
                    messageSendFailure.setDb(DBMessageSendFail);
                    sendFailureService.add(messageSendFailure);
                }
            }
            log.info("判断问候消息，隔时消息，定时消息使用");
            PersonalNoTemp temp = null;
            log.info("判断是否存在任务好友，不存在则发送默认消息");
            if (!VerifyUtils.isEmpty(currPR) && !VerifyUtils.collectionIsEmpty(currPR.getToUsernames()) && !VerifyUtils.isEmpty(currPR.getFromUsername()) && VerifyUtils.isEmpty(currPR.getChatroom())) {
                getSql = DaoGetSql.getSql("SELECT * from " + DBPeople + " where personal_no_wx_id = ? and personal_friend_wx_id = ? and flag = ? and deleted = 0 order by be_friend_time desc limit 0,1", wechatAccount.getWxId(), currPR.getFromUsername(), 2);
                sql.setSql(getSql);
                PersonalNoPeople personalNoPeople = taskPeopleService.getBySql(sql);
                PersonalNoFriends friends = null;
                if (VerifyUtils.isEmpty(personalNoPeople)) {
                    log.info("非通道进来的好友");
                    log.info("判断是否是十分钟内新添加的好友");
                    getSql = DaoGetSql.getSql("SELECT * FROM " + DBFriends + " WHERE `personal_no_wx_id` = ? AND `user_wx_id` = ? AND deleted = 0 limit 0,1", currPR.getToUsernames().get(0), currPR.getFromUsername());
                    sql.setSql(getSql);
                    friends = friendsService.getBySql(sql);
                    if (VerifyUtils.isEmpty(friends) || new Date().getTime() - friends.getBeFriendTime().getTime() < 20 * 60 * 1000) {
                        log.info("判断是否需要回复默认消息,有则获取消息id，没有则为空");
                        getSql = DaoGetSql.getSql("SELECT * from " + DBSendMessage + " where personal_wx_id = ? and timing = ? limit 0,1", currPR.getToUsernames().get(0), 0);
                        sql.setSql(getSql);
                        PersonalNoSendMessage messageId = sendMessageService.getBySql(sql);
                        if (!VerifyUtils.isEmpty(messageId)) {
                            b = false;
                            getSql = DaoGetSql.getSql("SELECT * FROM " + DBTemp + " where personal_no_wx_id = ? and user_wx_id = ? and timing = ? and personal_no_send_message_id = ? limit 0,1", currPR.getToUsernames().get(0), currPR.getFromUsername(), 0, messageId.getId());
                            sql.setSql(getSql);
                            temp = tempService.getBySql(sql);
                            if (VerifyUtils.isEmpty(temp)) {
                                temp = new PersonalNoTemp();
                                temp.setPersonalNoWxId(currPR.getToUsernames().get(0));
                                temp.setUserWxId(currPR.getFromUsername());
                                temp.setTiming(0);
                                temp.setGreetings(1);
                                temp.setPersonalNoSendMessageId(messageId.getId());
                                temp.setDb(DBTemp);
                                tempService.add(temp);
                                TaskUtiles.toMessageTask(map, currPR.getToUsernames().get(0), currPR.getFromUsername(), messageId.getMessageId(), 0);
                            }
                        }
                    }
                }
                if (!currPR.getFromUsername().equals(currPR.getToUsernames().get(0))) {
                    if (VerifyUtils.isEmpty(friends) || new Date().getTime() - friends.getBeFriendTime().getTime() < 20 * 60 * 1000) {
                        getSql = DaoGetSql.getSql("SELECT * from " + DBSendMessage + " where personal_wx_id = ? and timing <> 0", currPR.getToUsernames().get(0));
                        sql.setSql(getSql);
                        List<PersonalNoSendMessage> sendMessagelist = sendMessageService.listBySql(sql);
                        for (PersonalNoSendMessage personalNoSendMessage : sendMessagelist) {
                            temp = null;
                            log.info("判断是否有操作 0：都发送  1：无操作发送   2：有操作发送");
                            getSql = DaoGetSql.getSql("SELECT * FROM " + DBTemp + " WHERE personal_no_wx_id = ? and user_wx_id = ? and personal_no_send_message_id = ? limit 0,1", currPR.getToUsernames().get(0), currPR.getFromUsername(), personalNoSendMessage.getId());
                            sql.setSql(getSql);
                            switch (personalNoSendMessage.getInitiativeFlag()) {
                                case 0:
                                    log.info("判断是隔时任务还是定时任务  1：隔时任务  2：定时任务");
                                    if (personalNoSendMessage.getTiming() == 1) {
                                        temp = tempService.getBySql(sql);
                                        if (VerifyUtils.isEmpty(temp)) {
                                            temp = addTemp(currPR.getToUsernames().get(0), currPR.getFromUsername(), personalNoSendMessage.getId(), null, personalNoSendMessage.getTimingTime(), 1, 1, null);
                                            TaskUtiles.toMessageTask(map, currPR.getToUsernames().get(0), currPR.getFromUsername(), personalNoSendMessage.getMessageId(), Integer.parseInt(personalNoSendMessage.getTimingTime()) * 1000);
                                        }

                                    } else if (personalNoSendMessage.getTiming() == 2) {
                                        temp = tempService.getBySql(sql);
                                        if (VerifyUtils.isEmpty(temp)) {
                                            temp = addTemp(currPR.getToUsernames().get(0), currPR.getFromUsername(), personalNoSendMessage.getId(), WebConst.getDateHourByString(personalNoSendMessage.getTimingTime()), null, 1, null, 1);
                                            Date dateHourByString = WebConst.getDateHourByString(personalNoSendMessage.getTimingTime());
                                            Long l = dateHourByString.getTime() - new Date().getTime();
                                            if (l.intValue() > 0) {
                                                TaskUtiles.toMessageTask(map, currPR.getToUsernames().get(0), currPR.getFromUsername(), personalNoSendMessage.getMessageId(), l.intValue());
                                            }
                                        }
                                    }
                                    break;
                                case 1:
                                    if (personalNoSendMessage.getTiming() == 1) {
                                        temp = tempService.getBySql(sql);
                                        if (VerifyUtils.isEmpty(temp)) {
                                            temp = addTemp(currPR.getToUsernames().get(0), currPR.getFromUsername(), personalNoSendMessage.getId(), null, personalNoSendMessage.getTimingTime(), 0, null, null);
                                        } else {
                                            if (!VerifyUtils.isEmpty(temp.getEverTime()) || temp.getEver() != 1) {
                                                temp.setGreetings(1);
                                                temp.setEver(1);
                                                temp.setDb(DBTemp);
                                                tempService.add(temp);
                                            }
                                        }
                                    } else if (personalNoSendMessage.getTiming() == 2) {
                                        temp = tempService.getBySql(sql);
                                        if (VerifyUtils.isEmpty(temp)) {
                                            long l = WebConst.getDateHourByString(personalNoSendMessage.getTimingTime()).getTime() - new Date().getTime();
                                            if (l > 0) {
                                                temp = addTemp(currPR.getToUsernames().get(0), currPR.getFromUsername(), personalNoSendMessage.getId(), WebConst.getDateHourByString(personalNoSendMessage.getTimingTime()), null, 0, null, null);
                                            }
                                        } else if (!VerifyUtils.isEmpty(temp.getTimingTime()) || temp.getTiming() != 1) {
                                            temp.setGreetings(1);
                                            temp.setTiming(1);
                                            temp.setDb(DBTemp);
                                            tempService.add(temp);
                                        }
                                    }
                                    break;
                                case 2:
                                    if (personalNoSendMessage.getTiming() == 1) {
                                        temp = tempService.getBySql(sql);
                                        if (VerifyUtils.isEmpty(temp)) {
                                            temp = addTemp(currPR.getToUsernames().get(0), currPR.getFromUsername(), personalNoSendMessage.getId(), null, personalNoSendMessage.getTimingTime(), 1, 2, null);
                                        } else {
                                            if (!VerifyUtils.isEmpty(temp.getEverTime()) || temp.getEver() != 1) {
                                                temp.setGreetings(1);
                                                temp.setEver(1);
                                                temp.setDb(DBTemp);
                                                tempService.add(temp);
                                                TaskUtiles.toMessageTask(map, currPR.getToUsernames().get(0), currPR.getFromUsername(), personalNoSendMessage.getMessageId(), Integer.parseInt(personalNoSendMessage.getTimingTime()) * 1000);
                                            }
                                        }
                                    } else if (personalNoSendMessage.getTiming() == 2) {
                                        temp = tempService.getBySql(sql);
                                        if (VerifyUtils.isEmpty(temp)) {
                                            temp = addTemp(currPR.getToUsernames().get(0), currPR.getFromUsername(), personalNoSendMessage.getId(), WebConst.getDateHourByString(personalNoSendMessage.getTimingTime()), null, 1, null, 2);
                                        } else {
                                            if (!VerifyUtils.isEmpty(temp.getTimingTime()) || temp.getTiming() != 1) {
                                                temp.setGreetings(1);
                                                temp.setTiming(1);
                                                temp.setDb(DBTemp);
                                                tempService.add(temp);
                                                Date dateHourByString = WebConst.getDateHourByString(personalNoSendMessage.getTimingTime());
                                                Long l = dateHourByString.getTime() - new Date().getTime();
                                                if (l.intValue() > 0) {
                                                    TaskUtiles.toMessageTask(map, currPR.getToUsernames().get(0), currPR.getFromUsername(), personalNoSendMessage.getMessageId(), l.intValue());
                                                }
                                            }
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                }
                log.info("判断是否是审核图片");
//                        b = toAuditTask(writePrismRecordInfo, currPR);
                log.info("开始处理个人号关键词回复：个人号关键词和任务关键词");
                getSql = DaoGetSql.getSql("SELECT * from " + DBPeople + " where personal_no_wx_id = ? and personal_friend_wx_id = ? and flag = ? and deleted = 0 order by be_friend_time desc", wechatAccount.getWxId(), currPR.getFromUsername(), 2);
                sql.setSql(getSql);
                List<PersonalNoPeople> peopleList = taskPeopleService.listBySql(sql);
                b = toKeyWordTask(currPR, peopleList);
                log.info("将用户消息转发给运营或将运营消息转发给用户");
//                    sendMessageToManager(currPR);
                log.info("将小程序转换成链接");
                getSmallParamUrl(currPR, wechatAccount);
                log.info("机器人回复");
                if (b) {
//                    toTuLingMessage(writePrismRecordInfo, currPR);
                }
            }
            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            return tempSunApiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            tempSunApiResponse.setCode(SunApiResponse.CODE_SYS_ERROR);
            G.requestException(DBRequestException, requestExceptionService, request, JsonObjectUtils.objectToJson(writePrismRecordInfo), "手机记录请求出错", JsonObjectUtils.objectToJson(tempSunApiResponse), SunApiResponse.CODE_SYS_ERROR);
            return tempSunApiResponse;
        }
    }

    private PersonalNoTemp addTemp(String personalNoWxId, String userWxId, Integer sendMessageid, Date timingTime, String everTime, Integer greetings, Integer ever, Integer timing) {
        PersonalNoTemp temp = new PersonalNoTemp();
        temp.setCreateTime(new Date());
        temp.setPersonalNoWxId(personalNoWxId);
        temp.setUserWxId(userWxId);
        temp.setPersonalNoSendMessageId(sendMessageid);
        temp.setGreetings(greetings);
        temp.setEver(ever);
        temp.setTiming(timing);
        temp.setTimingTime(timingTime);
        temp.setEverTime(everTime);
        temp.setDb(DBTemp);
        tempService.add(temp);
        return temp;
    }

    //机器人回复
    private void toTuLingMessage(@RequestBody WritePrismRecordInfo writePrismRecordInfo, PersonalNoPrismRecord currPR) {
        String getSql = DaoGetSql.getSql("SELECT * FROM " + DBWeChat + " where wx_id = ?", currPR.getFromUsername());
        Sql sql = new Sql(getSql);
        PersonalNoOperationStockWechatAccount wechatAccount = wechatAccountService.getBySql(sql);
        if (VerifyUtils.isEmpty(wechatAccount)) {
            String params = TuLingParam.getParams(writePrismRecordInfo.recordList.get(0).getContent(), "");
            String s = HttpClientUtil.sendPost("http://openapi.tuling123.com/openapi/api/v2", params);
            TuLingResult tuLingResult = JSONUtils.jsonToPojo(s, TuLingResult.class);
            log.info("机器人回复");
            if (tuLingResult.getIntent().getCode() == 10004 || tuLingResult.getIntent().getCode() == 10008) {
                PersonalNoPhoneTaskGroup taskGroup = new PersonalNoPhoneTaskGroup();
                taskGroup.setStatus("未下发");
                log.info("去重用");
                taskGroup.setCurrentRobotId(currPR.getToUsernames().get(0));
                taskGroup.setNextStep(1);
                taskGroup.setTotalStep(1);
                taskGroup.setTname(currPR.getToUsernames().get(0) + "机器人回复给" + currPR.getFromUsername());
                taskGroup.setCreateTime(new Date());
                taskGroup.setTaskOrder(0);
                taskGroup.setDb(DBTaskGroup);
                boolean save = taskGroupService.add(taskGroup) > 0;
                if (!save) {
                    log.error("插入机器人回复失败");
                    throw new RuntimeException("插入机器人回复失败");
                }
                PersonalNoPhoneTask task = new PersonalNoPhoneTask();
                task.setTaskGroupId(taskGroup.getId());
                task.setTaskType(100);
                task.setTname(currPR.getToUsernames().get(0) + "发送机器人回复消息给" + currPR.getFromUsername());
                task.setStatus("未下发");
                task.setStep(1);
                task.setRobotId(currPR.getFromUsername());
                task.setCreateTime(new Date());
                task.setContent("1&&" + tuLingResult.getResults().get(0).getValues().getText());
                task.setContentType("文字");
                task.setDb(DBTask);
                taskService.add(task);
            }
        }
    }

    //  得到小程序链接或语音消息链接
    private void getSmallParamUrl(PersonalNoPrismRecord curr, PersonalNoOperationStockWechatAccount wechatAccount) {
        String getSql = DaoGetSql.getSql("select wx_id from " + DBValueTable + " where type = ?", 0);
        Sql sql = new Sql(getSql);
        List<String> strings = valueTableService.listBySql(sql);
        if (!strings.contains(curr.getFromUsername())) {
            return;
        }
        if ((curr.getContent().contains("<msg>") && curr.getContent().contains("</msg>")) || (curr.getContent().contains("http://") && curr.getContent().contains(".amr"))) {
            PersonalNoPhoneTaskGroup taskGroup = new PersonalNoPhoneTaskGroup();
            taskGroup.setTaskOrder(0);
            taskGroup.setCreateTime(new Date());
            taskGroup.setTname(wechatAccount.getNickName() + "返回小程序网址给" + curr.getFromUsername());
            taskGroup.setTotalStep(1);
            taskGroup.setNextStep(1);
            taskGroup.setCurrentRobotId(curr.getToUsernames().get(0));
            taskGroup.setStatus("未下发");
            taskGroup.setDb(DBTaskGroup);
            taskGroupService.add(taskGroup);
            PersonalNoPhoneTask task = new PersonalNoPhoneTask();
            task.setTaskGroupId(taskGroup.getId());
            task.setStep(1);
            task.setTaskType(SunTaskType.FRIEND_SEND_MSG);
            task.setRobotId(curr.getFromUsername());
            task.setStatus("未下发");
            task.setTname(wechatAccount.getNickName() + "返回小程序网址给" + curr.getFromUsername());
            task.setCreateTime(new Date());
            task.setContent(curr.getContent());
            task.setContentType("文字");
            task.setDb(DBTask);
            taskService.add(task);
        }
    }

    //     * 给微信好友贴标签任务
//     *
//     * @param currPR
//     * @param taskById
    private void toAddFriendLableTask(String userWxId, List<PersonalNoTaskLable> taskLableList, PersonalNoOperationStockWechatAccount wechatAccount, Integer time) {
        if (!VerifyUtils.collectionIsEmpty(taskLableList)) {
            PersonalNoPhoneTaskGroup taskGroup = new PersonalNoPhoneTaskGroup();
            taskGroup.setTaskOrder(0);
            taskGroup.setCreateTime(new Date(new Date().getTime() + time));
            taskGroup.setTname(wechatAccount.getNickName() + "给好友" + userWxId + "添加标签");
            taskGroup.setTotalStep(taskLableList.size());
            taskGroup.setNextStep(1);
            taskGroup.setCurrentRobotId(wechatAccount.getWxId());
            taskGroup.setStatus("未下发");
            taskGroup.setDb(DBTaskGroup);
            boolean save = taskGroupService.add(taskGroup) > 0;
            if (save) {
                for (int j = 0; j < taskLableList.size(); j++) {
                    PersonalNoPhoneTask task = new PersonalNoPhoneTask();
                    task.setStep(j + 1);
                    task.setTaskGroupId(taskGroup.getId());
                    task.setTaskType(SunTaskType.FRIEND_ADD_LABEL);
                    task.setRobotId(userWxId);
                    task.setStatus("未下发");
                    task.setTname(wechatAccount.getNickName() + "给好友" + userWxId + "添加标签" + taskLableList.get(j).getLableName());
                    task.setCreateTime(new Date());
                    task.setContent(taskLableList.get(j).getLableName());
                    task.setDb(DBTask);
                    boolean save1 = taskService.add(task) > 0;
                    if (!save1) {
                        log.error("添加粉丝标签任务失败");
                        throw new RuntimeException("添加粉丝标签任务失败");
                    }
                }
            }
        }
    }

    /**
     * 加满好友消息发送
     * @param wechatAccount
     */
    private void toFriendsMaxTask(PersonalNoOperationStockWechatAccount wechatAccount) {
        String getSql = DaoGetSql.getSql("select wx_id from " + DBValueTable + " where type = ? and deleted = 0", 0);
        Sql sql = new Sql(getSql);
        List<String> tiList = valueTableService.listBySql(sql);
        if (VerifyUtils.collectionIsEmpty(tiList)) {
            return;
        }
        getSql = DaoGetSql.getById(DBValueTable, 21);
        sql.setSql(getSql);
        PersonalNoValueTable bySql = valueTableService.getBySql(sql);
        if (VerifyUtils.isEmpty(bySql)) {
            return;
        }
        for (String s : tiList) {
            PersonalNoPhoneTaskGroup taskGroup = new PersonalNoPhoneTaskGroup();
            taskGroup.setTaskOrder(0);
            taskGroup.setCreateTime(new Date());
            taskGroup.setTname(bySql.getNickName() + "给好友" + s + "发送加满好友消息");
            taskGroup.setTotalStep(1);
            taskGroup.setNextStep(1);
            taskGroup.setCurrentRobotId(bySql.getWxId());
            taskGroup.setStatus("未下发");
            taskGroup.setDb(DBTaskGroup);
            boolean save = taskGroupService.add(taskGroup) > 0;
            if (save) {
                PersonalNoPhoneTask task = new PersonalNoPhoneTask();
                task.setStep(1);
                task.setTaskGroupId(taskGroup.getId());
                task.setTaskType(SunTaskType.FRIEND_SEND_MSG);
                task.setRobotId(s);
                task.setStatus("未下发");
                task.setTname(bySql.getNickName() + "给好友" + s + "发送加满好友消息");
                task.setCreateTime(new Date());
                task.setContentType("文字");
                task.setContent(G.ms_currProjectInstanceName+wechatAccount.getNickName()+"已经加满好友，今天日期为"+WebConst.getNowDate(new Date()));
                task.setDb(DBTask);
                boolean save1 = taskService.add(task) > 0;
            }
        }

    }

    //     * 删除好友任务
//     *
//     * @param writePrismRecordInfo
    private void toDeleteFriendTask(PersonalNoPrismRecord curr, PersonalNoOperationStockWechatAccount wechatAccount) {
        PersonalNoPhoneTaskGroup taskGroup = new PersonalNoPhoneTaskGroup();
        taskGroup.setCreateTime(new Date());
        taskGroup.setTaskOrder(0);
        taskGroup.setTname(wechatAccount.getNickName() + "删除好友" + curr.getFromUsername());
        taskGroup.setTotalStep(1);
        taskGroup.setNextStep(1);
        taskGroup.setCurrentRobotId(wechatAccount.getWxId());
        taskGroup.setStatus("未下发");
        taskGroup.setDb(DBTaskGroup);
        boolean save = taskGroupService.add(taskGroup) > 0;
        if (save) {
            PersonalNoPhoneTask task = new PersonalNoPhoneTask();
            task.setStep(1);
            task.setTname(wechatAccount.getNickName() + "删除好友" + curr.getFromUsername());
            task.setTaskType(SunTaskType.DELETE_FRIEND);
            task.setRobotId(curr.getFromUsername());
            task.setStatus("未下发");
            task.setCreateTime(new Date());
            task.setTaskGroupId(taskGroup.getId());
            task.setDb(DBTask);
            taskService.add(task);
        }
        log.info("删除任务粉丝数据");
        String delSql = DaoGetSql.getSql("UPDATE " + DBPeople + " set deleted = 1 where personal_no_wx_id = ? and personal_friend_wx_id = ?", wechatAccount.getWxId(), curr.getFromUsername());
        Sql sql = new Sql(delSql);
        taskPeopleService.deleteBySql(sql);
        log.info("删除个人号好友数据");
        delSql = DaoGetSql.getSql("UPDATE " + DBFriends + " set deleted = 1 where personal_no_wx_id = ? and user_wx_id = ?", wechatAccount.getWxId(), curr.getFromUsername());
        sql.setSql(delSql);
        friendsService.deleteBySql(sql);
    }

    //     *
//     * 关键字任务
//     *
//     * @param writePrismRecordInfo
//     * @param currPR
    private boolean toKeyWordTask(PersonalNoPrismRecord currPR, List<PersonalNoPeople> personalNoPeople) {
        log.info("开始匹配任务和关键词");
        log.info("根据任务id取得所有的关键词");
        Integer keywordId = null;
        String content = currPR.getContent();
        if (VerifyUtils.isEmpty(content)) {
            return true;
        }
        boolean flag = true;
        String getSql = "";
        Sql sql = new Sql();
        if (!VerifyUtils.collectionIsEmpty(personalNoPeople)) {
            for (PersonalNoPeople people : personalNoPeople) {
                getSql = DaoGetSql.getSql("SELECT * FROM  " + DBTaskAndKeyword + " WHERE `task_id` = ? AND  `keyword_name` = ? limit 0,1", people.getPersonalTaskId(), content);
                sql.setSql(getSql);
                PersonalNoTaskAndKeyword taskAndKeyword = taskAndKeywordService.getBySql(sql);
                if (!VerifyUtils.isEmpty(taskAndKeyword)) {
                    keywordId = taskAndKeyword.getKeywordId();
                    flag = addKeyWordTask(currPR, keywordId);
                }
            }
        }
        if (flag) {
            getSql = DaoGetSql.getSql("SELECT * FROM  " + DBNoAndKeyword + " WHERE `personal_no_wx_id` = ? AND `keyword_name` = ? limit 0,1", currPR.getToUsernames().get(0), content);
            sql.setSql(getSql);
            PersonalNoAndKeyword noAndKeyword = personalNoAndKeywordService.getBySql(sql);
            if (!VerifyUtils.isEmpty(noAndKeyword)) {
                keywordId = noAndKeyword.getKeywordId();
                addKeyWordTask(currPR, keywordId);
            }
        }
        return flag;
    }

    //    下发关键词任务
    private boolean addKeyWordTask(PersonalNoPrismRecord currPR, Integer keywordId) {
        if (!VerifyUtils.isEmpty(keywordId)) {
            String getSql = DaoGetSql.getSql("SELECT * FROM  " + DBKeywordContent + " WHERE `personal_no_keyword_id` = ? AND `deleted` = 0", keywordId);
            Sql sql = new Sql(getSql);
            List<PersonalNoKeywordContent> keywordContentList = keywordContentServicec.listBySql(sql);
            log.info("触发任务关键字");
            PersonalNoPhoneTaskGroup taskGroup = new PersonalNoPhoneTaskGroup();
            taskGroup.setStatus("未下发");
            taskGroup.setCurrentRobotId(currPR.getToUsernames().get(0));
            taskGroup.setNextStep(1);
            taskGroup.setTotalStep(keywordContentList.size());
            taskGroup.setTname(currPR.getToUsernames().get(0) + "发送任务关键字给" + currPR.getFromUsername());
            taskGroup.setCreateTime(new Date());
            taskGroup.setTaskOrder(0);
            taskGroup.setDb(DBTaskGroup);
            boolean save = taskGroupService.add(taskGroup) > 0;
            if (!save) {
                log.error("插入任务组失败");
                throw new RuntimeException("插入任务组失败");
            }
            for (int j = 0; j < keywordContentList.size(); j++) {
                PersonalNoPhoneTask task = new PersonalNoPhoneTask();
                task.setTaskGroupId(taskGroup.getId());
                if ("邀请入群".equals(keywordContentList.get(j).getContentType())) {
                    task.setTaskGroupId(taskGroup.getId());
                    task.setStatus("未下发");
                    task.setTname(currPR.getToUsernames().get(0) + "发送入群邀请给" + currPR.getFromUsername());
                    task.setTaskType(1102);
                    task.setContent(keywordContentList.get(j).getContent());
                    task.setContentType(keywordContentList.get(j).getContentType());
                    task.setRobotId(currPR.getFromUsername());
                    task.setStep(j + 1);
                } else {
                    task.setTaskType(100);
                    task.setTname(currPR.getToUsernames().get(0) + "发送关键词消息给" + currPR.getFromUsername());
                    task.setStatus("未下发");
                    task.setStep(j + 1);
                    task.setRobotId(currPR.getFromUsername());
                    task.setCreateTime(new Date());
                    task.setContent(keywordContentList.get(j).getContent());
                    task.setContentType(keywordContentList.get(j).getContentType());
                }
                task.setDb(DBTask);
                boolean save1 = taskService.add(task) > 0;
                if (!save1) {
                    log.error("插入关键词回复任务失败");
                    throw new RuntimeException("插入关键词回复任务失败");
                }
            }
        }
        return false;
    }

    //     *
//     * 审核任务
//     *
//     * @param writePrismRecordInfo
//     * @param currPR
//    private boolean toAuditTask( WritePrismRecordInfo writePrismRecordInfo, PersonalNoPrismRecord currPR) {
//        PersonalNoPeople personalNoPeople;
//        if (writePrismRecordInfo.recordList.get(0).getContent().contains("/robotFiles/imgMsg") || writePrismRecordInfo.recordList.get(0).getContent().contains("http://")) {
//            personalNoPeople = taskPeopleService.getByPersonalIdAndUserId(currPR.getToUsernames().get(0), currPR.getFromUsername(), 2);
//            if (!VerifyUtils.isEmpty(personalNoPeople)) {
//                boolean b2 = taskPeopleService.updateFlagById(personalNoPeople.getId(), 3);
//                if (!b2) {
//                    log.info("更新任务粉丝为已审核通过状态失败");
//                    throw new RuntimeException("更新任务粉丝为已审核通过状态失败");
//                }
//                log.info("根据任务id获取要发送的任务信息");
//                PersonalNoTask taskById = noTaskService.getTaskById(personalNoPeople.getPersonalTaskId());
//                if (null == taskById.getMessage()) {
//                    return true;
//                }
//                log.info("将回复信息转换为任务任务组");
//                PersonalNoPhoneTaskGroup taskGroup = new PersonalNoPhoneTaskGroup();
//                taskGroup.setTaskOrder(9);
//                taskGroup.setCreateTime(new Date());
//                taskGroup.setNextStep(1);
//                taskGroup.setTotalStep(1);
//                taskGroup.setCurrentRobotId(currPR.getToUsernames().get(0));
//                taskGroup.setStatus("未下发");
//                taskGroup.setTname(currPR.getToUsernames().get(0) + "发送回复消息" + currPR.getFromUsername());
//                boolean save = taskGroupService.insert(taskGroup);
//                if (!save) {
//                    log.error("添加到任务组失败");
//                    throw new RuntimeException("添加到任务组失败");
//                }
//                log.info("开始添加审核回复任务");
//                PersonalNoPhoneTask task = new PersonalNoPhoneTask();
//                task.setContent(taskById.getMessage());
//                task.setContentType("文字");
//                task.setStep(1);
//                task.setTaskType(100);
//                task.setTname(currPR.getToUsernames().get(0) + "发送自动回复审核消息给" + currPR.getFromUsername());
//                task.setTaskGroupId(taskGroup.getId());
//                task.setRobotId(currPR.getFromUsername());
//                task.setCreateTime(new Date());
//                task.setStatus("未下发");
//                boolean save1 = taskService.insert(task);
//                if (!save1) {
//                    log.error("插入审核回复任务失败");
//                    throw new RuntimeException("插入审核回复任务失败");
//                }
//                return false;
//            }
//        }
//        return false;
//    }


//    //转发消息给运营人员
//    private void sendMessageToManager(@RequestBody WritePrismRecordInfo writePrismRecordInfo, PersonalNoOperationStockWechatAccount wechatAccount) {
//        if (!VerifyUtils.isEmpty(writePrismRecordInfo.recordList.get(0).getToUsernames().get(0)) && !VerifyUtils.isEmpty(writePrismRecordInfo.recordList.get(0).getFromUsername())) {
//                if (VerifyUtils.isEmpty(byWxId.getSuperId())) {
//                    return;
//                }
//                PersonalNoSuperuesr byId = superuesrService.selectById(byWxId.getSuperId());
//                if (VerifyUtils.isEmpty(byId)) {
//                    return;
//                }
//                PersonalNoPhoneTaskGroup taskGroup = new PersonalNoPhoneTaskGroup();
//                if (byId.getWxId().equals(writePrismRecordInfo.recordList.get(0).getFromUsername())) {
//                    log.info("将用户消息转发给某个微信好友");
//                    String[] split = writePrismRecordInfo.recordList.get(0).getContent().split("@");
//                    if (split != null && split.length > 1) {
//                        taskGroup.setTaskOrder(0);
//                        taskGroup.setCreateTime(new Date());
//                        taskGroup.setTname(writePrismRecordInfo.recordList.get(0).getToUsernames().get(0) + "转发消息给用户" + split[0]);
//                        taskGroup.setNextStep(1);
//                        taskGroup.setTotalStep(split.length - 1);
//                        taskGroup.setCurrentRobotId(writePrismRecordInfo.recordList.get(0).getToUsernames().get(0));
//                        taskGroup.setStatus("未下发");
//                        boolean save = taskGroupService.insert(taskGroup);
//                        if (save) {
//                            for (int i = 1; i < split.length; i++) {
//                                PersonalNoPhoneTask task = new PersonalNoPhoneTask();
//                                task.setTaskGroupId(taskGroup.getId());
//                                task.setContent(split[i]);
//                                if (split[i].contains("/group1/M00")) {
//                                    task.setContentType("图片");
//                                } else {
//                                    task.setContentType("文字");
//                                }
//                                task.setStep(i);
//                                task.setStatus("未下发");
//                                task.setRobotId(split[0]);
//                                task.setTaskType(100);
//                                task.setCreateTime(new Date());
//                                taskService.insert(task);
//                            }
//                        }
//                    }
//                } else {
//                    log.info("将消息转发给管理员");
//                    taskGroup.setTaskOrder(0);
//                    taskGroup.setCreateTime(new Date());
//                    taskGroup.setTname(writePrismRecordInfo.recordList.get(0).getToUsernames().get(0) + "转发消息给运营");
//                    taskGroup.setNextStep(1);
//                    taskGroup.setTotalStep(1);
//                    taskGroup.setCurrentRobotId(writePrismRecordInfo.recordList.get(0).getToUsernames().get(0));
//                    taskGroup.setStatus("未下发");
//                    boolean save = taskGroupService.insert(taskGroup);
//                    if (save) {
//                        PersonalNoPhoneTask task = new PersonalNoPhoneTask();
//                        task.setTaskGroupId(taskGroup.getId());
//                        PersonalNoUser byWxId1 = userService.getByWxId(writePrismRecordInfo.recordList.get(0).getFromUsername());
//                        String nickName = "未知";
//                        if (byWxId1 != null) {
//                            nickName = byWxId1.getNickName();
//                        }
//                        task.setContent(noService.getByWxId(writePrismRecordInfo.recordList.get(0).getToUsernames().get(0)).getNickname() +
//                                "收到\n昵称：" + nickName +
//                                " \n微信id：" + writePrismRecordInfo.recordList.get(0).getFromUsername() + "  的消息，\n内容为: " + writePrismRecordInfo.recordList.get(0).getContent());
//                        task.setContentType("文字");
//                        task.setStep(1);
//                        task.setStatus("未下发");
//                        task.setRobotId(byId.getWxId());
//                        task.setTaskType(100);
//                        task.setCreateTime(new Date());
//                        taskService.insert(task);
//                    }
//                }
//            }
//        }
//    }

    // 增加黑名单成员
    @PostMapping("addGlobalBlockUser.do")
    public SunApiResponse addGlobalBlockUser(@RequestBody AddGlobalBlockUserInfo addGlobalBlockUserInfo, HttpServletRequest request) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
            return tempSunApiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return tempSunApiResponse;
        }
    }

    // 当收到加好友请求的时候, 通过这个接口通知服务器
    @PostMapping("addFriendRequest.do")
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.REPEATABLE_READ, timeout = 30)
    public SunApiResponse addFriendRequest(@RequestBody FriendRequestInfoWarpper friendRequestInfoWarpper, HttpServletRequest request, HttpServletResponse response) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
            FriendRequestInfo friendRequestInfo = friendRequestInfoWarpper.friendRequestInfo;
            log.info(friendRequestInfoWarpper);
            String getSql = DaoGetSql.getSql("SELECT * from " + DBWeChat + " where id = ? and operation_project_instance_id = ?", friendRequestInfoWarpper.logicId, G.ms_OPERATION_PROJECT_INSTANCE_ID);
            Sql sql = new Sql(getSql);
            PersonalNoOperationStockWechatAccount byId = wechatAccountService.getBySql(sql);
            if (VerifyUtils.isEmpty(byId)) {
                tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
                G.requestException(DBRequestException, requestExceptionService, request, JsonObjectUtils.objectToJson(friendRequestInfoWarpper), "该实例下不存在此微信号", JsonObjectUtils.objectToJson(tempSunApiResponse), SunApiResponse.CODE_SUCCESS);
                return tempSunApiResponse;
            }
            getSql = DaoGetSql.getSql("SELECT * from " + DBBlack + " WHERE wx_id = ?", friendRequestInfo.getUsername());
            sql.setSql(getSql);
            PersonalNoBlacklist blacklist = blacklistService.getBySql(sql);
            Integer taskId = null;
            if (!VerifyUtils.isEmpty(byId) && byId.getOperationProjectInstanceId() == G.ms_OPERATION_PROJECT_INSTANCE_ID) {
                if (!VerifyUtils.isEmpty(blacklist)) {
                    tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
                    return tempSunApiResponse;
                }
                log.info("不在黑名单内，开始处理");
                PersonalNoUser user = null;
                log.info("处理用户表的数据");
                getSql = DaoGetSql.getSql("SELECT * FROM  " + DBUser + "  WHERE nick_name = ? and (wx_id is null or wx_id = ?) order by create_time desc limit 0,1", friendRequestInfo.getNickname(), friendRequestInfo.getUsername());
                sql.setSql(getSql);
                user = userService.getBySql(sql);
                if (!VerifyUtils.isEmpty(user)) {
                    getSql = DaoGetSql.getSql("update " + DBUser + " set wx_id = ? where id = ?", friendRequestInfo.getUsername(), user.getId());
                    sql.setSql(getSql);
                    userService.updateBySql(sql);
                    log.info("处理用户粉丝表的数据");
                    log.info("处理用户粉丝表的数据");
                    getSql = DaoGetSql.getSql("SELECT * from " + DBPeople + " where personal_no_wx_id = ? and personal_friend_nick_name = ? and flag = 0 order by be_friend_time desc limit 0,1;", byId.getWxId(), user.getUnionid());
                    sql.setSql(getSql);
                    PersonalNoPeople people = taskPeopleService.getBySql(sql);
                    if (!VerifyUtils.isEmpty(people)) {
                        taskId = people.getPersonalTaskId();
                        people.setPersonalFriendWxId(friendRequestInfo.getUsername());
                        people.setDb(DBPeople);
                        taskPeopleService.add(people);
                    }

                }
                log.info("获取添加好友的间隔时间");
                getSql = DaoGetSql.getById(DBValueTable, 4);
                sql.setSql(getSql);
                PersonalNoValueTable value4 = valueTableService.getBySql(sql);
                int addFriendsIntervalTime = Integer.parseInt(VerifyUtils.isEmpty(value4.getValue()) ? "600000" : value4.getValue()) * 1000;
                log.info("获取添加好友总个数");
                getSql = DaoGetSql.getById(DBValueTable, 5);
                sql.setSql(getSql);
                PersonalNoValueTable value5 = valueTableService.getBySql(sql);
                int addFriendsNum = Integer.parseInt(VerifyUtils.isEmpty(value5.getValue()) ? "50" : value5.getValue());
                log.info("获取今天添加的好友人数");
                getSql = DaoGetSql.getSql("SELECT count(*) from " + DBFriends + " where personal_no_wx_id = ? and be_friend_time BETWEEN ? and ? and deleted = 0", byId.getWxId(), WebConst.getTodayZeroTime(new Date()), WebConst.getTomorrowZeroTime(new Date()));
                sql.setSql(getSql);
                Long count = friendsService.countBySql(sql);
                log.info("标识今天是否可以加好友");
                count = 51L;
                if (count.intValue() <= addFriendsNum) {
                    log.info("今天添加的好友数小于50，添加好友");
                    log.info("判断是否已经存在任务");
                    getSql = DaoGetSql.getSql("SELECT * FROM " + DBTaskGroup + " WHERE current_robot_id = ? AND tname = ? AND status = '未下发' and create_time >= ? ORDER BY id DESC LIMIT 0,1", byId.getWxId(), "" + byId.getNickName() + "添加好友" + friendRequestInfo.getUsername(), WebConst.getNowDate(new Date(new Date().getTime() - 600000)));
                    sql.setSql(getSql);
                    PersonalNoPhoneTaskGroup taskGroup1 = taskGroupService.getBySql(sql);
                    if (VerifyUtils.isEmpty(taskGroup1)) {
                        log.info("添加好友任务");
                        log.info("获取今天最新的添加好友任务");
                        getSql = DaoGetSql.getSql("SELECT * FROM " + DBTaskGroup + " WHERE `tname` LIKE '%" + byId.getNickName() + "添加%' AND `create_time` > ? limit 0,1", WebConst.getNowDate(new Date(new Date().getTime() - addFriendsIntervalTime)));
                        sql.setSql(getSql);
                        PersonalNoPhoneTaskGroup taskGroup = taskGroupService.getBySql(sql);
                        if (VerifyUtils.isEmpty(taskGroup)) {
                            log.info("不存在，则按当前时间算");
                            theDate = new Date();
                        } else {
                            theDate = new Date(theDate.getTime() + addFriendsIntervalTime);
                            log.info("存在，在得到的任务时间基础上加10分钟");
                        }
                        toAddFriendTask(friendRequestInfo, byId, theDate, taskId);
                    }
                } else {
                    toFriendsMaxTask(byId);
                    log.info("今天添加的好友数大于50，判断明天添加的好友人数");
                    log.info("将时间设置为明天时间");
                    theDate = WebConst.getDateHourByString(WebConst.getTomorrowEightTime(new Date()));
                    while (true) {
                        log.info("判断明天添加好友的人数，保证每个个人号每天只有50人添加好友");
                        getSql = DaoGetSql.getSql("SELECT count(*) from " + DBFriends + " where personal_no_wx_id = ? and be_friend_time BETWEEN ? and ?", byId.getWxId(), WebConst.getTodayZeroTime(theDate), WebConst.getTomorrowZeroTime(theDate));
                        sql.setSql(getSql);
                        count = friendsService.countBySql(sql);
                        log.info("如过明天添加好友数大于50，再延迟一天");
                        if (count.intValue() > 50) {
                            log.info("时间标识再加一天");
                            theDate = WebConst.getDateHourByString(WebConst.getTomorrowEightTime(taskDate));
                        } else {
                            break;
                        }
                    }
                    log.info("判断是否已经存在任务");
                    getSql = DaoGetSql.getSql("SELECT * FROM " + DBTaskGroup + " WHERE current_robot_id = ? AND tname = ? AND status = '未下发' and create_time >= ? ORDER BY id DESC LIMIT 0,1", byId.getWxId(), "" + byId.getNickName() + "添加好友" + friendRequestInfo.getUsername(), WebConst.getNowDate(new Date(theDate.getTime() - 600000)));
                    sql.setSql(getSql);
                    PersonalNoPhoneTaskGroup taskGroup1 = taskGroupService.getBySql(sql);
                    if (VerifyUtils.isEmpty(taskGroup1)) {
                        log.info("按时间节点间隔下发接受好友请求任务");
                        getSql = DaoGetSql.getSql("SELECT * FROM " + DBTaskGroup + " WHERE `tname` LIKE '%" + byId.getNickName() + "添加好友%' AND `create_time` >= ? order by id desc limit 0,1", WebConst.getNowDate(theDate));
                        sql.setSql(getSql);
                        PersonalNoPhoneTaskGroup taskGroup = taskGroupService.getBySql(sql);
                        if (VerifyUtils.isEmpty(taskGroup)) {
                            log.info("不存在，则按当前时间算");
                        } else {
                            theDate = new Date(taskGroup.getCreateTime().getTime() + addFriendsIntervalTime);
                            log.info("存在，在得到的任务时间基础上加10分钟");
                        }
                    }
                    toAddFriendTask(friendRequestInfo, byId, theDate, taskId);
                }

                log.info("处理好友表数据");
                getSql = DaoGetSql.getSql("SELECT * from " + DBFriends + " where personal_no_wx_id = ? and user_wx_id = ? and deleted = 0 limit 0,1", byId.getWxId(), friendRequestInfo.getUsername());
                sql.setSql(getSql);
                PersonalNoFriends friends = friendsService.getBySql(sql);
                if (VerifyUtils.isEmpty(friends)) {
                    log.info("处理好友信息的数据");
                    friends = new PersonalNoFriends();
                    friends.setPersonalNoId(friendRequestInfoWarpper.logicId);
                    friends.setUserWxId(friendRequestInfo.getUsername());
                    friends.setPersonalNoWxId(byId.getWxId());
                    friends.setBeFriendTime(theDate);
                    friends.setDb(DBFriends);
                    friendsService.add(friends);
                }


                log.info("处理注册个人号，暂时未用，需要一个任务和一个特定个人号单独处理上线个人号信息，主要为了拿到头像，解决昵称容易冲突的问题");
                getSql = DaoGetSql.getSql("select wx_id from " + DBValueTable + " where type = ?", 3);
                sql = new Sql(getSql);
                List<String> list = valueTableService.listBySql(sql);
                if (!VerifyUtils.isEmpty(list) && list.contains(byId.getWxId())) {
                    getSql = DaoGetSql.getSql("SELECT * FROM  " + DBUser + "  WHERE  wx_id = ? limit 0,1", friendRequestInfo.getUsername());
                    sql.setSql(getSql);
                    user = userService.getBySql(sql);
                    if (!VerifyUtils.isEmpty(user.getWxId())) {
                        getSql = DaoGetSql.getSql("SELECT * from " + DBWeChat + " where wx_id = ? and operation_project_instance_id = ?", friendRequestInfo.getUsername(), G.ms_OPERATION_PROJECT_INSTANCE_ID);
                        sql = new Sql(getSql);
                        PersonalNoOperationStockWechatAccount temp = wechatAccountService.getBySql(sql);
                        if (VerifyUtils.isEmpty(temp)) {
                            temp = new PersonalNoOperationStockWechatAccount();
                        }
                        temp.setWxId(user.getWxId());
                        temp.setAssignPhone(user.getPhone());
                        temp.setAvatar(user.getHeadPortrait());
                        temp.setNickName(user.getNickName());
                        temp.setCity(user.getAddress());
                        temp.setOperationProjectInstanceId(G.ms_OPERATION_PROJECT_INSTANCE_ID);
                        temp.setStatus("正常");
                        temp.setDb(DBWeChat);
                        wechatAccountService.add(temp);
                    }
                }
            }
            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            return tempSunApiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            tempSunApiResponse.setCode(SunApiResponse.CODE_SYS_ERROR);
            return tempSunApiResponse;
        }
    }

    //添加好友任务
    private void toAddFriendTask(FriendRequestInfo friendRequestInfo, PersonalNoOperationStockWechatAccount byId, Date date, Integer taskId) {
        String getsql = "SELECT * FROM " + DBTaskGroup + " where tname LIKE '%" + byId.getNickName() + "添加好友" + friendRequestInfo.getUsername() + "%' and create_time > '" + WebConst.getNowDate(new Date(new Date().getTime() - 600000)) + "' and status = '未下发' order by id desc limit 0,1";
        PersonalNoPhoneTaskGroup taskGroup = taskGroupService.getBySql(new Sql(getsql));
        if (!VerifyUtils.isEmpty(taskGroup)) {
            return;
        }
        taskGroup = new PersonalNoPhoneTaskGroup();
        taskGroup.setCreateTime(new Date());
        taskGroup.setTname(byId.getNickName() + "添加好友" + friendRequestInfo.getUsername());
        taskGroup.setCurrentRobotId(byId.getWxId());
        taskGroup.setStatus("未下发");
        taskGroup.setTotalStep(1);
        taskGroup.setNextStep(1);
        taskGroup.setTaskOrder(10);
        taskGroup.setCreateTime(date);
        taskGroup.setDb(DBTaskGroup);
        taskGroup.setTaskSendId(taskId);
        boolean save2 = taskGroupService.add(taskGroup) > 0;
        if (!save2) {
            log.info("添加好友任务组失败");
            throw new RuntimeException("添加好友任务组失败");
        }
        PersonalNoPhoneTask task = new PersonalNoPhoneTask();
        task.setStatus("未下发");
        task.setRobotId(friendRequestInfo.getUsername());
        task.setCreateTime(new Date());
        task.setTname(byId.getNickName() + "添加" + friendRequestInfo.getUsername() + "为好友");
        task.setStep(1);
        task.setTaskGroupId(taskGroup.getId());
        task.setTaskType(SunTaskType.FRIEND_ACCEPT_REQUEST);
        String s = JsonObjectUtils.objectToJson(friendRequestInfo);
        task.setTaskJson(s);
        task.setContent(friendRequestInfo.getUsername());
        task.setDb(DBTask);
        boolean save3 = taskService.add(task) > 0;
        if (!save3) {
            log.info("添加好友任务失败");
            throw new RuntimeException("添加好友任务失败");
        }
    }

    // 向服务器报告错误
    @PostMapping("reportErrors.do")
    public SunApiResponse reportErrors(@RequestBody ReportErrorsInfo reportErrorsInfo) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
            log.error("*********************************************************************************************");
            log.error("手机报错了！！！");
            PersonalNoPhoneRequestException phoneRequestException = new PersonalNoPhoneRequestException();
            phoneRequestException.setCreateTime(new Date());
            phoneRequestException.setRequestBody(JsonObjectUtils.objectToJson(reportErrorsInfo));
            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            phoneRequestException.setResponseBody(JsonObjectUtils.objectToJson(tempSunApiResponse));
            phoneRequestException.setStatusCode(SunApiResponse.CODE_SUCCESS);
            phoneRequestException.setDb(DBPhoneRequestException);
            phoneRequestExceptionService.add(phoneRequestException);
            return tempSunApiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            tempSunApiResponse.setCode(SunApiResponse.CODE_SYS_ERROR);
            return tempSunApiResponse;
        }
    }

    // ======================================================================================
    // 下面的都没有启用
    // 更新自身微信二维码
    @PostMapping("updateSelfQrCodeUrl.do")
    public SunApiResponse updateSelfQrCodeUrl(@RequestBody UpdateSelfQrCodeUrlInfo updateSelfQrCodeUrlInfo, HttpServletRequest request) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            log.info("去重");
            return tempSunApiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return tempSunApiResponse;
        }
    }

    // 上传聊天室管理员列表
    @PostMapping("uploadAdminList.do")
    public SunApiResponse uploadAdminList(@RequestBody UploadAdminListInfo uploadAdminListInfo, HttpServletRequest request) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            return tempSunApiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return tempSunApiResponse;
        }
    }

    // 上传好友列表
    @PostMapping("uploadFriendList.do")
    public SunApiResponse uploadFriendList(@RequestBody UploadFriendListInfo uploadFriendListInfo) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
            String getSql = DaoGetSql.getSql("SELECT * from " + DBWeChat + " where wx_id = ? and operation_project_instance_id = ?", uploadFriendListInfo.username, G.ms_OPERATION_PROJECT_INSTANCE_ID);
            Sql sql = new Sql(getSql);
            PersonalNoOperationStockWechatAccount wechatAccount = wechatAccountService.getBySql(sql);
            if (!VerifyUtils.isEmpty(wechatAccount)) {
                log.info("处理个人号好友表");
                getSql = DaoGetSql.getSql("SELECT * from " + DBFriends + " where personal_no_wx_id = ? and deleted = 0", uploadFriendListInfo.username);
                sql.setSql(getSql);
                List<PersonalNoFriends> friends = friendsService.listBySql(sql);
                Iterator<PersonalNoFriends> iterator = friends.iterator();
                PersonalNoFriends newFriends = null;
                while (iterator.hasNext()) {
                    PersonalNoFriends next = iterator.next();
                    if (uploadFriendListInfo.friendList.contains(next.getUserWxId())) {
                        iterator.remove();
                        uploadFriendListInfo.friendList.remove(next.getUserWxId());
                    } else {
                        getSql = DaoGetSql.getSql("UPDATE " + DBFriends + " set deleted = 1 where personal_no_wx_id = ? and user_wx_id = ?", wechatAccount.getWxId(), next.getUserWxId());
                        sql.setSql(getSql);
                        friendsService.deleteBySql(sql);
                        getSql = DaoGetSql.getSql("UPDATE " + DBPeople + " set deleted = 1 where personal_no_wx_id = ? and personal_friend_wx_id = ?", wechatAccount.getWxId(), next.getUserWxId());
                        sql.setSql(getSql);
                        taskPeopleService.deleteBySql(sql);
                    }
                }
                for (String s : uploadFriendListInfo.friendList) {
                    newFriends = new PersonalNoFriends();
                    newFriends.setUserWxId(s);
                    newFriends.setPersonalNoWxId(wechatAccount.getWxId());
                    newFriends.setPersonalNoId(wechatAccount.getId());
                    newFriends.setBeFriendTime(new Date(new Date().getTime() - 24 * 60 * 60 * 1000));
                    newFriends.setDeleted(0);
                    newFriends.setDb(DBFriends);
                    friendsService.add(newFriends);
                }
            }
            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            return tempSunApiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            tempSunApiResponse.setCode(SunApiResponse.CODE_SYS_ERROR);
            return tempSunApiResponse;
        }
    }

    // uploadFollowedOfficalAccounts_上传关注的官方账户信息
    @PostMapping("uploadFollowedOfficalAccounts.do")
    public SunApiResponse uploadFollowedOfficalAccounts(@RequestBody UploadFollowedOfficalAccountsInfo uploadFollowedOfficalAccountsInfo, HttpServletRequest request) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            log.info("uploadFollowedOfficalAccounts去重");
            return tempSunApiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return tempSunApiResponse;
        }
    }


    @PostMapping("signContent4OSS.do")
    public SunApiResponse signContent4OSS(@RequestBody SignContent4OSSInfo signContent4OSSInfo) {
        SunApiResponse ja1 = getSignContent4OSSJsonObject(signContent4OSSInfo);
        return ja1;
    }

    private SunApiResponse getSignContent4OSSJsonObject(SignContent4OSSInfo signContent4OSSInfo) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
            // 读取配置文件。
            Properties prop = new Properties();
            prop.load(new FileInputStream(G.oss_config));
            String accessKeyId = prop.getProperty("AccessKeyID");
            String accessKeySecret = prop.getProperty("AccessKeySecret");
            String signed = ServiceSignature.create().computeSignature(accessKeySecret, signContent4OSSInfo.content);
            tempSunApiResponse.setCode(tempSunApiResponse.CODE_SUCCESS);
            tempSunApiResponse.setData("OSS " + accessKeyId + ":" + signed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempSunApiResponse;
    }

    private static final long serialVersionUID = 5522372203700422672L;
    // 目前只有"cn-hangzhou"这个region可用, 不要使用填写其他region的值
    public static final String REGION_CN_HANGZHOU = "cn-hangzhou";
    public static final String STS_API_VERSION = "2015-04-01";

    protected AssumeRoleResponse assumeRole(String accessKeyId, String accessKeySecret, String roleArn, String roleSessionName, String policy, ProtocolType protocolType, long durationSeconds) throws ClientException {
        try {
            // 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
            IClientProfile profile = DefaultProfile.getProfile(REGION_CN_HANGZHOU, accessKeyId, accessKeySecret);
            DefaultAcsClient client = new DefaultAcsClient(profile);

            // 创建一个 AssumeRoleRequest 并设置请求参数
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setVersion(STS_API_VERSION);
            request.setMethod(MethodType.POST);
            request.setProtocol(protocolType);

            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            request.setPolicy(policy);
            request.setDurationSeconds(durationSeconds);

            // 发起请求，并得到response
            final AssumeRoleResponse response = client.getAcsResponse(request);
            return response;
        } catch (ClientException e) {
            throw e;
        }
    }


    // 回调邀请_不知道是做什么的
    @PostMapping("callback4Invite.do")
    public SunApiResponse callback4Invite(@RequestBody Callback4InviteInfo callback4InviteInfo, HttpServletRequest request) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            log.info("去重使用");
            return tempSunApiResponse;
        } catch (Exception e) {
            tempSunApiResponse.setCode(SunApiResponse.CODE_SYS_ERROR);
            return tempSunApiResponse;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void updateLastConnectInfo(String logicId, String wx_id, HttpServletRequest request) {
        try {
            String getSql = "";
            Sql sql = new Sql();
            PersonalNoOperationStockWechatAccount tempOSWA = null;
            if (null != logicId && (!logicId.equals(""))) {
                getSql = DaoGetSql.getSql("SELECT * from " + DBWeChat + " where id = ? and operation_project_instance_id = ? limit 0,1", logicId, G.ms_OPERATION_PROJECT_INSTANCE_ID);
                sql.setSql(getSql);
                tempOSWA = wechatAccountService.getBySql(sql);
            } else if (tempOSWA == null && null != wx_id && (!wx_id.equals(""))) {
                getSql = DaoGetSql.getSql("SELECT * from " + DBWeChat + " where wx_id = ? and operation_project_instance_id = ? limit 0,1", wx_id, G.ms_OPERATION_PROJECT_INSTANCE_ID);
                sql.setSql(getSql);
                tempOSWA = wechatAccountService.getBySql(sql);
            }
            if (null == tempOSWA) {
                throw new RuntimeException("当前工程实例中不存在该微信号：logicId=" + logicId + ",wx_id=" + wx_id);
            } else {
                tempOSWA.setLastConnectTime(new Date());
                tempOSWA.setLastConnectInternetIp(request.getRemoteHost());
                tempOSWA.setDb(DBWeChat);
                wechatAccountService.add(tempOSWA);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = 300000)
    public void reportCurrentTime() {
        System.err.println(WebConst.getNowDate(new Date()));
        System.out.println("转移完成的任务和任务组");
        String getSql = DaoGetSql.getSql("SELECT * FROM " + DBTaskGroup + " where `status` = '已完成' limit 0,20");
        Sql sql = new Sql(getSql);
        List<PersonalNoPhoneTaskGroupFinsh> taskGroupList = taskGroupService.listFinshBySql(sql);
        List<Integer> groupIdList = new ArrayList<>();
        Integer id = null;
        Map<Integer, Integer> map = new HashMap<>();
        for (PersonalNoPhoneTaskGroupFinsh personalNoPhoneTaskGroupFinsh : taskGroupList) {
            groupIdList.add(personalNoPhoneTaskGroupFinsh.getId());
            id = personalNoPhoneTaskGroupFinsh.getId();
            personalNoPhoneTaskGroupFinsh.setId(null);
            personalNoPhoneTaskGroupFinsh.setDb(DBTaskGroupFinish);
            taskGroupFinshService.add(personalNoPhoneTaskGroupFinsh);
            map.put(id, personalNoPhoneTaskGroupFinsh.getId());
        }
        String taskGroupIds = DaoGetSql.getIds(groupIdList);
        getSql = "SELECT * FROM " + DBTask + " where task_group_id in " + taskGroupIds;
        sql.setSql(getSql);
        List<PersonalNoPhoneTaskFinish> personalNoPhoneTasks = taskService.listFinshBySql(sql);
        for (PersonalNoPhoneTaskFinish personalNoPhoneTaskFinish : personalNoPhoneTasks) {
            personalNoPhoneTaskFinish.setId(null);
            Integer groupId = map.get(personalNoPhoneTaskFinish.getTaskGroupId());
            personalNoPhoneTaskFinish.setTaskGroupId(groupId);
            personalNoPhoneTaskFinish.setDb(DBTaskFinish);
            taskFinishService.add(personalNoPhoneTaskFinish);
        }
        getSql = "DELETE FROM " + DBTaskGroup + " where id in " + taskGroupIds;
        sql.setSql(getSql);
        taskGroupService.deleteBySql(sql);
        getSql = "DELETE FROM " + DBTask + " WHERE task_group_id in " + taskGroupIds;
        sql.setSql(getSql);
        taskService.deleteBySql(sql);
    }

    //    @Scheduled(fixedRate = 600000)
    public void reportPhoneTime() {
        System.err.println(WebConst.getNowDate(new Date()));
        System.out.println("查看手机请求任务时间");
        log.info("获取上报手机不请求任务的微信id列表");
        String getSql = DaoGetSql.getSql("SELECT id FROM " + DBValueTable + " WHERE `type` = '2' and deleted = 0");
        Sql sql = new Sql(getSql);
        List<Integer> idList = valueTableService.listStringBySql(sql);
        if (VerifyUtils.collectionIsEmpty(idList)) {
            return;
        }
        int index = idList.get(new Random().nextInt(idList.size()));
        getSql = DaoGetSql.getById(DBValueTable, index);
        sql.setSql(getSql);
        PersonalNoValueTable bySql = valueTableService.getBySql(sql);
        if (VerifyUtils.isEmpty(bySql)) {
            return;
        }
        log.info("处理十分钟未请求任务的个人号机器人");
        getSql = DaoGetSql.getSql("SELECT * from " + DBWeChat + " where last_request_job_time < ? and operation_project_instance_id = ? and status = '正常'", WebConst.getNowDate(new Date(new Date().getTime() - 10 * 60 * 1000)), G.ms_OPERATION_PROJECT_INSTANCE_ID);
        sql.setSql(getSql);
        List<PersonalNoOperationStockWechatAccount> operationStockWechatAccounts = wechatAccountService.listbySql(sql);
        if (VerifyUtils.collectionIsEmpty(operationStockWechatAccounts)) {
            return;
        }
        log.info("取得所有的管理员微信id");
        getSql = DaoGetSql.getSql("select wx_id from " + DBValueTable + " where type = ? and deleted = 0", 0);
        sql.setSql(getSql);
        List<String> tiList = valueTableService.listBySql(sql);
        if (VerifyUtils.collectionIsEmpty(tiList)) {
            return;
        }
        log.info("循环给手机添加任务");
        StringBuffer stringBuffer = new StringBuffer();
        for (PersonalNoOperationStockWechatAccount operationStockWechatAccount : operationStockWechatAccounts) {
            if (!WebConst.WECHATSTATUS.equals(operationStockWechatAccount.getStatus())) {
                stringBuffer.append("个人号项目机器人不请求任务\nwxId：" + operationStockWechatAccount.getWxId()
                        + "\n类型：" + G.ms_currProjectInstanceName
                        + "\n微信号：" + operationStockWechatAccount.getWxIdBieMing()
                        + "\n昵称：" + operationStockWechatAccount.getNickName()
                        + "\n最后请求任务时间" + WebConst.getNowDate(operationStockWechatAccount.getLastRequestJobTime()) + "\n");
            }
        }
        Date date = new Date();
        for (String s : tiList) {
            date = new Date(date.getTime() + 30 * 1000);
            PersonalNoPhoneTaskGroup taskGroup = new PersonalNoPhoneTaskGroup();
            taskGroup.setCreateTime(date);
            taskGroup.setNextStep(1);
            taskGroup.setTaskOrder(0);
            taskGroup.setStatus("未下发");
            taskGroup.setTname(bySql.getNickName() + " " + bySql.getWxId() + "发送个人号不请求任务消息给" + s);
            taskGroup.setCurrentRobotId(bySql.getWxId());
            taskGroup.setTotalStep(1);
            taskGroup.setDb(DBTaskGroup);
            boolean save = taskGroupService.add(taskGroup) > 0;
            if (save) {
                log.info("开始添加任务");
                PersonalNoPhoneTask task = new PersonalNoPhoneTask();
                task.setTname(bySql.getNickName() + " " + bySql.getWxId() + "发送个人号不请求任务消息给" + s);
                task.setTaskGroupId(taskGroup.getId());
                task.setContent(stringBuffer.toString());
                task.setContentType("文字");
                task.setStep(1);
                task.setTaskType(100);
                task.setRobotId(s);
                task.setCreateTime(date);
                task.setStatus("未下发");
                task.setDb(DBTask);
                boolean save1 = taskService.add(task) > 0;
                if (!save1) {
                    log.info("插入任务失败");
                    throw new RuntimeException("插入任务失败");
                }
            }
        }
    }

    @Scheduled(fixedRate = 3600000)
    public void reportUpdateFriendsList() {
        System.err.println(WebConst.getNowDate(new Date()));
        System.out.println("下发手机上报好友列表任务");
        log.info("处理好友列表");
        String getSql = DaoGetSql.getSql("SELECT * from " + DBWeChat + " where last_request_job_time > ? and operation_project_instance_id = ?", WebConst.getNowDate(new Date(new Date().getTime() - 5 * 60 * 1000)), G.ms_OPERATION_PROJECT_INSTANCE_ID);
        Sql sql = new Sql(getSql);
        List<PersonalNoOperationStockWechatAccount> operationStockWechatAccounts = wechatAccountService.listbySql(sql);
        Date date = new Date();
        for (PersonalNoOperationStockWechatAccount operationStockWechatAccount : operationStockWechatAccounts) {
            date = new Date(date.getTime() + 60 * 1000);
            PersonalNoPhoneTaskGroup taskGroup = new PersonalNoPhoneTaskGroup();
            taskGroup.setCreateTime(date);
            taskGroup.setNextStep(1);
            taskGroup.setTaskOrder(0);
            taskGroup.setStatus("未下发");
            taskGroup.setTname(operationStockWechatAccount.getNickName() + " " + operationStockWechatAccount.getWxId() + "上传好友列表");
            taskGroup.setCurrentRobotId(operationStockWechatAccount.getWxId());
            taskGroup.setTotalStep(1);
            taskGroup.setDb(DBTaskGroup);
            boolean save = taskGroupService.add(taskGroup) > 0;
            if (save) {
                log.info("开始添加任务");
                PersonalNoPhoneTask task = new PersonalNoPhoneTask();
                task.setTname(operationStockWechatAccount.getNickName() + " " + operationStockWechatAccount.getWxId() + "上传好友列表");
                task.setTaskGroupId(taskGroup.getId());
                task.setContentType("上传好友列表");
                task.setStep(1);
                task.setTaskType(SunTaskType.UPLOAD_FRIEND_LIST);
                task.setCreateTime(date);
                task.setStatus("未下发");
                task.setDb(DBTask);
                boolean save1 = taskService.add(task) > 0;
                if (!save1) {
                    log.info("插入任务失败");
                    throw new RuntimeException("插入任务失败");
                }
            }
        }
    }
}
