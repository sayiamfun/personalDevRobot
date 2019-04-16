package com.warm.system.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.warm.entity.R;
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
import net.sf.json.JSONObject;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Transactional
@CrossOrigin //跨域
@Api(description = "手机微信端个人号的管理")
@RestController
@RequestMapping("/robot")
public class RobotController {

    private static boolean toGroup = false;
    private SunTask toGroupSunTask = new SunTask();
    private static Date taskDate = null;
    public static Log log = LogFactory.getLog(RobotController.class);
    @Autowired
    private PersonalNoTaskRemindFlagService remindFlagService;
    @Autowired
    private PersonalNoAutoReplayNoService autoReplayNoService;
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
    private PersonalNoWxUserService wxUserService;
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
    private PersonalNoService noService;
    @Autowired
    private PersonalNoTaskMessageSendService taskMessageSendService;
    @Autowired
    private PersonalNoLableMessageSendService lableMessageSendService;
    @Autowired
    private PersonalNoKeywordService keywordService;
    @Autowired
    private PersonalNoKeywordContentService keywordContentServicec;
    @Autowired
    private PersonalNoRequestExceptionService requestExceptionService;
    @Autowired
    private PersonalNoSuperuesrService superuesrService;
    @Autowired
    private PersonalNoBlacklistService blacklistService;
    @Autowired
    private PersonalNoDataService dataService;
    @Autowired
    private PersonalNoTaskDataService taskDataService;
    @Autowired
    private PersonalNoValueTableService valueTableService;
    @Autowired
    private PersonalNoFriendsCircleService friendsCircleService;
    @Autowired
    private PersonalNoPhoneRequestTaskTimeService phoneRequestTaskTimeService;
    @Autowired
    private PersonalNoAccessTockenService accessTockenService;
    @Autowired
    private PersonalNoRoadService roadService;
    @Autowired
    private PersonalNoTempService tempService;

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
                boolean b = prismrecordService.insert(robotPrismrecord);
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
                boolean b = logAndroidService.insert(tempRL);
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

    @Value("${fileServer.qrurl}")
    String qrurl;

