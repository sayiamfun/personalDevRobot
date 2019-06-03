package com.warm.utils;

import com.warm.system.entity.PersonalNoKeywordContent;
import com.warm.system.entity.PersonalNoLableMessageSendContent;
import com.warm.system.entity.PersonalNoRequestException;
import com.warm.system.entity.PersonalNoTaskMessageSendContent;
import com.warm.system.service.db1.PersonalNoRequestExceptionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @param
 * @return
 */
public class WebConst {

    public static final String WECHATSTATUS = "封禁";

    private static int textNum = 0; //文字
    private static int photoNum = 0; //图片
    private static int cardNum = 0; //名片
    private static int urlNum = 0; // 链接
    private static int intoGroup = 0; // 邀请入群
    private static int smallProgramNum = 0; //小程序

    //做判断，生成格式：2（1条文字，1条邀请入群）
    private static void initNum() {
        textNum = 0; //文字
        photoNum = 0; //图片
        cardNum = 0; //名片
        urlNum = 0; // 链接
        intoGroup = 0; // 邀请入群
        smallProgramNum = 0; //小程序
    }

    public static String getTaskContentShow(List<PersonalNoTaskMessageSendContent> personalNoTaskMessageSendContents) {

        StringBuffer temp = new StringBuffer();
        temp.append("(");
        if (!VerifyUtils.collectionIsEmpty(personalNoTaskMessageSendContents)) {
            initNum();
            for (PersonalNoTaskMessageSendContent personalNoTaskMessageSendContent : personalNoTaskMessageSendContents) {
                if ("文字".equals(personalNoTaskMessageSendContent.getContentType())) {
                    textNum += 1;
                    continue;
                } else if ("图片".equals(personalNoTaskMessageSendContent.getContentType())) {
                    photoNum += 1;
                    continue;
                } else if ("名片".equals(personalNoTaskMessageSendContent.getContentType())) {
                    cardNum += 1;
                    continue;
                } else if ("链接".equals(personalNoTaskMessageSendContent.getContentType())) {
                    urlNum += 1;
                    continue;
                } else if ("邀请入群".equals(personalNoTaskMessageSendContent.getContentType())) {
                    intoGroup += 1;
                    continue;
                } else if ("小程序".equals(personalNoTaskMessageSendContent.getContentType())) {
                    smallProgramNum += 1;
                    continue;
                }
            }
            boolean flag = false;
            if (textNum > 0) {
                temp.append(textNum + "条文字");
                flag = true;
            }
            if (urlNum > 0) {
                if (flag) {
                    temp.append(",");
                }
                temp.append(urlNum + "条链接");
                flag = true;
            }
            if (intoGroup > 0) {
                if (flag) {
                    temp.append(",");
                }
                temp.append(intoGroup + "条入群邀请");
                flag = true;
            }
            if (cardNum > 0) {
                if (flag) {
                    temp.append(",");
                }
                temp.append(cardNum + "条名片");
                flag = true;
            }
            if (smallProgramNum > 0) {
                if (flag) {
                    temp.append(",");
                }
                temp.append(smallProgramNum + "条小程序");
                flag = true;
            }
            if (photoNum > 0) {
                if (flag) {
                    temp.append(",");
                }
                temp.append(photoNum + "条图片");
                flag = true;
            }
        }
        temp.append(")");
        return temp.toString();
    }

    //做判断，生成格式：2（1条文字，1条邀请入群）
    public static String getLableContentShow(List<PersonalNoLableMessageSendContent> personalNoLableMessageSendContentList) {

        StringBuffer temp = new StringBuffer();
        temp.append("(");
        if (!VerifyUtils.collectionIsEmpty(personalNoLableMessageSendContentList)) {
            initNum();
            for (PersonalNoLableMessageSendContent personalNoLableMessageSendContent : personalNoLableMessageSendContentList) {
                if ("文字".equals(personalNoLableMessageSendContent.getContentType())) {
                    textNum += 1;
                    continue;
                } else if ("图片".equals(personalNoLableMessageSendContent.getContentType())) {
                    photoNum += 1;
                    continue;
                } else if ("链接".equals(personalNoLableMessageSendContent.getContentType())) {
                    urlNum += 1;
                    continue;
                } else if ("小程序".equals(personalNoLableMessageSendContent.getContentType())) {
                    smallProgramNum += 1;
                    continue;
                } else if ("邀请入群".equals(personalNoLableMessageSendContent.getContentType())) {
                    intoGroup += 1;
                    continue;
                }else if ("名片".equals(personalNoLableMessageSendContent.getContentType())) {
                    cardNum += 1;
                    continue;
                }
            }
            boolean flag = false;
            if (textNum > 0) {
                temp.append(textNum + "条文字");
                flag = true;
            }
            if (cardNum > 0) {
                if (flag) {
                    temp.append(",");
                }
                temp.append(cardNum + "条名片");
                flag = true;
            }
            if (photoNum > 0) {
                if (flag) {
                    temp.append(",");
                }
                temp.append(photoNum + "条图片");
                flag = true;
            }
            if (intoGroup > 0) {
                if (flag) {
                    temp.append(",");
                }
                temp.append(intoGroup + "条入群邀请");
                flag = true;
            }
            if (smallProgramNum > 0) {
                if (flag) {
                    temp.append(",");
                }
                temp.append(smallProgramNum + "条小程序");
            }
            if (urlNum > 0) {
                if (flag) {
                    temp.append(",");
                }
                temp.append(urlNum + "条链接");
                flag = true;
            }
        }
        temp.append(")");
        return temp.toString();
    }

