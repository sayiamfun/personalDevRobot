package com.warm.system.service.impl;

import com.warm.system.entity.PersonalNoTemp;
import com.warm.system.mapper.PersonalNoTempMapper;
import com.warm.system.service.db1.PersonalNoTempService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.system.service.db1.PersonalNoValueTableService;
import com.warm.utils.WebConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 存储用户的交互数据 服务实现类
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
@Service
public class PersonalNoTempServiceImpl extends ServiceImpl<PersonalNoTempMapper, PersonalNoTemp> implements PersonalNoTempService {
    @Autowired
    private PersonalNoTempMapper personalNoTempMapper;
    @Autowired
    private PersonalNoValueTableService valueTableService;
    /**
     * 根据个人号微信id和用户微信id查找
     * @param s
     * @param fromUsername
     * @return
     */
    @Override
    public PersonalNoTemp getByPersonalIdAndUserWxId(String s, String fromUsername) {
        return personalNoTempMapper.getByPersonalIdAndUserWxId(s,fromUsername);
    }

    @Override
    public void insertPersonalNoTemp(PersonalNoTemp personalNoTemp) {
        personalNoTempMapper.insert(personalNoTemp);
    }

    @Override
    public List<PersonalNoTemp> listByPersonalWxIdAndTimeAndFlag(String wxId, int i) {
        return personalNoTempMapper.listByPersonalWxIdAndTimeAndFlag(wxId, WebConst.getNowDate(new Date(new Date().getTime()-Integer.parseInt(valueTableService.getById(5).getValue())*1000)),i);
    }

    @Override
    public void updateFlagById(PersonalNoTemp personalNoTemp) {
        personalNoTempMapper.updateFlagById(personalNoTemp.getFlag(),personalNoTemp.getId());
    }

}
