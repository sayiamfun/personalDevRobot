package com.warm.entity.robot;

import com.warm.system.entity.PersonalNoRequestException;
import com.warm.system.service.db1.PersonalNoRequestExceptionService;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

public class G
{

	public static int ms_OPERATION_PROJECT_INSTANCE_ID = 8;		//手机实例，不同实例对应不同项目
	public static String ms_currProjectInstanceName = "线下测试个人号";   //手机实例名称


	public static String oss_config = "F:/myproject/Idea/dev/personalDevRobot/src/main/resources/oss_config.properties";//手机上传二维码配置文件
	public static String qrurl = "http://www.jiazhang111.xyz:9091";//旧二维码文件服务器
	// 微信公众号配置
	public static String WX_APPID = "wx8e17aa77af6c4ae3";		// 线下对应好孩子智慧屋
	public static String WX_SECRET = "702c3bfc75d8bed0bfe105679513c7d0";
	public static String WX_GRANT_TYPE = "authorization_code";

	// 日志控制——机器人
	public static boolean ms_IS_LOG_ROBOT_INFO_TO_DB;		// = true;
	public static boolean ms_IS_LOG_ROBOT_INFO_TO_FILE;		// = false;

	// 运行时状态辅助
	public static HashMap<String, Integer> ms_robotStatusCodeMap = new HashMap<String, Integer>();
	public static Integer ms_ROBOT_STATUS_START_CODE = 10000;


	public static void i(int logicId, String s, String s1) {
		System.err.println(logicId + ":" + s + ":" + s1);
	}

	public static void requestException(String dbRequestException, PersonalNoRequestExceptionService requestExceptionService, HttpServletRequest request, String requestBody, String remarks, String responseBody, Integer code) {
		PersonalNoRequestException exception = new PersonalNoRequestException();
		exception.setMethod(request.getMethod());
		exception.setCreateTime(new Date());
		exception.setUrl(request.getRequestURI());
		exception.setStatusCode(code);
		exception.setRequestBody(requestBody);
		exception.setResponseBody(responseBody);
		exception.setRemarks(remarks);
		exception.setDb(dbRequestException);
		requestExceptionService.add(exception);
	}
}
