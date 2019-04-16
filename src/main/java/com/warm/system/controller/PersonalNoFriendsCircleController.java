package com.warm.system.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.warm.entity.PageResult;
import com.warm.entity.R;
import com.warm.entity.query.QueryFriendsCircle;
import com.warm.system.entity.PersonalNoFriendsCircle;
import com.warm.system.entity.PersonalNoFriendsCirclePersonal;
import com.warm.system.entity.PersonalNoFriendsCirclePhoto;
import com.warm.system.entity.PersonalNoSuperuesr;
import com.warm.system.service.db1.PersonalNoFriendsCirclePersonalService;
import com.warm.system.service.db1.PersonalNoFriendsCirclePhotoService;
import com.warm.system.service.db1.PersonalNoFriendsCircleService;
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
@Api(description = "朋友圈管理")
@RestController
@RequestMapping("noFriendsCircleManager")
public class PersonalNoFriendsCircleController {
    private static Log log = LogFactory.getLog(PersonalNoFriendsCircleController.class);
    @Autowired
    private PersonalNoFriendsCircleService noFriendsCircleService;
    @Autowired
    private PersonalNoFriendsCirclePersonalService noFriendsCirclePersonalService;
    @Autowired
    private PersonalNoFriendsCirclePhotoService circlePhotoService;

    @ApiOperation(value = "查询所有朋友圈列表")
    @GetMapping
    public R list(){
        try {
            log.info("开始查询所有朋友圈");
            List<PersonalNoFriendsCircle> personalList = noFriendsCircleService.selectList(null);
            log.info("根据朋友圈id查询对应的个人号数量");
            for (PersonalNoFriendsCircle noFriendsCircle : personalList) {
                List<PersonalNoFriendsCirclePersonal> personals = noFriendsCirclePersonalService.listByCircleId(noFriendsCircle.getId());
                noFriendsCircle.setPersonalNum(personals==null?0:personals.size());
            }
            log.info("查询成功返回数据列表");
            return R.ok().data(personalList);
        }catch (Exception e){
            e.printStackTrace();
            return R.error().message(e.getMessage());
        }
    }

    @ApiOperation(value = "分页查询朋友圈列表")
    @PostMapping(value = "{pageNum}/{size}/")
    public R pageQuery(
            @ApiParam(name = "pageNum", value = "当前页码", required = true)
            @PathVariable Long pageNum,

            @ApiParam(name = "size", value = "每页记录数", required = true)
            @PathVariable Long size,

            @ApiParam(name = "searchObj", value = "查询条件", required = true)
            @RequestBody QueryFriendsCircle searchObj

    ){
        try {
            log.info("开始条件查询朋友圈");
            Page<PersonalNoFriendsCircle> page = new Page<>(VerifyUtils.setPageNum(pageNum), VerifyUtils.setSize(size));
            noFriendsCircleService.pageQuery(page, searchObj);
            log.info("根据朋友圈id查询对应的个人号数量，朋友圈图片列表");
            for (PersonalNoFriendsCircle noFriendsCircle : page.getRecords()) {
                List<PersonalNoFriendsCirclePersonal> personals = noFriendsCirclePersonalService.listByCircleId(noFriendsCircle.getId());
                noFriendsCircle.setPersonalNum(personals.size());
                List<PersonalNoFriendsCirclePhoto> photoList = circlePhotoService.listByCircleId(noFriendsCircle.getId());
                noFriendsCircle.setPhotoList(photoList);
            }
            if(!VerifyUtils.collectionIsEmpty(page.getRecords())){
                log.info("开始拼接朋友圈类型");
                for (PersonalNoFriendsCircle row : page.getRecords()) {
                    String circleType = getCircleType(row);
                    row.setCircleType(circleType);
                }
            }
            log.info("分页条件查朋友圈成功,返回数据");
            return R.ok().data(page);
        }catch (Exception e){
            e.printStackTrace();
            return R.error().message(e.getMessage());
        }
    }
    //返回朋友圈类型
    private String getCircleType(PersonalNoFriendsCircle row) {
        StringBuffer temp = new StringBuffer();
        boolean flag = false;
        if(!VerifyUtils.isEmpty(row.getFriendsCircleOfficial())){
            temp.append("文案");
            flag = true;
        }
        if(!VerifyUtils.isEmpty(row.getCardUrl())){
            if(flag){
                temp.append("+");
            }
            temp.append("卡片");
            flag = true;
        }
        if(!VerifyUtils.collectionIsEmpty(row.getPhotoList())){
            if(flag){
                temp.append("+");
            }
            temp.append("" + row.getPhotoList().size() +  "图片");
        }
        return temp.toString();
    }

    @ApiOperation(value = "插入朋友圈")
    @PostMapping(value = "addCircle")
    public R addCircle(
            @ApiParam(name = "noFriendsCircle", value = "要添加的朋友圈信息", required = true)
            @RequestBody  PersonalNoFriendsCircle noFriendsCircle,
            HttpServletRequest request
    ){
        try {
            log.info("添加朋友圈信息开始");
            log.info("添加超级用户id");
            PersonalNoSuperuesr superuesr = (PersonalNoSuperuesr) SessionUtil.getSession(request, WebConst.SUPERUSER);
            boolean result =  noFriendsCircleService.insert(noFriendsCircle  , superuesr.getId());
            if(!result){
                log.info("添加朋友圈信息失败");
                return R.error().message("新增朋友圈失败");
            }
            log.info("添加朋友圈信息成功");
            return R.ok();
        }catch (Exception e){
            e.printStackTrace();
            return R.error().message(e.getMessage());
        }
    }

    @ApiOperation(value = "根据朋友圈id查询朋友圈")
    @PostMapping(value = "{id}")
    public R getById(
            @ApiParam(name = "id", value = "要查找的朋友圈id", required = true)
            @PathVariable("id") Integer id
    ){
        try {
            log.info("根据id查询朋友圈");
            PersonalNoFriendsCircle noFriendsCircle = noFriendsCircleService.getCircleById(id);
            String circleType = getCircleType(noFriendsCircle);
            noFriendsCircle.setCircleType(circleType);
            log.info("根据id查询朋友圈结束");
            return R.ok().data(noFriendsCircle);
        }catch (Exception e){
            e.printStackTrace();
            return R.error().message(e.getMessage());
        }
    }
}

