package com.warm.system.controller;


import com.warm.entity.R;
import com.warm.system.entity.PersonalNo;
import com.warm.system.service.db1.PersonalNoFriendsService;
import com.warm.utils.VerifyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
@CrossOrigin //跨域
@Api(description = "粉丝（用户）管理")
@RestController
@RequestMapping("/personalNoUser")
public class PersonalNoUserController {
    private static Log log = LogFactory.getLog(PersonalNoUserController.class);
    @Autowired
    private PersonalNoFriendsService noFriendsService;

    @ApiOperation(value = "根据个人号查询个人号粉丝")
    @PostMapping(value = "getUserByPersonal")
    public R taskMessageSend(
            @ApiParam(name = "name", value = "要查找的标签类别", required = true)
            @RequestBody List<PersonalNo> list
    ){
        try {
            log.info("根据个人号查询个人号粉丝");
            if(VerifyUtils.collectionIsEmpty(list)){
                log.info("个人号列表为空");
                return R.error().message("个人号列表为空");
            }
            Set<Integer> friendsIdSet = noFriendsService.listByPersonalList(list);
            return R.ok().data(friendsIdSet.size());
        }catch (Exception e){
            e.printStackTrace();
            return R.error().message("网页走丢了，请刷新后重试。。。");
        }
    }
}

