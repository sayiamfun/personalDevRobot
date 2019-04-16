package com.warm.system.controller;

import com.warm.entity.R;
import com.warm.system.entity.PersonalNoOperationStockWechatAccount;
import com.warm.system.service.db2.PersonalNoOperationStockWechatAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin //跨域
@Api(description = "测试")
@RestController
@RequestMapping("/test")
public class Test {
    @Autowired
    private PersonalNoOperationStockWechatAccountService personalNoOperationStockWechatAccountService;

    @ApiOperation(value = "测试")
    @PostMapping(value = "list")
    public R list(){
        try {
            personalNoOperationStockWechatAccountService.listByInstanceId(0);
            return R.ok().data("");
        }catch (Exception e){
            e.printStackTrace();
            return R.error().message("网页走丢了，请刷新。。。");
        }
    }
}
