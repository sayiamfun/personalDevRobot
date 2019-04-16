package com.warm.system.controller;


import com.warm.entity.R;
import com.warm.system.entity.PersonalNoGroupCategorySet;
import com.warm.system.service.db3.PersonalNoGroupCategorySetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
@CrossOrigin //跨域
@Api(description = "群类别集合管理")
@RestController
@RequestMapping("/groupCategorySet")
public class PersonalNoGroupCategorySetController {

    private static Log log = LogFactory.getLog(PersonalNoGroupCategorySetController.class);
    @Autowired
    private PersonalNoGroupCategorySetService groupCategorySetService;

    @ApiOperation(value = "查询所有的群类别集合")
    @GetMapping()
    public R listAll(){
        try {
            log.info("查询所有的类别集合");
            List<PersonalNoGroupCategorySet> list = groupCategorySetService.listAll();
            log.info("查询所有的类别集合结束");
            return R.ok().data(list);
        }catch (Exception e){
            e.printStackTrace();
            return R.error().message("网页走丢了，请刷新。。。");
        }
    }

}