    // 每次启动都会调用这个, 试图注册当前微信号 ,如果已经注册了, 就返回当前logicId
    // logicId其实robot表的主键
    @PostMapping("newRegister.do")
    public IntegerResponse newRegister(@RequestBody NewRegisterInfo newRegisterInfo, HttpServletRequest request, HttpServletResponse response) {
        IntegerResponse tempIntegerResponse = new IntegerResponse();
        log.info("开始注册新个人号信息");
        try {
            log.info("根据个人号昵称和工程实例查找个人号列表");
            List<PersonalNoOperationStockWechatAccount> tempOperationStockWechatAccountList = wechatAccountService.listByNickNameAndInstanceId(newRegisterInfo.nickname, G.ms_OPERATION_PROJECT_INSTANCE_ID);
            int tempLogicId = -1;
            String retMsg = "";
            if (tempOperationStockWechatAccountList.size() == 1) {
                log.info("如果只存在一个，则进行注册");
                PersonalNoOperationStockWechatAccount tempWechatAccount = tempOperationStockWechatAccountList.get(0);
                // 之所以要只更新wxid为空的数据库条目, 是因为怕有昵称重名的
                if (tempWechatAccount.getWxId() == null || tempWechatAccount.getWxId().equals("")) {
                    // 注册成功
                    // 存入数据库
                    tempWechatAccount.setProjectInstanceRegTime(new Date());
                    retMsg = "注册成功";
                } else {
                    retMsg = "已经注册过";
                }
                tempWechatAccount.setWxId(newRegisterInfo.username);
                String qrCodePath = newRegisterInfo.qrCode;
                tempWechatAccount.setQrCode(qrCodePath);
                tempWechatAccount.setLastUpdateLocalIp(newRegisterInfo.ip);
                tempWechatAccount.setLastConnectTime(new Date());
                tempWechatAccount.setClickId(newRegisterInfo.clickId);
                tempWechatAccount.setNickName(newRegisterInfo.nickname);
                log.info("将更新后的个人号信息刷新到数据库");
                boolean b = wechatAccountService.updateByLogicId(tempWechatAccount);
                if (!b) {
                    log.info("更新数据库的个人号信息失败");
                    throw new RuntimeException("数据库更新个人号信息失败");
                }
                log.info("将实例信息添加到个人号表");
                PersonalNo no = noService.getByWxId(tempWechatAccount.getWxId());
                if (VerifyUtils.isEmpty(no)) {
                    no = new PersonalNo();
                }
                no.setCreateTime(new Date());
                no.setEquipmentStatus("线上");
                no.setFriendsNum(0);
                no.setHeadPortraitUrl(null);
                no.setNickname(tempWechatAccount.getNickName());
                no.setWxId(tempWechatAccount.getWxId());
                no.setWxName(tempWechatAccount.getWxIdBieMing());
                if (tempWechatAccount.getQrCode().contains("http://")) {
                    no.setQrCode(tempWechatAccount.getQrCode());
                } else {
                    no.setQrCode(qrurl + tempWechatAccount.getQrCode());
                }
                no.setWaitingPassNum(0);
                no.setFriendsNum(0);
                no.setPersonalNoCategory("jiazhangjia");
                boolean save = noService.insertInfo(no);
                tempLogicId = tempWechatAccount.getId();
            } else if (tempOperationStockWechatAccountList.size() == 0) {
                //
                log.info("当前个人号不在数据库中存在");
                retMsg = "机器人表中不存在当前微信号昵称";
                PersonalNoOperationStockWechatAccount temp = wechatAccountService.getByWxIdAndInstanceId(newRegisterInfo.username, G.ms_OPERATION_PROJECT_INSTANCE_ID);
                if (!VerifyUtils.isEmpty(temp)) {
                    temp.setNickName(newRegisterInfo.nickname);
                    wechatAccountService.updateByLogicId(temp);
                }
                PersonalNo noByWxId = noService.getByWxId(newRegisterInfo.username);
                if (!VerifyUtils.isEmpty(noByWxId)) {
                    noByWxId.setNickname(newRegisterInfo.nickname);
                    noService.updateByLogicId(noByWxId);
                }
            } else if (tempOperationStockWechatAccountList.size() > 1) {
                log.info("个人号昵称重新，需要重新设置个人号昵称");
                retMsg = "机器人重名";
            } else {
                log.info("发生未知错误，无法解决");
                throw new Exception("tempOperationStockWechatAccountList.size()=" + tempOperationStockWechatAccountList.size());
            }
            tempIntegerResponse.code = (SunApiResponse.CODE_SUCCESS);
            tempIntegerResponse.msg = (retMsg);
            tempIntegerResponse.data = (tempLogicId);
            return tempIntegerResponse;
        } catch (Exception e) {
            e.printStackTrace();
            WebConst.insertRequseException(requestExceptionService, request, response, newRegisterInfo);
            tempIntegerResponse.code = (SunApiResponse.CODE_SYS_ERROR);
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
            G.e(e);
            tempIR.code = (SunApiResponse.CODE_SYS_ERROR);
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
            List<PersonalNoWxUser> tempWUList = wxUserService.listByis_bask(1);
            for (int i = 0; i < tempWUList.size(); i++) {
                PersonalNoWxUser tempWU = tempWUList.get(i);
                if (null != tempWU.getWxId())
                    blockUsernames.add(tempWU.getWxId());
            }

            // 只有B1类型需要上传信息，其他都不需要
//            OperationStockWechatAccount tempOSWA = new OperationStockWechatAccount().dao().findFirst("select * from operation_stock_wechat_account where id=? and operation_project_instance_id=?", queryConfInfo.logicId, G.ms_OPERATION_PROJECT_INSTANCE_ID);
            PersonalNoOperationStockWechatAccount tempOSWA = wechatAccountService.getByIdAndInstanceId(queryConfInfo.logicId, G.ms_OPERATION_PROJECT_INSTANCE_ID);
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
                boolean b = wechatAccountService.updateByLogicId(tempOSWA);
                if (!b) {
                    throw new RuntimeException("数据库更新个人号信息失败");
                }
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
            G.e(e);
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
//            OperationStockWechatAccount tempOSWA = new OperationStockWechatAccount().dao().findFirst("select * from operation_stock_wechat_account where wx_id='" + updateWeChatInfo.username + "' and operation_project_instance_id=" + G.ms_OPERATION_PROJECT_INSTANCE_ID);
            PersonalNoOperationStockWechatAccount tempOSWA = wechatAccountService.getByWxIdAndInstanceId(updateWeChatInfo.username, G.ms_OPERATION_PROJECT_INSTANCE_ID);
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
                boolean b = wechatAccountService.updateByLogicId(tempOSWA);
                if (!b) {
                    throw new RuntimeException("数据库更新数据失败");
                }
            } else {
                throw new RuntimeException("微信号库存表中当前工程实例下不存在的wxid请求了updateWeChatInfo接口：wxid=" + updateWeChatInfo.username);
            }
            // ........
            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            return tempSunApiResponse;
        } catch (Exception e) {
            G.e(e);
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
//            List<OperationStockWechatAccount> tempOSWAList = new OperationStockWechatAccount().dao().find("select * from operation_stock_wechat_account where operation_project_instance_id=" + G.ms_OPERATION_PROJECT_INSTANCE_ID);
            List<PersonalNoOperationStockWechatAccount> tempOSWAList = wechatAccountService.listByInstanceId(G.ms_OPERATION_PROJECT_INSTANCE_ID);
            List<String> allRobotUsernames = new LinkedList<String>();
            for (int i = 0; i < tempOSWAList.size(); i++) {
                allRobotUsernames.add(tempOSWAList.get(i).getWxId());
            }
            List<PersonalNoWxUser> tempWUList = wxUserService.listByis_assistant(1);
            for (int i = 0; i < tempWUList.size(); i++) {
                allRobotUsernames.add(tempWUList.get(i).getWxId());
            }
            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            tempSunApiResponse.setData(allRobotUsernames);
            return tempSunApiResponse;
        } catch (Exception e) {
            G.e(e);
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
//            OperationStockWechatAccount tempOSWA = new OperationStockWechatAccount().dao().findFirst("select * from operation_stock_wechat_account where operation_project_instance_id=" + G.ms_OPERATION_PROJECT_INSTANCE_ID + " and wx_id='" + statusDescInfo.username + "'");
            PersonalNoOperationStockWechatAccount tempOSWA = wechatAccountService.getByWxIdAndInstanceId(statusDescInfo.username, G.ms_OPERATION_PROJECT_INSTANCE_ID);
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
                boolean b = wechatAccountService.updateByLogicId(tempOSWA);
                if (!b) {
                    log.error("数据库更新个人号机器人状态信息失败");
                    throw new RuntimeException("数据库更新失败");
                }
            }
            tempSunApiResponse.setData(tempStr);
            return tempSunApiResponse;
        } catch (Exception e) {
            log.error("发生未知错误，无法解决");
            G.e(e);
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
            PersonalNoOperationStockWechatAccount tempOSWA = wechatAccountService.getByIdAndInstanceId(ubi.logicId, G.ms_OPERATION_PROJECT_INSTANCE_ID);
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

            boolean b = wechatAccountService.updateByLogicId(tempOSWA);
            if (!b) {
                log.error("数据库更新个人号机器人相关信息失败");
                throw new RuntimeException("数据库更新数据失败");
            }

            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            return tempSunApiResponse;
        } catch (Exception e) {
            G.e(e);
            log.error("updateBasicInfo出现异常");
            tempSunApiResponse.setCode(SunApiResponse.CODE_SYS_ERROR);
            return tempSunApiResponse;
        }
    }

    // 从服务器获取SunTask
    // 下发任务的同时, 要根据给当前任务存一个标志, 以供后期完成任务的时候能从任务表里标定任务. 完成任务后需要把这个tag给去掉
    @PostMapping("pickTask.do")
    public SunApiResponse pickTask(@RequestBody PickupTaskInfo pickupTaskInfo, HttpServletRequest request, HttpServletResponse response) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
            log.info("开始执行手机请求任务的操作");
            List<SunTask> tempSunTaskList = new LinkedList<SunTask>();
            Integer currRobotLogicId = pickupTaskInfo.logicId;
            String currRobotWxid = pickupTaskInfo.username;
            Map<String, Object> map = TaskUtiles.getMap(taskPeopleService, taskGroupService, noTaskService, taskService, keywordService);
            // 更新机器人最后获取任务的时间
            log.info("去数据库查找该个人号机器人");
            PersonalNoOperationStockWechatAccount tempOSWA = null;
            if (null != currRobotLogicId && (!currRobotLogicId.equals(""))) {
                tempOSWA = wechatAccountService.getByIdAndInstanceId(currRobotLogicId, G.ms_OPERATION_PROJECT_INSTANCE_ID);
            } else if (tempOSWA == null && null != currRobotWxid && (!currRobotWxid.equals(""))) {
                tempOSWA = wechatAccountService.getByWxIdAndInstanceId(currRobotWxid, G.ms_OPERATION_PROJECT_INSTANCE_ID);
            }
            if (null == tempOSWA) {
                G.i(Integer.parseInt(request.getHeader("logicId")), request.getRequestURL().toString(), "当前工程实例中不存在该微信号：logicId=" + currRobotLogicId + ",wx_id=" + currRobotWxid + "   rawInfo=" + pickupTaskInfo);
//                WebConst.insertRequseException(requestExceptionService, request, response, request.getRequestURL().toString()+ "当前工程实例中不存在该微信号：logicId=" + currRobotLogicId + ",wx_id=" + currRobotWxid + "   rawInfo=" + pickupTaskInfo);
                tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
                return tempSunApiResponse;
            } else {
                log.info("更新个人号机器人最后请求任务的时间和链接时间");
                tempOSWA.setLastRequestJobTime(new Date());
                tempOSWA.setLastConnectTime(new Date());
                tempOSWA.setLastConnectInternetIp(request.getRemoteHost());
                boolean b = wechatAccountService.updateByLogicId(tempOSWA);
                if (!b) {
                    log.error("更细个人号最后请求任务时间和链接时间");
                    throw new RuntimeException("数据库更新wechat失败");
                }
            }

            log.info("将当前手机和时间添加到数据库");
            PersonalNoPhoneRequestTaskTime phoneRequestTaskTime = new PersonalNoPhoneRequestTaskTime();
            phoneRequestTaskTime.setWxId(tempOSWA.getWxId());
            phoneRequestTaskTime.setNickName(tempOSWA.getNickName());
            phoneRequestTaskTime.setRequestTime(new Date());
            phoneRequestTaskTimeService.insertRequestTime(phoneRequestTaskTime);
            SunTask sunTask = null;
            PersonalNoPhoneTaskGroup taskGroup = null;
            PersonalNoPhoneTask byId = null;
            log.info("先找order为10的添加好友任务");
            List<PersonalNoPhoneTaskGroup> wxid_o72bs8evoigc22 = taskGroupService.listBycurrent_robot_idAndStatusWating(tempOSWA.getWxId(), 10);
            if (!VerifyUtils.collectionIsEmpty(wxid_o72bs8evoigc22)) {
                log.info("有添加好友任务");
                taskGroup = wxid_o72bs8evoigc22.get(0);
                log.info("找到要执行的添加好友任务");
                byId = taskService.getOneBytask_group_idAndstep(taskGroup.getId(), taskGroup.getNextStep());
                if (!VerifyUtils.isEmpty(byId) && "未下发".equals(byId.getStatus())) {
                    sunTask = SunTaskHelper.getTask_personOP(SunTaskType.FRIEND_ACCEPT_REQUEST, null, null, null, null, byId.getTaskJson());
                    tempSunTaskList.add(sunTask);
                }
            } else {
                wxid_o72bs8evoigc22 = taskGroupService.listBycurrent_robot_idAndStatusWating(tempOSWA.getWxId(), 9);
                if (VerifyUtils.collectionIsEmpty(wxid_o72bs8evoigc22)) {
                    log.info("没有将要执行的自动回复任务");
                    wxid_o72bs8evoigc22 = taskGroupService.listBycurrent_robot_idAndStatusWating(tempOSWA.getWxId(), 0);
                    if (VerifyUtils.collectionIsEmpty(wxid_o72bs8evoigc22)) {
                        log.info("没有将要执行的普通任务");
                    }
                }
            }
            log.info("将一条任务下发给手机");
            //修改普通任务的发送数量
            updateSendNum(wxid_o72bs8evoigc22);
            List<SunTask> sunTasks1 = gettempSunTaskList(wxid_o72bs8evoigc22);
            tempSunTaskList.addAll(sunTasks1);
            log.info("根据当前时间查看是否有此时间段的定时任务");
            List<PersonalNoPhoneTaskGroup> taskGroups = taskGroupService.listBycurrent_robot_idAndStatusGoingAndTime(tempOSWA.getWxId(), new Date(), 1);
            if (!VerifyUtils.collectionIsEmpty(taskGroups)) {
                //修改定时任务的发送数量
                updateSendNum(taskGroups);
                List<SunTask> sunTasks = gettempSunTaskList(taskGroups);
                log.info("将定时任务一起发送给手机");
                tempSunTaskList.addAll(sunTasks);
            }
            if (toGroup) {
                tempSunTaskList.add(toGroupSunTask);
            }
            log.info("判断是否需要判断请求手机的任务");
            updateTaskDate(tempSunTaskList, tempOSWA.getWxId());
            log.info("判断是否需要主动回复消息");
            List<PersonalNoTemp> tempList = tempService.listByPersonalWxIdAndTimeAndFlag(tempOSWA.getWxId(), 0);
            if (!VerifyUtils.collectionIsEmpty(tempList)) {
                for (PersonalNoTemp personalNoTemp : tempList) {
                    TaskUtiles.toKeyWordTask(map, personalNoTemp.getPersonalNoWxId(), personalNoTemp.getUserWxId(), 3, 0);
                    personalNoTemp.setFlag(1);
                    tempService.updateFlagById(personalNoTemp);
                }
            }
            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            tempSunApiResponse.setData(tempSunTaskList);
            return tempSunApiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            WebConst.insertRequseException(requestExceptionService, request, response, pickupTaskInfo.logicId + pickupTaskInfo.username + e.getCause());
            tempSunApiResponse.setCode(SunApiResponse.CODE_SYS_ERROR);
            return tempSunApiResponse;
        }
    }

    //将今天的任务数据添加到数据库
    private void insertToDayData() {
        PersonalNoValueTable byId = valueTableService.getById(3);
        Date datTaskDate = WebConst.getDateByString(byId.getValue());
        if (new Date().getTime() - (datTaskDate.getTime() + 24 * 60 * 60 * 1000) > 1000) {
            log.info("开始统计今天数据到数据库");
            List<PersonalNoData> dataList = dataService.listByDate(WebConst.getNowDate(datTaskDate));
            log.info("先将以前的删除，保证只有一个时间点的数据");
            dataService.deleteByDate(dataList);
            log.info("查询所有此时间段的任务");
            List<PersonalNoTask> noTaskList = noTaskService.listByStartTimeAndEndTime(datTaskDate, new Date(datTaskDate.getTime() + 24 * 60 * 60 * 1000));
            for (PersonalNoTask noTask : noTaskList) {
                log.info("获取每个任务的数据");
                log.info("获取昨天的任务数据");
                PersonalNoData yesterdayData = dataService.getByTaskNameAndTime(noTask.getTaskName(), WebConst.getNowDateNotHour(new Date(datTaskDate.getTime() - 24 * 60 * 60 * 1000)));
                PersonalNoTaskData byTaskId = taskDataService.getByTaskId(noTask.getId());
                PersonalNoData noData = new PersonalNoData();
                if (!VerifyUtils.isEmpty(byTaskId)) {
                    log.info("推送人数");
                    noData.setToPeopleNum(yesterdayData == null ? byTaskId.getToPeopleNum() : byTaskId.getToPeopleNum() - yesterdayData.getToPeopleNum());
                } else {
                    log.info("推送人数");
                    noData.setToPeopleNum(0);
                }
                log.info("添加好友人数");
                List<PersonalNoPeople> peopleList = taskPeopleService.listByTaskIdAndTime(noTask.getId(), datTaskDate, new Date(datTaskDate.getTime() + 24 * 60 * 60 * 1000));
                noData.setAddFriendsNum(peopleList == null ? 0 : peopleList.size());
                noData.setTheRateOfAddFriends(WebConst.div(noData.getAddFriendsNum(), noData.getToPeopleNum(), 2));
                noData.setDate(datTaskDate);
                noData.setTaskName(noTask.getTaskName());
                boolean save = dataService.insert(noData);
                if (!save) {
                    throw new RuntimeException("添加任务数据信息失败");
                }
            }
            log.info("修改下次任务时间");
            String nowDateNotHour = WebConst.getNowDateNotHour(new Date(datTaskDate.getTime() + 24 * 60 * 60 * 1000));
            byId.setValue(nowDateNotHour);
            valueTableService.updateInfoById(byId);
        }
    }

    //更新请求手机最后下发任务时间的时间点
    private void updateTaskDate(List<SunTask> tempSunTaskList, String personalWxId) {
        log.info("判断是否需要添加今天任务的数据到数据库");
        insertToDayData();

        PersonalNoValueTable byId = valueTableService.getById(2);
        if (VerifyUtils.isEmpty(taskDate)) {
            taskDate = new Date(new Date().getTime() + Integer.parseInt(byId.getValue()) * 1000);
        } else {
            if (new Date().getTime() - taskDate.getTime() > 10000L) {
                log.info("处理十分钟未请求任务的个人号机器人");
                Long temp = Long.valueOf(valueTableService.getById(2).getValue()) * 1000;
                Date date = new Date(new Date().getTime() - temp);
                List<PersonalNoOperationStockWechatAccount> operationStockWechatAccounts = wechatAccountService.listByRequestTaskTimeAndInstanceId(date, G.ms_OPERATION_PROJECT_INSTANCE_ID);
                List<String> personalWxIdList = valueTableService.listWxIdByType(2);
                if (personalWxIdList.contains(personalWxId)) {
                    log.info("取得所有的管理员微信id");
                    List<String> tiList = valueTableService.listWxIdByType(0);
                    for (String s : tiList) {
                        SunTask sunTask1 = new SunTask();
                        log.info("循环给手机添加任务");
                        List<String> tolist = new ArrayList<>();
                        tolist.add(s);
                        sunTask1.setToUsernames(tolist);
                        sunTask1.setWeChatMsgType(WeChatMsgType.WECHAT_MESSAGE_TYPE_TEXT);
                        sunTask1.setType(SunTaskType.FRIEND_SEND_MSG);
                        StringBuffer stringBuffer = new StringBuffer();
                        for (PersonalNoOperationStockWechatAccount operationStockWechatAccount : operationStockWechatAccounts) {
                            stringBuffer.append("个人号项目机器人不请求任务\nwxId：" + operationStockWechatAccount.getWxId()
                                    + "\n类型：" + operationStockWechatAccount.getWxClass()
                                    + "\n昵称：" + operationStockWechatAccount.getNickName()
                                    + "\n最后请求任务时间" + WebConst.getNowDate(operationStockWechatAccount.getLastRequestJobTime()) + "\n");
                        }
                        sunTask1.setContent(stringBuffer.toString());
                        tempSunTaskList.add(sunTask1);
                    }
                    log.info("处理好友列表");
                    SunTask sunTask = SunTaskHelper.getTask_personOP(SunTaskType.UPLOAD_FRIEND_LIST, null, null, null, null, null);
                    tempSunTaskList.add(sunTask);
                    taskDate = new Date(taskDate.getTime() + Integer.parseInt(byId.getValue()) * 1000);
                }
            }
        }
    }

    //下发任务后修改任务的发送条数（任务状态显示）
    private void updateSendNum(List<PersonalNoPhoneTaskGroup> taskGroups) {
        if (!VerifyUtils.collectionIsEmpty(taskGroups)) {
            for (PersonalNoPhoneTaskGroup taskGroup : taskGroups) {
                log.info("根据任务或标签消息id查询对应的任务标签消息");
                if (!VerifyUtils.isEmpty(taskGroup.getTaskSendId())) {
                    if (taskGroup.getTaskSendId() != -1) {
                        log.info("修改任务消息");
                        PersonalNoTaskMessageSend taskMessageById = taskMessageSendService.selectById(taskGroup.getTaskSendId());
                        if (!VerifyUtils.isEmpty(taskMessageById)) {
                            String[] split = taskMessageById.getPersonaNolTaskMessageStatus().split("/");
                            if (Integer.parseInt(split[0]) < Integer.parseInt(split[1])) {
                                Integer num = Integer.parseInt(split[0]) + 1;
                                taskMessageById.setPersonaNolTaskMessageStatus(num.toString() + "/" + split[1]);
                                taskMessageSendService.updateById(taskMessageById);
                            }
                        }
                    } else {
                        log.info("修改朋友圈消息");
                        PersonalNoFriendsCircle circleById = friendsCircleService.getCircleById(taskGroup.getLableSendId());
                        if (!VerifyUtils.isEmpty(circleById)) {
                            String[] split = circleById.getStatus().split("/");
                            if (Integer.parseInt(split[0]) < Integer.parseInt(split[1])) {
                                Integer num = Integer.parseInt(split[0]) + 1;
                                circleById.setStatus(num.toString() + "/" + split[1]);
                                friendsCircleService.updateById(circleById);
                            }
                        }
                    }
                } else {
                    if (!VerifyUtils.isEmpty(taskGroup.getLableSendId())) {
                        log.info("修改标签消息");
                        PersonalNoLableMessageSend lableMessageById = lableMessageSendService.selectById(taskGroup.getLableSendId());
                        if (!VerifyUtils.isEmpty(lableMessageById)) {
                            String[] split = lableMessageById.getPersonaNolLableMessageStatus().split("/");
                            if (Integer.parseInt(split[0]) < Integer.parseInt(split[1])) {
                                Integer num = Integer.parseInt(split[0]) + 1;
                                lableMessageById.setPersonaNolLableMessageStatus(num.toString() + "/" + split[1]);
                                lableMessageSendService.updateById(lableMessageById);
                            }
                        }
                    }
                }
            }
        }

    }

    //将任务组集合转换为发给手机的任务集合
    private List<SunTask> gettempSunTaskList(List<PersonalNoPhoneTaskGroup> wxid_o72bs8evoigc22) {
        List<SunTask> tempSunTaskList = new ArrayList<>();
        PersonalNoPhoneTask byId = null;
        SunTask sunTask = null;
        if (!VerifyUtils.collectionIsEmpty(wxid_o72bs8evoigc22)) {
            for (PersonalNoPhoneTaskGroup personalNoPhoneTaskGroup : wxid_o72bs8evoigc22) {
                PersonalNoPhoneTaskGroup taskGroup1 = personalNoPhoneTaskGroup;
                log.info("找到该任务组要执行的任务");
                byId = taskService.getOneBytask_group_idAndstep(taskGroup1.getId(), taskGroup1.getNextStep());
                if (!VerifyUtils.isEmpty(byId) && "未下发".equals(byId.getStatus())) {
                    sunTask = taskToSunTask(byId);
                    log.info("开始下发任务");
                    tempSunTaskList.add(sunTask);
                    log.info("下发完更新任务状态");
                    byId.setStatus("执行中");
                    boolean b = taskService.updateById(byId);
                    if (!b) {
                        throw new RuntimeException("更新任务失败");
                    }
                }
                if (taskGroup1.getNextStep() < taskGroup1.getTotalStep()) {
                    taskGroup1.setNextStep(taskGroup1.getNextStep() + 1);
                } else {
                    taskGroup1.setStatus("已完成");
                }
                boolean update = taskGroupService.updateInfoById(taskGroup1);
                if (!update) {
                    throw new RuntimeException("更新任务组失败");
                }
            }
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
                if(byId.getContent().split("/").length>1) {
                    String[] split = byId.getContent().split("/");
                    byId.setContent(split[1]);
                }else {
                    List<Integer> idList = personalNoSmallFaceService.listId();
                    int index = idList.get(new Random().nextInt(idList.size()));
                    PersonalNoSmallFace smallFace = personalNoSmallFaceService.getById(index);
                    if (!VerifyUtils.isEmpty(smallFace)) {
                        byId.setContent(smallFace.getFace() + " " + byId.getContent());
                    }
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
            PersonalNoUser byWxId = userService.getByWxId(byId.getRobotId());
            if (!VerifyUtils.isEmpty(byWxId) && !VerifyUtils.isEmpty(byWxId.getNickName()) && !VerifyUtils.isEmpty(byId.getContent())) {
                sunTask.setContent(byId.getContent().replaceFirst("####", "亲爱的" + byWxId.getNickName() + ","));
            } else if (!VerifyUtils.isEmpty(byId.getContent())) {
                sunTask.setContent(byId.getContent().replaceFirst("####", ""));
            }
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
            for (int i = 0; i < feedBackReceivedInfo.taskIdList.size(); i++) {
//                Long tempTaskId = feedBackReceivedInfo.taskIdList.get(i);
////                Db.update("UPDATE task SET is_client_feedback_received = 1 ,feedback_received_time=now()  WHERE id = " + tempTaskId);
//                PersonalNoPhoneTask byId = taskService.getById(tempTaskId);
//                byId.setIsClientFeedbackReceived(1);
//                byId.setFeedbackReceivedTime(new Date());
//                boolean b = taskService.updateById(byId);
//                if (!b) {
//                    throw new RuntimeException("数据库更新失败");
//                }
            }
            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            tempSunApiResponse.setData(feedBackReceivedInfo.taskIdList.size()); // 返回接受到的任务id的数目
            return tempSunApiResponse;
        } catch (Exception e) {
            G.e(e);
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
                boolean b = taskService.updateById(tempTask);
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
            G.e(e);
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
            G.e(e);
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
            G.e(e);
            tempBooleanResponse.code = SunApiResponse.CODE_SYS_ERROR;
            return tempBooleanResponse;
        }
    }

    // 更新上报机器人本地IP
    @PostMapping("updateRobotIp.do")
    public SunApiResponse updateRobotIp(@RequestBody UpdateRobotIpInfo updateRobotIpInfo, HttpServletRequest request) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
//            OperationStockWechatAccount tempOSWA = new OperationStockWechatAccount().dao().findFirst("select * from operation_stock_wechat_account where id=? and operation_project_instance_id=?", updateRobotIpInfo.logicId, G.ms_OPERATION_PROJECT_INSTANCE_ID);
            PersonalNoOperationStockWechatAccount tempOSWA = wechatAccountService.getByIdAndInstanceId(updateRobotIpInfo.logicId, G.ms_OPERATION_PROJECT_INSTANCE_ID);
            if (tempOSWA == null) {
                throw new RuntimeException("updateRobotIp 数据库里的查不到该robot,logicId=" + updateRobotIpInfo.logicId);
            }
            tempOSWA.setLastUpdateLocalIp(updateRobotIpInfo.ip);
            tempOSWA.setLastConnectInternetIp(request.getRemoteHost());
            tempOSWA.setLastConnectTime(new Date());
            boolean b = wechatAccountService.updateByLogicId(tempOSWA);
            if (!b) {
                throw new RuntimeException("更新微信号信息失败" + updateRobotIpInfo.logicId);
            }
            tempSunApiResponse.setCode(SunApiResponse.CODE_UNREALIZATION);
            return tempSunApiResponse;
        } catch (Exception e) {
            G.e(e);
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
    public SunApiResponse writePrismRecord(@RequestBody WritePrismRecordInfo writePrismRecordInfo, HttpServletRequest request, HttpServletResponse response) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
            boolean b = true;
            if (writePrismRecordInfo.recordList.get(0).getType() == 40) {
                log.info("处理删除好友任务");
                toDeleteFriendTask(writePrismRecordInfo);
            } else {
                for (int i = 0; i < writePrismRecordInfo.recordList.size(); i++) {
                    log.info("将操作记录插入到数据库");
//                    PersonalNoPrismRecord currPR = insertPrismRecord(writePrismRecordInfo, request, i);
                    PersonalNoPrismRecord currPR = writePrismRecordInfo.recordList.get(i);
                    if (currPR == null) continue;
                    log.info("获取个人号列表");
                    List<String> personlaWxIdList = noService.lisWxId();
                    log.info("判断是否存在任务好友，存在发送任务消息，不存在则发送默认消息");
                    if (!VerifyUtils.isEmpty(currPR) && !VerifyUtils.collectionIsEmpty(currPR.getToUsernames()) && !VerifyUtils.isEmpty(currPR.getFromUsername()) && personlaWxIdList.contains(currPR.getToUsernames().get(0))) {
                        log.info("根据个人号微信id和好友微信id确认是哪一个个人号下的哪一个任务粉丝");
                        PersonalNoPeople personalNoPeople = taskPeopleService.getByPersonalIdAndUserId(currPR.getToUsernames().get(0), currPR.getFromUsername(), 0);
                        if (VerifyUtils.isEmpty(personalNoPeople)) {
                            personalNoPeople = taskPeopleService.getByPersonalIdAndUserId(currPR.getToUsernames().get(0), currPR.getFromUsername(), 1);
                        }
                        PersonalNoValueTable byId = valueTableService.getById(1);
                        Map<String, Object> map = TaskUtiles.getMap(taskPeopleService, taskGroupService, noTaskService, taskService, keywordService);
                        if (!VerifyUtils.isEmpty(personalNoPeople)) {
                            b = false;
                            log.info("根据个人号wxid和用户微信id下发任务");
                            log.info("发送自动回复内容给好友");
                            TaskUtiles.toTask(map, personalNoPeople.getPersonalNoWxId(), personalNoPeople.getPersonalFriendWxId(), personalNoPeople.getPersonalTaskId(), Integer.parseInt(byId.getValue()) * 1000);
                            log.info("添加任务开课提醒");
                            TaskUtiles.toRemindTask(map, remindFlagService, personalNoPeople.getPersonalNoWxId(), personalNoPeople.getPersonalFriendWxId(), personalNoPeople.getPersonalTaskId(), 0);
                            log.info("添加一个定时任务");
                            TaskUtiles.toKeyWordTask(map, personalNoPeople.getPersonalNoWxId(), personalNoPeople.getPersonalFriendWxId(), 2, Integer.parseInt(valueTableService.getById(4).getValue()) * 1000);
                            taskPeopleService.updateFlagById(personalNoPeople.getId(), 2);
                            log.info("根据任务id获取要发送的任务信息");
                            PersonalNoTask taskById = noTaskService.getTaskById(personalNoPeople.getPersonalTaskId());
                            log.info("给微信好友添加标签");
                            toAddFriendLableTask(currPR, taskById);
                            log.info("将定时任务一并添加到数据库");
                            toAutoRmindTask(currPR, taskById);
                        } else {
                            if (personlaWxIdList.contains(currPR.getToUsernames().get(0))) {
                                PersonalNoFriends byPersonalWxIdAndUserWxId = friendsService.getByPersonalWxIdAndUserWxId(currPR.getToUsernames().get(0), currPR.getFromUsername());
                                if (VerifyUtils.isEmpty(byPersonalWxIdAndUserWxId)) {
                                    b = false;
                                    log.info("处理个人号好友");
                                    PersonalNoFriends personalNoFriends = new PersonalNoFriends();
                                    personalNoFriends.setBeFriendTime(new Date());
                                    log.info("处理个人号id");
                                    PersonalNoOperationStockWechatAccount byWxId = wechatAccountService.getByWxIdAndInstanceId(currPR.getToUsernames().get(0), 0);
                                    personalNoFriends.setPersonalNoId(byWxId == null ? null : byWxId.getId());
                                    personalNoFriends.setPersonalNoWxId(currPR.getToUsernames().get(0));
                                    log.info("处理用户id");
                                    PersonalNoUser byWxId1 = userService.getByWxId(currPR.getFromUsername());
                                    if (VerifyUtils.isEmpty(byWxId1)) {
                                        byWxId1 = new PersonalNoUser();
                                        byWxId1.setCreateTime(new Date());
                                        byWxId1.setWxId(currPR.getFromUsername());
                                        userService.insert(byWxId1);
                                    }
                                    personalNoFriends.setUserId(byWxId1.getId());
                                    personalNoFriends.setUserWxId(currPR.getFromUsername());
                                    friendsService.insert(personalNoFriends);
                                    List<String> autoReplayNos = autoReplayNoService.listWxId();
                                    if (!autoReplayNos.contains(currPR.getToUsernames().get(0))) {
                                        map.put("keywordService", keywordService);
                                        map.put("keywordContentServicec", keywordContentServicec);
                                        TaskUtiles.toKeyWordTask(map, currPR.getToUsernames().get(0), currPR.getFromUsername(), 1, Integer.parseInt(byId.getValue()) * 1000);
                                    }
                                }
                            }
                        }
                        log.info("判断是否是审核图片");
                        b = toAuditTask(writePrismRecordInfo, currPR);
                        log.info("开始处理关键词回复");
                        b = toKeyWordTask(writePrismRecordInfo, currPR);
                        log.info("将用户消息转发给运营或将运营消息转发给用户");
                        sendMessageToManager(writePrismRecordInfo, personlaWxIdList);
                        log.info("将小程序转换成链接");
                        getSmallParamUrl(writePrismRecordInfo, personlaWxIdList);
                        log.info("机器人回复");
                        if (b) {
                            toTuLingMessage(writePrismRecordInfo, currPR);
                        }
                        log.info("第一条消息插入到数据库，如果指定时间内没有说话则发送一条默认信息");
                        toAuToSayHello(currPR, personlaWxIdList, map);
                    }
                }
            }
            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            return tempSunApiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            WebConst.insertRequseException(requestExceptionService, request, response, writePrismRecordInfo.recordList.get(0));
            tempSunApiResponse.setCode(SunApiResponse.CODE_SYS_ERROR);
            return tempSunApiResponse;
        }
    }
    //机器人回复
    private void toTuLingMessage(@RequestBody WritePrismRecordInfo writePrismRecordInfo, PersonalNoPrismRecord currPR) {
        String params = TuLingParam.getParams(writePrismRecordInfo.recordList.get(0).getContent(), "");
        String s = HttpClientUtil.sendPost("http://openapi.tuling123.com/openapi/api/v2", params);
        TuLingResult tuLingResult = JSONUtils.jsonToPojo(s, TuLingResult.class);
        log.info("机器人回复");
        if(tuLingResult.getIntent().getCode() == 10004 || tuLingResult.getIntent().getCode() == 10008){
            PersonalNoPhoneTaskGroup taskGroup = new PersonalNoPhoneTaskGroup();
            taskGroup.setStatus("未下发");
            log.info("去重用");
            taskGroup.setCurrentRobotId(currPR.getToUsernames().get(0));
            taskGroup.setNextStep(1);
            taskGroup.setTotalStep(1);
            taskGroup.setTname(currPR.getToUsernames().get(0) + "机器人回复给" + currPR.getFromUsername());
            taskGroup.setCreateTime(new Date());
            taskGroup.setTaskOrder(0);
            boolean save = taskGroupService.insert(taskGroup);
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
            task.setContent("1/" + tuLingResult.getResults().get(0).getValues().getText());
            task.setContentType("文字");
            boolean save1 = taskService.insert(task);
        }
    }

    //主动回复消息
    private void toAuToSayHello(PersonalNoPrismRecord currPR, List<String> personlaWxIdList, Map<String, Object> map) {
        PersonalNoTemp personalNoTemp = tempService.getByPersonalIdAndUserWxId(currPR.getToUsernames().get(0), currPR.getFromUsername());
        if (personlaWxIdList.contains(currPR.getToUsernames().get(0))) {
            List<String> list = autoReplayNoService.listWxId();
            if (!list.contains(currPR.getToUsernames().get(0))) {
                if (VerifyUtils.isEmpty(personalNoTemp)) {
                    personalNoTemp = new PersonalNoTemp();
                    personalNoTemp.setCreateTime(new Date());
                    personalNoTemp.setPersonalNoWxId(currPR.getToUsernames().get(0));
                    personalNoTemp.setUserWxId(currPR.getFromUsername());
                    personalNoTemp.setFlag(0);
                    tempService.insertPersonalNoTemp(personalNoTemp);
                }
            }
        }
    }

    //将操作记录插入到数据库
    private PersonalNoPrismRecord insertPrismRecord(@RequestBody WritePrismRecordInfo writePrismRecordInfo, HttpServletRequest request, int i) {
        String tempStr;//避免重复提交
        synchronized (ms_PrismRecordIdList) {
            if (ms_PrismRecordIdList.size() > 500) {
                for (int x = 0; x < 300; x++) {
                    ms_PrismRecordIdList.remove(0);
                }
            }
            if (ms_PrismRecordIdList.contains(writePrismRecordInfo.recordList.get(i).getId()))
                return null;
            else {
                ms_PrismRecordIdList.add(writePrismRecordInfo.recordList.get(i).getId().longValue());
            }
        }
        PersonalNoPrismRecord currPR = writePrismRecordInfo.recordList.get(i);
        //数据库存在此条数据
        //一次只允许一个，防止脏读，幻读
        PersonalNoPrismRecord tempPR = prismrecordService.selectById(currPR.getId());
        if (VerifyUtils.isEmpty(tempPR)) {
            tempPR = new PersonalNoPrismRecord();
            boolean save = prismrecordService.insert(tempPR);
            if (!save) {
                log.info("插入记录失败");
                throw new RuntimeException("插入记录失败");
            }
        }
        tempPR.setBetaType(currPR.getBetaType());
        tempPR.setByOwner(currPR.getByOwner());
        tempPR.setByQrcode(currPR.getByQrcode());
        tempPR.setChatroom(currPR.getChatroom());
        tempPR.setContent(currPR.getContent());
        tempPR.setCreateTime(currPR.getCreateTime());
        tempPR.setExtContent(currPR.getExtContent());
        tempPR.setFromNickname(currPR.getFromNickname());
        if (null != currPR.getReporterMentioned())
            tempPR.setReporterMentioned(currPR.getReporterMentioned() ? true : false);
        if (null != currPR.getRobotMentioned())
            tempPR.setRobotMentioned(currPR.getRobotMentioned() ? true : false);
        if (null != currPR.getSent())
            tempPR.setSent(currPR.getSent() ? true : false);
        tempPR.setLogicId(currPR.getLogicId());
        tempPR.setMd5(currPR.getMd5());
        if (null != currPR.getPackageId())
            tempPR.setPackageId(currPR.getPackageId());
        if (null != currPR.getToUserType())
            tempPR.setToUserType(currPR.getToUserType());
        if (!VerifyUtils.collectionIsEmpty(currPR.getToUsernames())) {
            tempStr = "";
            for (String tousername : currPR.getToUsernames()) {
                tempStr += "||" + tousername;
            }
            tempPR.setToUserNameList(tempStr);
        }
        tempPR.setType(currPR.getType());
        tempPR.setUnit(currPR.getUnit());
        tempPR.setWeChatmsgType(currPR.getWeChatmsgType());
        tempPR.setWhatever(currPR.getWhatever());
        tempPR.setFromUsername(currPR.getFromUsername());
        tempPR.setLogicId(Integer.parseInt(request.getHeader("logicId")));
        tempPR.setReportInternetIp(request.getRemoteHost());
        tempPR.setReportTime(new Date());
        tempPR.setId(currPR.getId());
        boolean b1 = prismrecordService.updateById(tempPR);
        if (!b1) {
            throw new RuntimeException("数据库更新失败");
        }
        return currPR;
    }

    private void getSmallParamUrl(@RequestBody WritePrismRecordInfo writePrismRecordInfo, List<String> personlaWxIdList) {
        List<String> strings = valueTableService.listWxIdByType(0);
        if (!strings.contains(writePrismRecordInfo.recordList.get(0).getFromUsername())) {
            return;
        }
        if (personlaWxIdList.contains(writePrismRecordInfo.recordList.get(0).getToUsernames().get(0))) {
            if ((writePrismRecordInfo.recordList.get(0).getContent().contains("<msg><appmsg appid=\"\" sdkver=\"0\">") && writePrismRecordInfo.recordList.get(0).getContent().contains("<appname></appname></appinfo><commenturl></commenturl></msg>")) || (writePrismRecordInfo.recordList.get(0).getContent().contains("/robotFiles/voiceMsg") && writePrismRecordInfo.recordList.get(0).getContent().contains(".amr"))) {
                PersonalNoPhoneTaskGroup taskGroup = new PersonalNoPhoneTaskGroup();
                taskGroup.setTaskOrder(0);
                taskGroup.setCreateTime(new Date());
                taskGroup.setTname("返回小程序");
                taskGroup.setTotalStep(1);
                taskGroup.setNextStep(1);
                taskGroup.setCurrentRobotId(writePrismRecordInfo.recordList.get(0).getToUsernames().get(0));
                taskGroup.setStatus("未下发");
                boolean save = taskGroupService.insert(taskGroup);
                PersonalNoPhoneTask task = new PersonalNoPhoneTask();
                task.setTaskGroupId(taskGroup.getId());
                task.setStep(1);
                task.setTaskType(SunTaskType.FRIEND_SEND_MSG);
                task.setRobotId(writePrismRecordInfo.recordList.get(0).getFromUsername());
                task.setStatus("未下发");
                task.setTname("返回小程序");
                task.setCreateTime(new Date());
                if (writePrismRecordInfo.recordList.get(0).getContent().contains(".amr")) {
                    writePrismRecordInfo.recordList.get(0).setContent(qrurl + writePrismRecordInfo.recordList.get(0).getContent());
                }
                task.setContent(writePrismRecordInfo.recordList.get(0).getContent());
                task.setContentType("文字");
                boolean save1 = taskService.insert(task);
            }
        }
    }

