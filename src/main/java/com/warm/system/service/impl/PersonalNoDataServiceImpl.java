package com.warm.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.warm.entity.query.QueryPersonalData;
import com.warm.entity.result.ResultPersonalData;
import com.warm.entity.result.ResultPersonalDataWithTask;
import com.warm.entity.result.ResultPersonalDataWithTime;
import com.warm.system.entity.PersonalNoData;
import com.warm.system.mapper.PersonalNoDataMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.system.service.db1.PersonalNoDataService;
import com.warm.utils.VerifyUtils;
import com.warm.utils.WebConst;
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
public class PersonalNoDataServiceImpl extends ServiceImpl<PersonalNoDataMapper, PersonalNoData> implements PersonalNoDataService {
    private static Log log = LogFactory.getLog(PersonalNoDataServiceImpl.class);
    @Autowired
    private PersonalNoDataMapper dataMapper;
    /**
     * 根据条件分页查询个人号相关数据
     * @param page
     * @param queryPersonalData
     */
    @Override
    public void pageQuery(Page<PersonalNoData> page, QueryPersonalData queryPersonalData) {
        log.info("开始处理数据查询条件");
        String noTaskName = queryPersonalData.getNoTaskName();
        String personalnoChannelName = queryPersonalData.getPersonalnoChannelName();
        Date startTime = queryPersonalData.getStartTime();
        Date endTime = queryPersonalData.getEndTime();
        EntityWrapper<PersonalNoData> entityWrapper = new EntityWrapper<>();
        entityWrapper.orderDesc(Arrays.asList(new String[]{"date"}));
        if(!VerifyUtils.isEmpty(noTaskName)){
            log.info("根据任务名称查询");
            entityWrapper.eq("task_name", noTaskName);
        }
        if(!VerifyUtils.isEmpty(startTime) && !VerifyUtils.isEmpty(endTime)){
            log.info("根据时间查询");
            entityWrapper.between("date", startTime, endTime);
        }
        baseMapper.selectPage(page, entityWrapper);
    }

