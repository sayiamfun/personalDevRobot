package com.warm.system.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.warm.entity.R;
import com.warm.system.entity.PersonalNoLableCategory;
import com.warm.system.entity.PersonalNoSuperuesr;
import com.warm.system.service.db1.PersonalNoLableCategoryService;
import com.warm.utils.SessionUtil;
import com.warm.utils.VerifyUtils;
import com.warm.utils.WebConst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.Date;
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
@Api(description = "粉丝标签类别管理")
@RestController
@RequestMapping("/personalLableCategoryManager")
public class PersonalNoLableCategoryController {
    private static Log log = LogFactory.getLog(PersonalNoLableCategoryController.class);
    @Autowired
    private PersonalNoLableCategoryService noLableCategoryService;

    @ApiOperation(value = "根据id删除标签类别信息")
    @DeleteMapping("deleteById/{id}/")
    public R delete(
            @ApiParam(name = "id", value = "标签类别id", required = true)
            @PathVariable("id") Integer id
    ){
        try {
            boolean b = noLableCategoryService.deleteById(id);
            if(!b){
                return R.error().message("删除标签类别失败");
            }
            return R.ok().data(null);
        }catch (Exception e){
            e.printStackTrace();
            return R.error().message("网页走丢了，请刷新后重试。。。");
        }
    }

    @ApiOperation(value = "根据id查询标签类别信息")
    @GetMapping("getById/{id}/")
    public R getById(
            @ApiParam(name = "id", value = "标签类别id", required = true)
            @PathVariable("id") PersonalNoLableCategory id
    ){
        try {
            log.info("根据id查询类别信息");
            PersonalNoLableCategory byId = noLableCategoryService.selectById(id);
            log.info("根据id查询类别信息结束");
            return R.ok().data(byId);
        }catch (Exception e){
            e.printStackTrace();
            return R.error().message("网页走丢了，请刷新后重试。。。");
        }
    }

    @ApiOperation(value = "添加标签类别信息")
    @PostMapping("addLableCategory")
    public R addLableCategory(
            @ApiParam(name = "lableCategory", value = "标签类别信息", required = true)
            @RequestBody PersonalNoLableCategory lableCategory,
            HttpServletRequest request
    ){
        try {
            log.info("添加类别信息");
            if(VerifyUtils.isEmpty(lableCategory)){
                log.info("要添加的类别信息为空");
                return R.error().message("要添加的类别信息为空");
            }
            lableCategory.setCreateTime(new Date());
            PersonalNoSuperuesr superuesr = (PersonalNoSuperuesr) SessionUtil.getSession(request, WebConst.SUPERUSER);
            lableCategory.setSuperId(superuesr.getId());
            boolean save = false;
            if(VerifyUtils.isEmpty(lableCategory.getId())){
                save = noLableCategoryService.insert(lableCategory);
            }else {
                save = noLableCategoryService.updateById(lableCategory);
            }
            if(!save){
                log.info("添加标签类别失败");
                return R.error().message("添加标签类别失败");
            }
            log.info("添加标签类别成功");

            return R.ok().data(null);
        }catch (Exception e){
            e.printStackTrace();
            return R.error().message("网页走丢了，请刷新后重试。。。");
        }
    }

    @ApiOperation(value = "查询所有粉丝标签类别列表")
    @GetMapping("/{name}/")
    public R list(
            @ApiParam(name = "lableName", value = "标签类别名称", required = true)
            @PathVariable("name") String name
    ){
        try {
            log.info("查询所有粉丝标签类别列表开始");
            List<PersonalNoLableCategory> personalList = noLableCategoryService.listByName(name);
            List<PersonalNoLableCategory> resultList = noLableCategoryService.getInfo(personalList);
            log.info("查询标签类别列表成功,返回数据");
            return R.ok().data(resultList);
        }catch (Exception e){
            e.printStackTrace();
            return R.error().message(e.getMessage());
        }
    }

    @ApiOperation(value = "分页查询粉丝标签类别列表")
    @GetMapping("{pageNum}/{size}/{categoryname}/")
    public R pageQuery(
            @ApiParam(name = "pageNum", value = "当前页码", required = true)
            @PathVariable("pageNum") Long pageNum,

            @ApiParam(name = "size", value = "每页记录数", required = true)
            @PathVariable("size") Long size,

            @ApiParam(name = "categoryname", value = "标签类别名称", required = true)
            @PathVariable("categoryname") String categoryname
    ){
        try {
            log.info("分页查询标签类别开始");
            Page<PersonalNoLableCategory> page = new Page<>(VerifyUtils.setPageNum(pageNum), VerifyUtils.setSize(size));
            noLableCategoryService.pageList(page, categoryname);
            List<PersonalNoLableCategory> info = noLableCategoryService.getInfo(page.getRecords());
            log.info("分页查询标签类别结束");
            return R.ok().data(info);
        }catch (Exception e){
            e.printStackTrace();
            return R.error().message("网页走丢了，请刷新后重试。。。");
        }
    }

}