//     * 给微信好友贴标签任务
//     *
//     * @param currPR
//     * @param taskById


    private void toAddFriendLableTask(PersonalNoPrismRecord currPR, PersonalNoTask taskById) {
        if (!VerifyUtils.collectionIsEmpty(taskById.getNoLableList())) {
            PersonalNoPhoneTaskGroup taskGroup = new PersonalNoPhoneTaskGroup();
            taskGroup.setTaskOrder(0);
            taskGroup.setCreateTime(new Date());
            taskGroup.setTname(currPR.getToUsernames().get(0) + "给好友" + currPR.getFromUsername() + "添加标签");
            taskGroup.setTotalStep(taskById.getNoLableList().size());
            taskGroup.setNextStep(1);
            taskGroup.setCurrentRobotId(currPR.getToUsernames().get(0));
            taskGroup.setStatus("未下发");
            boolean save = taskGroupService.insert(taskGroup);
            if (save) {
                for (int j = 0; j < taskById.getNoLableList().size(); j++) {
                    PersonalNoPhoneTask task = new PersonalNoPhoneTask();
                    task.setStep(j + 1);
                    task.setTaskGroupId(taskGroup.getId());
                    task.setTaskType(SunTaskType.FRIEND_ADD_LABEL);
                    task.setRobotId(currPR.getFromUsername());
                    task.setStatus("未下发");
                    task.setTname(currPR.getToUsernames().get(0) + "给好友" + currPR.getFromUsername() + "添加标签" + taskById.getNoLableList().get(j).getLableName());
                    task.setCreateTime(new Date());
                    task.setContent(taskById.getNoLableList().get(j).getLableName());
                    boolean save1 = taskService.insert(task);
                    if (!save1) {
                        log.error("添加粉丝标签任务失败");
                        throw new RuntimeException("添加粉丝标签任务失败");
                    }
                }
            }
        }
    }