    /**
     * 开始处理返回数据
     * 总数据
     * 根据时间数据
     * 根据任务数据
     * @param records
     * @return
     */
    @Override
    public ResultPersonalData getInfoByDateList(List<PersonalNoData> records) {
        ResultPersonalData resultPersonalData = null;
        Map<String, ResultPersonalDataWithTime> resultPersonalDataWithTimeMap = new HashMap<>();
        Map<String, ResultPersonalDataWithTask> resultPersonalDataWithTaskMap = new HashMap<>();
        if(!VerifyUtils.collectionIsEmpty(records)){
            log.info("查询到数据，开始处理");
            ResultPersonalDataWithTime dataWithTime = new ResultPersonalDataWithTime();
            ResultPersonalDataWithTask dataWithTask = new ResultPersonalDataWithTask();
            for (PersonalNoData record : records) {
                log.info("处理时间数据");
                if(resultPersonalDataWithTimeMap.containsKey(WebConst.getNowDateNotHour(record.getDate()))){
                    log.info("已经存在此时间，修改数量");
                    dataWithTime = resultPersonalDataWithTimeMap.get(WebConst.getNowDateNotHour(record.getDate()));
                    dataWithTime.setToPeopleNum(dataWithTime.getToPeopleNum() + record.getToPeopleNum());
                    dataWithTime.setAddFriendsNum(dataWithTime.getAddFriendsNum() + record.getAddFriendsNum());
                    dataWithTime.setJoinGroupNum(dataWithTime.getJoinGroupNum() + record.getJoinGroupNum());
                    dataWithTime.setTodayKeep(dataWithTime.getTodayKeep() + record.getTodayKeep());
                }else {
                    log.info("不存在此时间，加入到map");
                    dataWithTime.setDate(record.getDate());
                    dataWithTime.setAddFriendsNum(record.getAddFriendsNum());
                    dataWithTime.setJoinGroupNum(record.getJoinGroupNum());
                    dataWithTime.setTodayKeep(record.getTodayKeep());
                    dataWithTime.setToPeopleNum(record.getToPeopleNum());
                }
                dataWithTime.setTheRateOfAddFriends(WebConst.div(dataWithTime.getToPeopleNum(), dataWithTime.getAddFriendsNum(), 2));
                log.info("处理任务数据");
                if(resultPersonalDataWithTaskMap.containsKey(record.getTaskName())){
                    log.info("已经存在此任务，修改数量");
                    dataWithTask = resultPersonalDataWithTaskMap.get(record.getTaskName());
                    dataWithTask.setToPeopleNum(dataWithTask.getToPeopleNum() + record.getToPeopleNum());
                    dataWithTask.setAddFriendsNum(dataWithTask.getAddFriendsNum() + record.getAddFriendsNum());
                    dataWithTask.setJoinGroupNum(dataWithTask.getJoinGroupNum() + dataWithTask.getJoinGroupNum());
                    dataWithTask.setTodayKeep(dataWithTask.getTodayKeep() + record.getTodayKeep());
                }else {
                    dataWithTask.setToPeopleNum(record.getToPeopleNum());
                    dataWithTask.setAddFriendsNum(record.getAddFriendsNum());
                    dataWithTask.setJoinGroupNum(record.getJoinGroupNum());
                    dataWithTask.setTodayKeep(record.getTodayKeep());
                    dataWithTask.setTaskName(record.getTaskName());
                }
                dataWithTask.setTheRateOfAddFriends(WebConst.div(dataWithTask.getToPeopleNum(), dataWithTask.getAddFriendsNum(), 2));
                resultPersonalDataWithTimeMap.put(WebConst.getNowDateNotHour(record.getDate()), dataWithTime);
                resultPersonalDataWithTaskMap.put(record.getTaskName(), dataWithTask);
                log.info("处理最外层数据");
                if(resultPersonalData == null) {
                    resultPersonalData = new ResultPersonalData();
                    resultPersonalData.setToPeopleNum(record.getToPeopleNum());
                    resultPersonalData.setAddFriendsNum(record.getAddFriendsNum());
                    resultPersonalData.setJoinGroupNum(record.getJoinGroupNum());
                    resultPersonalData.setTodayKeep(record.getTodayKeep());
                }else {
                    resultPersonalData.setToPeopleNum(resultPersonalData.getToPeopleNum() + record.getToPeopleNum());
                    resultPersonalData.setAddFriendsNum(resultPersonalData.getAddFriendsNum() + record.getAddFriendsNum());
                    resultPersonalData.setJoinGroupNum(resultPersonalData.getJoinGroupNum() + record.getJoinGroupNum());
                    resultPersonalData.setTodayKeep(resultPersonalData.getTodayKeep() + record.getTodayKeep());
                }
                resultPersonalData.setTheRateOfAddFriends(WebConst.div(resultPersonalData.getToPeopleNum(), resultPersonalData.getAddFriendsNum(), 2));
            }
            log.info("将map转换为list");
            Collection<ResultPersonalDataWithTask> values = resultPersonalDataWithTaskMap.values();
            Collection<ResultPersonalDataWithTime> values1 = resultPersonalDataWithTimeMap.values();
            resultPersonalData.setResultPersonalDataWithTaskList(new ArrayList<ResultPersonalDataWithTask>(values));
            resultPersonalData.setResultPersonalDataWithTimeList(new ArrayList<ResultPersonalDataWithTime>(values1));
        }
        return resultPersonalData;
    }

    /**
     * 根据时间查询数据信息
     * @param toString
     * @return
     */
    @Override
    public List<PersonalNoData> listByDate(String toString) {
        return dataMapper.listByDate(toString);
    }

    /**
     * 根据时间删除数据
     * @param list
     * @return
     */
    @Override
    public boolean deleteByDate(List<PersonalNoData> list) {
        for (PersonalNoData noData : list) {
            baseMapper.deleteById(noData.getId());
        }
        return true;
    }
    /**
     * 根据时间和任务名称查询数据
     * @param taskName
     * @param date
     * @return
     */
    @Override
    public PersonalNoData getByTaskNameAndTime(String taskName, String date) {
        return dataMapper.getByTaskNameAndTime(date,taskName);
    }
}
