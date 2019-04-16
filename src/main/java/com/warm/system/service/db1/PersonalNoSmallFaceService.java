package com.warm.system.service.db1;

import com.warm.system.entity.PersonalNoSmallFace;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 表情库 服务类
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
public interface PersonalNoSmallFaceService extends IService<PersonalNoSmallFace> {

    int getCount();

    PersonalNoSmallFace getById(int i);

    List<Integer> listId();
}