//     * 删除好友任务
//     *
//     * @param writePrismRecordInfo


    private void toDeleteFriendTask(@RequestBody WritePrismRecordInfo writePrismRecordInfo) {
        PersonalNoOperationStockWechatAccount byId = wechatAccountService.getByLogicId(writePrismRecordInfo.recordList.get(0).getLogicId());
        PersonalNoPhoneTaskGroup taskGroup = new PersonalNoPhoneTaskGroup();
        taskGroup.setCreateTime(new Date());
        taskGroup.setTaskOrder(0);
        taskGroup.setTname(byId.getNickName() + "删除好友" + writePrismRecordInfo.recordList.get(0).getFromUsername());
        taskGroup.setTotalStep(1);
        taskGroup.setNextStep(1);
        taskGroup.setCurrentRobotId(byId.getWxId());
        taskGroup.setStatus("未下发");
        boolean save = taskGroupService.insert(taskGroup);
        if (save) {
            PersonalNoPhoneTask task = new PersonalNoPhoneTask();
            task.setStep(1);
            task.setTname(byId.getNickName() + "删除好友");
            task.setTaskType(SunTaskType.DELETE_FRIEND);
            task.setRobotId(writePrismRecordInfo.recordList.get(0).getFromUsername());
            task.setStatus("未下发");
            task.setCreateTime(new Date());
            task.setTaskGroupId(taskGroup.getId());
            taskService.insert(task);
        }
        log.info("删除任务粉丝数据");
        List<Integer> peopleIdList = taskPeopleService.listIdByPersonalWxIdAndUserWxId(byId.getWxId(), writePrismRecordInfo.recordList.get(0).getFromUsername());
        taskPeopleService.deleteBatchIds(peopleIdList);
        log.info("删除个人号好友数据");
        List<Integer> personalNoFriendsIdList = friendsService.listByPersonalWxIdAndUserWxId(byId.getWxId(), writePrismRecordInfo.recordList.get(0).getFromUsername());
        friendsService.deleteBatchIds(personalNoFriendsIdList);
    }

