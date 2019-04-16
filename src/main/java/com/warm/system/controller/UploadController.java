package com.warm.system.controller;

import com.warm.entity.R;
import com.warm.system.service.db1.UploadService;
import com.warm.utils.ImageUpload.ImageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin //跨域
@Api(description = "文件上传")
@RestController
@RequestMapping("/upload")
public class UploadController {

    public static Log log = LogFactory.getLog(UploadController.class);

    @Autowired
    private UploadService uploadService;

    @ApiOperation(value = "上传图片")
    @PostMapping(value = "imageUpload")
    public R test(
            @ApiParam(name = "imageSrc", value = "上传照片的base64字符串", required = true)
            @RequestBody String imageSrc
    ){
        try {
            MultipartFile multipartFile = ImageUtils.base64ToMultipart(imageSrc);
            log.info("开始上传照片");
            //默认头像
            String s = "http://47.112.102.117:8080/group1/M00/00/00/rBI6u1x_e2qAedlbAAbtUP5DrZ0703.jpg";
            try {
                s = uploadService.fileUpload(multipartFile);
            } catch (IOException e) {
                e.printStackTrace();
                log.info("上传照片发生IO错误");
            } catch (MyException e) {
                e.printStackTrace();
                log.info("上传照片发生MyException错误");
            }
            log.info("上传成功");
            return R.ok().data(s);
        }catch (Exception e){
            e.printStackTrace();
        	return R.error().message(e.getMessage());
        }
    }
}
