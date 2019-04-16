package com.warm.system.service.db2;

import com.warm.system.entity.PersonalNoOperationStockWechatAccount;
import com.baomidou.mybatisplus.service.IService;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dgd123
 * @since 2019-03-29
 */
public interface PersonalNoOperationStockWechatAccountService extends IService<PersonalNoOperationStockWechatAccount> {

    PersonalNoOperationStockWechatAccount getByIdAndInstanceId(Integer currRobotLogicId, int ms_operation_project_instance_id);

    PersonalNoOperationStockWechatAccount getByWxIdAndInstanceId(String currRobotWxid, int ms_operation_project_instance_id);

    List<PersonalNoOperationStockWechatAccount> listByNickNameAndInstanceId(String nickname, int ms_operation_project_instance_id);

    List<PersonalNoOperationStockWechatAccount> listByInstanceId(int ms_operation_project_instance_id);

    List<PersonalNoOperationStockWechatAccount> listByRequestTaskTimeAndInstanceId(Date date, int ms_operation_project_instance_id);

    boolean updateByLogicId(PersonalNoOperationStockWechatAccount tempOSWA);

    PersonalNoOperationStockWechatAccount getByLogicId(Integer logicId);
}