//*
//     * 关键字任务
//     *
//     * @param writePrismRecordInfo
//     * @param currPR


    private boolean toKeyWordTask(@RequestBody WritePrismRecordInfo writePrismRecordInfo, PersonalNoPrismRecord currPR) {
        String content = writePrismRecordInfo.recordList.get(0).getContent();
        List<PersonalNoKeyword> list = keywordService.listAll();
        if (!VerifyUtils.isEmpty(content)) {
            for (PersonalNoKeyword personalNoKeyword : list) {
                if (content.equals(personalNoKeyword.getKeyword())) {
                    List<PersonalNoKeywordContent> keywordContentList = keywordContentServicec.listByKeywordId(personalNoKeyword.getId());
                    log.info("触发关键字");
                    PersonalNoPhoneTaskGroup taskGroup = new PersonalNoPhoneTaskGroup();
                    taskGroup.setStatus("未下发");
                    taskGroup.setCurrentRobotId(currPR.getToUsernames().get(0));
                    taskGroup.setNextStep(1);
                    taskGroup.setTotalStep(keywordContentList.size());
                    taskGroup.setTname(currPR.getToUsernames().get(0) + "发送关键字给" + currPR.getFromUsername());
                    taskGroup.setCreateTime(new Date());
                    taskGroup.setTaskOrder(0);
                    boolean save = taskGroupService.insert(taskGroup);
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
                        boolean save1 = taskService.insert(task);
                        if (!save1) {
                            log.error("插入关键词回复任务失败");
                            throw new RuntimeException("插入关键词回复任务失败");
                        }
                    }
                    return true;
                }
            }
        }
        return true;
    }

    //*
