package com.warm.system.service.impl;

import com.warm.system.entity.PersonalNoGroupCategory;
import com.warm.system.mapper.PersonalNoGroupCategoryMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.system.service.db3.PersonalNoGroupCategoryService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
@Service
public class PersonalNoGroupCategoryServiceImpl extends ServiceImpl<PersonalNoGroupCategoryMapper, PersonalNoGroupCategory> implements PersonalNoGroupCategoryService {

    private static Log log = LogFactory.getLog(PersonalNoGroupCategoryServiceImpl.class);
    @Autowired
    private PersonalNoGroupCategoryMapper groupCategoryMapper;
    /**
     * 根据类别集合id查询所有的群类别
     * @param setId
     * @return
     */
    @Override
    public List<PersonalNoGroupCategory> listBySetId(Integer setId) {
        return groupCategoryMapper.listBySetId(setId);
    }
}
