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

	public static int ms_OPERATION_PROJECT_INSTANCE_ID = 8;		//=4;
	public static String ms_currProjectInstanceName = "线下测试个人号";


	// 微信公众号配置
	public static String WX_APPID = "wx84e37786f1e95df0";		// = "wx8e17aa77af6c4ae3";
	public static String WX_SECRET = "e98b8e91311915b74c8432bf34675491";   // = "702c3bfc75d8bed0bfe105679513c7d0";
	public static String WX_GRANT_TYPE = "authorization_code";		// = "authorization_code";

	// 日志控制——机器人
	public static boolean ms_IS_LOG_ROBOT_INFO_TO_DB;		// = true;
	public static boolean ms_IS_LOG_ROBOT_INFO_TO_FILE;		// = false;

	// 运行时状态辅助
	public static HashMap<String, Integer> ms_robotStatusCodeMap = new HashMap<String, Integer>();
	public static Integer ms_ROBOT_STATUS_START_CODE = 10000;

	public static byte[] readAsBytes(HttpServletRequest request)
	{

		int len = request.getContentLength();
		byte[] buffer = new byte[len];
		ServletInputStream in = null;

		try
		{
			in = request.getInputStream();
			in.read(buffer, 0, len);
			in.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (null != in)
			{
				try
				{
					in.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return buffer;
	}


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