//     * 审核任务
//     *
//     * @param writePrismRecordInfo
//     * @param currPR
    private boolean toAuditTask(@RequestBody WritePrismRecordInfo writePrismRecordInfo, PersonalNoPrismRecord currPR) {
        PersonalNoPeople personalNoPeople;
        if (writePrismRecordInfo.recordList.get(0).getContent().contains("/robotFiles/imgMsg")) {
            personalNoPeople = taskPeopleService.getByPersonalIdAndUserId(currPR.getToUsernames().get(0), currPR.getFromUsername(), 2);
            if (!VerifyUtils.isEmpty(personalNoPeople)) {
                boolean b2 = taskPeopleService.updateFlagById(personalNoPeople.getId(),3);
                if (!b2) {
                    log.info("更新任务粉丝为已审核通过状态失败");
                    throw new RuntimeException("更新任务粉丝为已审核通过状态失败");
                }
                log.info("根据任务id获取要发送的任务信息");
                PersonalNoTask taskById = noTaskService.getTaskById(personalNoPeople.getPersonalTaskId());
                if (null == taskById.getMessage()) {
                    return true;
                }
                log.info("将回复信息转换为任务任务组");
                PersonalNoPhoneTaskGroup taskGroup = new PersonalNoPhoneTaskGroup();
                taskGroup.setTaskOrder(9);
                taskGroup.setCreateTime(new Date());
                taskGroup.setNextStep(1);
                taskGroup.setTotalStep(1);
                taskGroup.setCurrentRobotId(currPR.getToUsernames().get(0));
                taskGroup.setStatus("未下发");
                taskGroup.setTname(currPR.getToUsernames().get(0) + "发送回复消息" + currPR.getFromUsername());
                boolean save = taskGroupService.insert(taskGroup);
                if (!save) {
                    log.error("添加到任务组失败");
                    throw new RuntimeException("添加到任务组失败");
                }
                log.info("开始添加审核回复任务");
                PersonalNoPhoneTask task = new PersonalNoPhoneTask();
                task.setContent(taskById.getMessage());
                task.setContentType("文字");
                task.setStep(1);
                task.setTaskType(100);
                task.setTname(currPR.getToUsernames().get(0) + "发送自动回复审核消息给" + currPR.getFromUsername());
                task.setTaskGroupId(taskGroup.getId());
                task.setRobotId(currPR.getFromUsername());
                task.setCreateTime(new Date());
                task.setStatus("未下发");
                boolean save1 = taskService.insert(task);
                if (!save1) {
                    log.error("插入审核回复任务失败");
                    throw new RuntimeException("插入审核回复任务失败");
                }
                return false;
            }
        }
        return false;
    }

//*
//     * 开课提醒任务
//     * @param currPR
//     * @param taskById


    private void toAutoRmindTask(PersonalNoPrismRecord currPR, PersonalNoTask taskById) {
        boolean save;
        if ("0".equals(taskById.getAutoRemind())) {
            return;
        }
        long time1 = taskById.getTaskEndTime().getTime() - new Integer(Integer.parseInt(taskById.getAutoRemind())).longValue();
        if (new Date().getTime() < time1) {
            List<PersonalNoTaskBeginRemind> noTaskBeginRemindList = taskById.getNoTaskBeginRemindList();
            PersonalNoPhoneTaskGroup taskGroup1 = new PersonalNoPhoneTaskGroup();
            taskGroup1.setTaskOrder(1);
            log.info("开始计算开课提醒的时间");
            long time = taskById.getTaskStartTime().getTime();
            long autoTime = time - new Integer(Integer.parseInt(taskById.getAutoRemind()) * 60 * 1000).longValue();
            taskGroup1.setCreateTime(new Date(autoTime));
            taskGroup1.setTotalStep(noTaskBeginRemindList.size());
            taskGroup1.setTname(currPR.getToUsernames().get(0) + "发送开课提醒给" + currPR.getFromUsername());
            taskGroup1.setNextStep(1);
            taskGroup1.setCurrentRobotId(currPR.getToUsernames().get(0));
            taskGroup1.setStatus("未下发");
            save = taskGroupService.insert(taskGroup1);
            if (!save) {
                log.error("添加开课提醒任务组失败");
                throw new RuntimeException("添加开课提醒任务组失败");
            }
            for (int j = 0; j < noTaskBeginRemindList.size(); j++) {
                PersonalNoPhoneTask task = new PersonalNoPhoneTask();
                if ("邀请入群".equals(noTaskBeginRemindList.get(j).getContentType())) {
                    task.setTaskGroupId(taskGroup1.getId());
                    task.setStatus("未下发");
                    task.setTaskType(1102);
                    task.setTname(currPR.getToUsernames().get(0) + "发送入群邀请给" + currPR.getFromUsername());
                    task.setContent(noTaskBeginRemindList.get(j).getContent());
                    task.setRobotId(currPR.getFromUsername());
                    task.setContentType(noTaskBeginRemindList.get(j).getContentType());
                    task.setStep(j + 1);
                } else {
                    task.setContent(noTaskBeginRemindList.get(j).getContent());
                    task.setContentType(noTaskBeginRemindList.get(j).getContentType());
                    task.setStep(j + 1);
                    task.setTaskGroupId(taskGroup1.getId());
                    task.setRobotId(currPR.getFromUsername());
                    task.setStatus("未下发");
                    task.setTaskType(100);
                    task.setTname(currPR.getToUsernames().get(0) + "发送开课提醒消息给" + currPR.getFromUsername());
                }
                boolean save1 = taskService.insert(task);
                if (!save1) {
                    log.info("插入开课提醒任务失败");
                    throw new RuntimeException("插入开课提醒任务失败");
                }
            }
        }
    }