    //做判断，生成格式：2（1条文字，1条邀请入群）
    public static String getKeyWordContentShow(List<PersonalNoKeywordContent> personalNoKeywordContentList) {

        StringBuffer temp = new StringBuffer();
        temp.append("(");
        if (!VerifyUtils.collectionIsEmpty(personalNoKeywordContentList)) {
            initNum();
            for (PersonalNoKeywordContent personalNoLableMessageSendContent : personalNoKeywordContentList) {
                if ("小程序".equals(personalNoLableMessageSendContent.getContentType())) {
                    smallProgramNum += 1;
                    continue;
                }else if ("图片".equals(personalNoLableMessageSendContent.getContentType())) {
                    photoNum += 1;
                    continue;
                } else if ("名片".equals(personalNoLableMessageSendContent.getContentType())) {
                    cardNum += 1;
                    continue;
                } else if ("链接".equals(personalNoLableMessageSendContent.getContentType())) {
                    urlNum += 1;
                    continue;
                } else if ("邀请入群".equals(personalNoLableMessageSendContent.getContentType())) {
                    intoGroup += 1;
                    continue;
                }if ("文字".equals(personalNoLableMessageSendContent.getContentType())) {
                    textNum += 1;
                    continue;
                }
            }
            boolean flag = false;
            if (textNum > 0) {
                temp.append(textNum + "条文字");
                flag = true;
            }
            if (photoNum > 0) {
                if (flag) {
                    temp.append(",");
                }
                temp.append(photoNum + "条图片");
                flag = true;
            }
            if (cardNum > 0) {
                if (flag) {
                    temp.append(",");
                }
                temp.append(cardNum + "条名片");
                flag = true;
            }
            if (intoGroup > 0) {
                if (flag) {
                    temp.append(",");
                }
                temp.append(intoGroup + "条入群邀请");
                flag = true;
            }
            if (urlNum > 0) {
                if (flag) {
                    temp.append(",");
                }
                temp.append(urlNum + "条链接");
                flag = true;
            }
            if (smallProgramNum > 0) {
                if (flag) {
                    temp.append(",");
                }
                temp.append(smallProgramNum + "条小程序");
            }
        }
        temp.append(")");
        return temp.toString();
    }

    /**
     * 时间格式转换带时分秒
     * Date  to   String
     * @return
     */
    public static String getNowDate(Date currentTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    /**
     * 时间格式转换不带时分秒
     * Date   to   String
     * @return
     */
    public static String getNowDateNotHour(Date currentTime){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 时间格式转换不带时分秒
     * String   to   Date
     * @return
     */
    public static Date getDateByString(String date){
        DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        try {
            date1 = fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    /**
     * 时间格式转换不带时分秒
     * String   to   Date
     * @return
     */
    public static Date getDateHourByString(String date){
        DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        try {
            date1 = fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    /**
     * 除法运算
     * @param d1
     * @param d2
     * @param len
     * @return
     */
    public static double div(double d1,double d2,int len) {
        if(d2==0){
            return 0;
        }
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.divide(b2,len,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 根据传入时间获得明天凌晨0点时间
     * @return
     */
    public static String getTodayZeroTime(Date date) {
        return getNowDateNotHour(date)+" 00:00:00";
    }

    /**
     * 根据传入时间获得后天凌晨0点时间
     * @return
     */
    public static String getTomorrowZeroTime(Date date) {
        return getNowDateNotHour(new Date(date.getTime()+24*60*60*1000))+" 00:00:00";
    }
    /**
     * 根据传入时间获得明天早上8点时间
     * @return
     */
    public static String getTomorrowEightTime(Date date) {
        return getNowDateNotHour(new Date(date.getTime()+24*60*60*1000))+" 08:00:00";
    }
}
