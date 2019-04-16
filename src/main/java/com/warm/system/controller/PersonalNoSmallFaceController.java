package com.warm.system.controller;


import com.warm.entity.R;
import com.warm.system.entity.PersonalNoSmallFace;
import com.warm.system.service.db1.PersonalNoSmallFaceService;
import com.warm.utils.VerifyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.List;

/**
 * <p>
 * 表情库 前端控制器
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
@CrossOrigin //跨域
@Api(description = "表情管理")
@RestController
@RequestMapping("/personalNoSmallFace")
public class PersonalNoSmallFaceController {
    private static Log log = LogFactory.getLog(PersonalNoSmallFaceController.class);

}