//
//*
//     * 自动回复任务
//     *
//     * @param currPR
//     * @param taskById
//

    private void addAutoReplyTask(PersonalNoPrismRecord currPR, PersonalNoTask taskById) {
        PersonalNoPhoneTaskGroup taskGroup = new PersonalNoPhoneTaskGroup();
        taskGroup.setCreateTime(new Date());
        taskGroup.setTname(currPR.getToUsernames().get(0) + "发送回复消息" + currPR.getFromUsername());
        taskGroup.setNextStep(1);
        taskGroup.setTaskOrder(9);
        taskGroup.setStatus("未下发");
        taskGroup.setTotalStep(taskById.getNoTaskReplyContentList().size());
        taskGroup.setCurrentRobotId(currPR.getToUsernames().get(0));
        boolean save = taskGroupService.insert(taskGroup);
        if (!save) {
            log.error("添加到任务组失败");
            throw new RuntimeException("添加到任务组失败");
        }
        log.info("开始添加任务");
        for (int j = 0; j < taskById.getNoTaskReplyContentList().size(); j++) {
            PersonalNoPhoneTask task = new PersonalNoPhoneTask();
            task.setContent(taskById.getNoTaskReplyContentList().get(j).getContent());
            task.setContentType(taskById.getNoTaskReplyContentList().get(j).getContentType());
            task.setStep(j + 1);
            task.setTname(currPR.getToUsernames().get(0) + "发送自动回复消息给" + currPR.getFromUsername());
            task.setTaskGroupId(taskGroup.getId());
            task.setRobotId(currPR.getFromUsername());
            task.setCreateTime(new Date());
            task.setStatus("未下发");
            task.setTaskType(100);
            boolean save1 = taskService.insert(task);
            if (!save1) {
                log.info("插入任务失败");
                throw new RuntimeException("插入任务失败");
            }
        }
    }

    //转发消息给运营人员
    private void sendMessageToManager(@RequestBody WritePrismRecordInfo writePrismRecordInfo, List<String> personlaWxIdList) {
        if (!VerifyUtils.isEmpty(writePrismRecordInfo.recordList.get(0).getToUsernames().get(0)) && !VerifyUtils.isEmpty(writePrismRecordInfo.recordList.get(0).getFromUsername())) {
            if (personlaWxIdList.contains(writePrismRecordInfo.recordList.get(0).getToUsernames().get(0))) {
                PersonalNo byWxId = noService.getByWxId(writePrismRecordInfo.recordList.get(0).getToUsernames().get(0));
                if (VerifyUtils.isEmpty(byWxId.getSuperId())) {
                    return;
                }
                PersonalNoSuperuesr byId = superuesrService.selectById(byWxId.getSuperId());
                if (VerifyUtils.isEmpty(byId)) {
                    return;
                }
                PersonalNoPhoneTaskGroup taskGroup = new PersonalNoPhoneTaskGroup();
                if (byId.getWxId().equals(writePrismRecordInfo.recordList.get(0).getFromUsername())) {
                    log.info("将用户消息转发给某个微信好友");
                    String[] split = writePrismRecordInfo.recordList.get(0).getContent().split("@");
                    if (split != null && split.length > 1) {
                        taskGroup.setTaskOrder(0);
                        taskGroup.setCreateTime(new Date());
                        taskGroup.setTname(writePrismRecordInfo.recordList.get(0).getToUsernames().get(0) + "转发消息给用户" + split[0]);
                        taskGroup.setNextStep(1);
                        taskGroup.setTotalStep(split.length - 1);
                        taskGroup.setCurrentRobotId(writePrismRecordInfo.recordList.get(0).getToUsernames().get(0));
                        taskGroup.setStatus("未下发");
                        boolean save = taskGroupService.insert(taskGroup);
                        if (save) {
                            for (int i = 1; i < split.length; i++) {
                                PersonalNoPhoneTask task = new PersonalNoPhoneTask();
                                task.setTaskGroupId(taskGroup.getId());
                                task.setContent(split[i]);
                                if (split[i].contains("/group1/M00")) {
                                    task.setContentType("图片");
                                } else {
                                    task.setContentType("文字");
                                }
                                task.setStep(i);
                                task.setStatus("未下发");
                                task.setRobotId(split[0]);
                                task.setTaskType(100);
                                task.setCreateTime(new Date());
                                taskService.insert(task);
                            }
                        }
                    }
                } else {
                    log.info("将消息转发给管理员");
                    taskGroup.setTaskOrder(0);
                    taskGroup.setCreateTime(new Date());
                    taskGroup.setTname(writePrismRecordInfo.recordList.get(0).getToUsernames().get(0) + "转发消息给运营");
                    taskGroup.setNextStep(1);
                    taskGroup.setTotalStep(1);
                    taskGroup.setCurrentRobotId(writePrismRecordInfo.recordList.get(0).getToUsernames().get(0));
                    taskGroup.setStatus("未下发");
                    boolean save = taskGroupService.insert(taskGroup);
                    if (save) {
                        PersonalNoPhoneTask task = new PersonalNoPhoneTask();
                        task.setTaskGroupId(taskGroup.getId());
                        PersonalNoUser byWxId1 = userService.getByWxId(writePrismRecordInfo.recordList.get(0).getFromUsername());
                        String nickName = "未知";
                        if (byWxId1 != null) {
                            nickName = byWxId1.getNickName();
                        }
                        task.setContent(noService.getByWxId(writePrismRecordInfo.recordList.get(0).getToUsernames().get(0)).getNickname() +
                                "收到\n昵称：" + nickName +
                                " \n微信id：" + writePrismRecordInfo.recordList.get(0).getFromUsername() + "  的消息，\n内容为: " + writePrismRecordInfo.recordList.get(0).getContent());
                        task.setContentType("文字");
                        task.setStep(1);
                        task.setStatus("未下发");
                        task.setRobotId(byId.getWxId());
                        task.setTaskType(100);
                        task.setCreateTime(new Date());
                        taskService.insert(task);
                    }
                }
            }
        }
    }

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
    public SunApiResponse addFriendRequest(@RequestBody FriendRequestInfoWarpper friendRequestInfoWarpper, HttpServletRequest request, HttpServletResponse response) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
            FriendRequestInfo friendRequestInfo = friendRequestInfoWarpper.friendRequestInfo;
            log.info(friendRequestInfoWarpper);
            PersonalNoOperationStockWechatAccount byId = wechatAccountService.getByLogicId(friendRequestInfoWarpper.logicId);
            PersonalNoBlacklist blacklist = blacklistService.getByWxId(friendRequestInfo.getUsername());
            if (!VerifyUtils.isEmpty(byId) && byId.getOperationProjectInstanceId() == G.ms_OPERATION_PROJECT_INSTANCE_ID) {
                if (VerifyUtils.isEmpty(blacklist)) {
                    log.info("不在黑名单内，开始处理");
                    PersonalNoFriends personalNoFriendList = friendsService.getByPersonalWxIdAndUserWxId(byId.getWxId(), friendRequestInfo.getUsername());
                    if (VerifyUtils.isEmpty(personalNoFriendList)) {
                        log.info("判断是否已经有此任务");
                        PersonalNoPhoneTaskGroup taskGroup1 = taskGroupService.getByPersonalWxIdAndTheme(byId.getWxId(), "" + byId.getWxId() + "添加好友" + friendRequestInfo.getUsername(), "未下发");
                        if (VerifyUtils.isEmpty(taskGroup1)) {
                            taskGroup1 = taskGroupService.getByPersonalWxIdAndTheme(byId.getWxId(), "" + byId.getWxId() + "添加好友" + friendRequestInfo.getUsername(), "已完成");
                            if (VerifyUtils.isEmpty(taskGroup1)) {
                                log.info("添加好友任务");
                                toAddFriendTask(friendRequestInfo, byId);
                            }
                        }
                        log.info("处理用户表的数据");
                        List<PersonalNoUser> userList = userService.getByNickName(friendRequestInfo.getNickname());
                        if (!VerifyUtils.collectionIsEmpty(userList)) {
                            PersonalNoUser user = userList.get(0);
                            if (!VerifyUtils.isEmpty(user)) {
                                user.setWxId(friendRequestInfo.getUsername());
                                boolean b = userService.updateById(user);
                                if (!b) {
                                    log.info("处理用户表失败");
                                    throw new RuntimeException("处理用户表失败");
                                }
                                log.info("处理用户粉丝表的数据");
                                List<PersonalNoPeople> peopleList = taskPeopleService.getByPersonalWxIdAndUserName(byId.getWxId(), user.getOpenid());
                                if (!VerifyUtils.collectionIsEmpty(peopleList)) {
                                    PersonalNoPeople people = peopleList.get(0);
                                    people.setPersonalFriendWxId(friendRequestInfo.getUsername());
                                    b = taskPeopleService.updateById(people);
                                    if (!b) {
                                        log.info("处理用户表失败");
                                        throw new RuntimeException("处理用户表失败");
                                    }
                                    log.info("处理好友信息的数据");
                                    PersonalNoFriends friends = friendsService.getByPersonalIdAndUserWxId(friendRequestInfoWarpper.logicId, friendRequestInfo.getUsername());
                                    if (VerifyUtils.isEmpty(friends)) {
                                        friends = new PersonalNoFriends();
                                    }
                                    friends.setPersonalNoId(friendRequestInfoWarpper.logicId);
                                    friends.setUserId(user.getId());
                                    friends.setUserWxId(friendRequestInfo.getUsername());
                                    friends.setPersonalNoWxId(byId.getWxId());
                                    friends.setBeFriendTime(new Date());
                                    boolean save1 = friendsService.insert(friends);
                                    if (!save1) {
                                        log.info("添加个人号好友信息失败");
                                        throw new RuntimeException("添加个人号好友信息失败");
                                    }
                                }
                            }
                        }
                        log.info("更新个人号好友数量和待通过好友人数");
                        PersonalNo byWxId = noService.getByWxId(byId.getWxId());
                        if (!VerifyUtils.isEmpty(byId)) {
                            byWxId.setWaitingPassNum(byWxId.getWaitingPassNum() > 0 ? byWxId.getWaitingPassNum() - 1 : 0);
                            byWxId.setFriendsNum(byWxId.getFriendsNum() + 1);
                            boolean b1 = noService.updateById(byWxId);
                            if (!b1) {
                                log.info("更新个人号好友数量和待通过好友人数失败");
                                throw new RuntimeException("更新个人号好友数量和待通过好友人数失败");
                            }
                        }
                    } else {
                        log.info("添加好友任务");
                        toAddFriendTask(friendRequestInfo, byId);
                    }
                }
            }
            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            return tempSunApiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            WebConst.insertRequseException(requestExceptionService, request, response, "" + friendRequestInfoWarpper.logicId + "  " + friendRequestInfoWarpper.friendRequestInfo);
            tempSunApiResponse.setCode(SunApiResponse.CODE_SYS_ERROR);
            return tempSunApiResponse;
        }
    }

    //添加好友任务
    private void toAddFriendTask(FriendRequestInfo friendRequestInfo, PersonalNoOperationStockWechatAccount byId) {
        PersonalNoPhoneTaskGroup taskGroup = new PersonalNoPhoneTaskGroup();
        taskGroup.setCreateTime(new Date());
        taskGroup.setTname(byId.getWxId() + byId.getWxId() + "添加好友" + friendRequestInfo.getUsername());
        taskGroup.setCurrentRobotId(byId.getWxId());
        taskGroup.setStatus("未下发");
        taskGroup.setTotalStep(1);
        taskGroup.setNextStep(1);
        taskGroup.setTaskOrder(10);
        boolean save2 = taskGroupService.insert(taskGroup);
        if (!save2) {
            log.info("添加好友任务组失败");
            throw new RuntimeException("添加好友任务组失败");
        }
        PersonalNoPhoneTask task = new PersonalNoPhoneTask();
        task.setStatus("未下发");
        task.setRobotId(friendRequestInfo.getUsername());
        task.setCreateTime(new Date());
        task.setTname(byId.getWxId() + "添加" + friendRequestInfo.getUsername() + "为好友");
        task.setStep(1);
        task.setTaskGroupId(taskGroup.getId());
        task.setTaskType(SunTaskType.FRIEND_ACCEPT_REQUEST);
        String s = JsonObjectUtils.objectToJson(friendRequestInfo);
        task.setTaskJson(s);
        task.setContent(friendRequestInfo.getUsername());
        boolean save3 = taskService.insert(task);
        if (!save3) {
            log.info("添加好友任务失败");
            throw new RuntimeException("添加好友任务失败");
        }
    }

    // 向服务器报告错误
    @PostMapping("reportErrors.do")
    public SunApiResponse reportErrors(@RequestBody ReportErrorsInfo reportErrorsInfo, HttpServletRequest request, HttpServletResponse response) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
            log.error("*********************************************************************************************");
            log.error("手机报错了！！！");
            log.error(reportErrorsInfo.errorList.get(0));
            log.error("*********************************************************************************************");
            WebConst.insertRequseException(requestExceptionService, request, response, reportErrorsInfo.errorList.get(0).getMessage());
            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            return tempSunApiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
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
    public SunApiResponse uploadFriendList(@RequestBody UploadFriendListInfo uploadFriendListInfo, HttpServletRequest request, HttpServletResponse response) {
        SunApiResponse tempSunApiResponse = new SunApiResponse();
        try {
            log.info("处理个人号好友表");
            List<PersonalNoFriends> friends = friendsService.listByPersonalWxId(uploadFriendListInfo.username);
            Iterator<PersonalNoFriends> iterator = friends.iterator();
            while (iterator.hasNext()) {
                PersonalNoFriends next = iterator.next();
                if (uploadFriendListInfo.friendList.contains(next.getUserWxId())) {
                    iterator.remove();
                }
            }
            friendsService.removeByFriends(friends);
            log.info("处理个人号好友表");
            for (PersonalNoFriends friend : friends) {
                List<Integer> peopleIdList = taskPeopleService.listIdByPersonalWxIdAndUserWxId(uploadFriendListInfo.username, friend.getUserWxId());
                taskPeopleService.deleteBatchIds(peopleIdList);
            }
            log.info("处理原有的微信好友");
            PersonalNoOperationStockWechatAccount noByWxIdAndInstanceId = wechatAccountService.getByWxIdAndInstanceId(uploadFriendListInfo.username, 0);
            for (String s : uploadFriendListInfo.friendList) {
                PersonalNoFriends byPersonalWxIdAndUserWxId = friendsService.getByPersonalWxIdAndUserWxId(uploadFriendListInfo.username, s);
                if (VerifyUtils.isEmpty(byPersonalWxIdAndUserWxId)) {
                    PersonalNoFriends newFriends = new PersonalNoFriends();
                    newFriends.setUserWxId(s);
                    newFriends.setPersonalNoWxId(uploadFriendListInfo.username);
                    PersonalNoUser userByWxId = userService.getByWxId(s);
                    newFriends.setUserId(userByWxId == null ? null : userByWxId.getId());
                    newFriends.setPersonalNoId(noByWxIdAndInstanceId == null ? null : noByWxIdAndInstanceId.getId());
                    newFriends.setBeFriendTime(new Date());
                    friendsService.insert(newFriends);
                }
            }
            tempSunApiResponse.setCode(SunApiResponse.CODE_SUCCESS);
            return tempSunApiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            WebConst.insertRequseException(requestExceptionService, request, response, uploadFriendListInfo.username + uploadFriendListInfo.friendList);
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

    // signContent4OSS_应该是跟阿里云OSS相关
    @GetMapping("signContent4OSS.do")
    public String signContent4OSSGet() {
        JSONObject ja1 = getSignContent4OSSJsonObject();
        return ja1.toString();
    }

    @PostMapping("signContent4OSS.do")
    public String signContent4OSS() {
        //SignContent4OSSInfo signContent4OSSInfo = G.ms_om.readValue(requestInfo, SignContent4OSSInfo.class);
        JSONObject ja1 = getSignContent4OSSJsonObject();
        return ja1.toString();
    }

    private JSONObject getSignContent4OSSJsonObject() {
        Map<String, String> respMap = new LinkedHashMap<String, String>();
        try {
            // 读取配置文件。
            Properties prop = new Properties();
            prop.load(new FileInputStream("F:/myproject/Idea/dev/personalDevRobot/src/main/resources/oss_config.properties"));
            String accessKeyId = prop.getProperty("AccessKeyID");
            String accessKeySecret = prop.getProperty("AccessKeySecret");
            String roleArn = prop.getProperty("RoleArn");
            long durationSeconds = new Long(prop.getProperty("TokenExpireTime"));
            String policyFile = prop.getProperty("PolicyFile");
            // 只有 RAM用户（子账号）才能调用 AssumeRole 接口
            // 阿里云主账号的AccessKeys不能用于发起AssumeRole请求
            // 请首先在RAM控制台创建一个RAM用户，并为这个用户创建AccessKeys
            // RoleArn 需要在 RAM 控制台上获取
            String policy = ReadJson(policyFile);
            // RoleSessionName 是临时Token的会话名称，自己指定用于标识你的用户，主要用于审计，或者用于区分Token颁发给谁
            // 但是注意RoleSessionName的长度和规则，不要有空格，只能有'-' '_' 字母和数字等字符
            // 具体规则请参考API文档中的格式要求
            String roleSessionName = "alice-001";

            // 此处必须为 HTTPS
            ProtocolType protocolType = ProtocolType.HTTPS;

            final AssumeRoleResponse stsResponse = assumeRole(accessKeyId, accessKeySecret, roleArn, roleSessionName, policy, protocolType, durationSeconds);

            respMap.put("StatusCode", "200");
            respMap.put("AccessKeyId", stsResponse.getCredentials().getAccessKeyId());
            respMap.put("AccessKeySecret", stsResponse.getCredentials().getAccessKeySecret());
            respMap.put("SecurityToken", stsResponse.getCredentials().getSecurityToken());
            respMap.put("Expiration", stsResponse.getCredentials().getExpiration());
        } catch (ClientException e) {
            e.printStackTrace();
            respMap.put("StatusCode", "500");
            respMap.put("ErrorCode", e.getErrCode());
            respMap.put("ErrorMessage", e.getErrCode());
        } catch (Exception e) {
            e.printStackTrace();
            respMap.put("StatusCode", "500");
            respMap.put("ErrorMessage", e.getMessage());
        }

        return JSONObject.fromObject(respMap);
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

    /**
     * 读取配置文件
     *
     * @param path
     * @return
     */
    private static String ReadJson(String path) {
        //从给定位置获取文件
        File file = new File(path);
        BufferedReader reader = null;
        //返回值,使用StringBuffer
        StringBuffer data = new StringBuffer();
        //
        try {
            reader = new BufferedReader(new FileReader(file));
            //每次读取文件的缓存
            String temp = null;
            while ((temp = reader.readLine()) != null) {
                data.append(temp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭文件流
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data.toString();
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
            G.e(e);
            tempSunApiResponse.setCode(SunApiResponse.CODE_SYS_ERROR);
            return tempSunApiResponse;
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void updateLastConnectInfo(String logicId, String wx_id, HttpServletRequest request) {
        try {
            PersonalNoOperationStockWechatAccount tempOSWA = null;
            if (null != logicId && (!logicId.equals(""))) {
//                tempOSWA = new OperationStockWechatAccount().dao().findFirst("select * from operation_stock_wechat_account where id=" + logicId + " and operation_project_instance_id=" + G.ms_OPERATION_PROJECT_INSTANCE_ID);
                tempOSWA = wechatAccountService.getByIdAndInstanceId(Integer.parseInt(logicId), G.ms_OPERATION_PROJECT_INSTANCE_ID);
            } else if (tempOSWA == null && null != wx_id && (!wx_id.equals(""))) {
//                tempOSWA = new OperationStockWechatAccount().dao().findFirst("select * from operation_stock_wechat_account where wx_id='" + wx_id + "' and operation_project_instance_id=" + G.ms_OPERATION_PROJECT_INSTANCE_ID);
                tempOSWA = wechatAccountService.getByWxIdAndInstanceId(wx_id, G.ms_OPERATION_PROJECT_INSTANCE_ID);
            }
            if (null == tempOSWA) {
                throw new RuntimeException("当前工程实例中不存在该微信号：logicId=" + logicId + ",wx_id=" + wx_id);
            } else {
                tempOSWA.setLastConnectTime(new Date());
                tempOSWA.setLastConnectInternetIp(request.getRemoteHost());
                boolean b = wechatAccountService.updateByLogicId(tempOSWA);
                if (!b) {
                    throw new RuntimeException("数据库更新失败");
                }
            }
        } catch (Exception e) {
            G.e(e);
        }
    }
}
