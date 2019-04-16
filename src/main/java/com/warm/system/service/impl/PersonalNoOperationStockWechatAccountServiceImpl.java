package com.warm.system.service.impl;

import com.warm.common.DBTypeEnum;
import com.warm.common.DataSourceSwitch;
import com.warm.system.entity.PersonalNoOperationStockWechatAccount;
import com.warm.system.mapper.PersonalNoOperationStockWechatAccountMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.warm.system.service.db2.PersonalNoOperationStockWechatAccountService;
import com.warm.utils.WebConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
public class PersonalNoOperationStockWechatAccountServiceImpl extends ServiceImpl<PersonalNoOperationStockWechatAccountMapper, PersonalNoOperationStockWechatAccount> implements PersonalNoOperationStockWechatAccountService {
    @Autowired
    private PersonalNoOperationStockWechatAccountMapper operationStockWechatAccountMapper;
    /**
     * 根据id和operation_project_instance_id查找一个
     * @param currRobotLogicId
     * @param ms_operation_project_instance_id
     * @return
     */
    @Override
    public PersonalNoOperationStockWechatAccount getByIdAndInstanceId(Integer currRobotLogicId, int ms_operation_project_instance_id) {
        return operationStockWechatAccountMapper.getByLogicIdAndInstanceId(currRobotLogicId, ms_operation_project_instance_id);
    }
    /**
     * 根据wx_id和operation_project_instance_id查找一个
     * @param currRobotWxid
     * @param ms_operation_project_instance_id
     * @return
     */
    @Override
    public PersonalNoOperationStockWechatAccount getByWxIdAndInstanceId(String currRobotWxid, int ms_operation_project_instance_id) {
        return operationStockWechatAccountMapper.getByWxIdAndInstanceId(currRobotWxid, ms_operation_project_instance_id);
    }
    /**
     * 根据nick_name和operation_project_instance_id查找列表
     * @param nickname
     * @param ms_operation_project_instance_id
     * @return
     */
    @Override
    public List<PersonalNoOperationStockWechatAccount> listByNickNameAndInstanceId(String nickname, int ms_operation_project_instance_id) {
        return operationStockWechatAccountMapper.listByNickNameAndInstanceId(nickname, ms_operation_project_instance_id);
    }

    /**
     * 根据operation_project_instance_id查询列表
     * @param ms_operation_project_instance_id
     * @return
     */
    @Override
    public List<PersonalNoOperationStockWechatAccount> listByInstanceId(int ms_operation_project_instance_id) {
        return operationStockWechatAccountMapper.listByInstanceId(ms_operation_project_instance_id);
    }

    @Override
    public List<PersonalNoOperationStockWechatAccount> listByRequestTaskTimeAndInstanceId(Date date, int ms_operation_project_instance_id) {
        return operationStockWechatAccountMapper.listByRequestTaskTimeAndInstanceId(WebConst.getNowDate(date), ms_operation_project_instance_id);
    }
    @Override
    public boolean updateByLogicId(PersonalNoOperationStockWechatAccount tempOSWA) {
        return operationStockWechatAccountMapper.updateByLogicId(tempOSWA.getWxId(), tempOSWA.getWxIdBieMing(),tempOSWA.getAssignPhone(),tempOSWA.getNickName(),tempOSWA.getArea(),tempOSWA.getWhatsUp(),tempOSWA.getQrCode(),tempOSWA.getIsHaveRealname(),tempOSWA.getIsSlientDownload(),tempOSWA.getOperationProjectInstanceId(),tempOSWA.getWxClass(),tempOSWA.getPassword(),tempOSWA.getProjectInstanceRegTime(),tempOSWA.getLastRequestJobTime(),tempOSWA.getLastConnectTime(),tempOSWA.getLastUpdateLocalIp(),tempOSWA.getLastConnectInternetIp(),tempOSWA.getClickId(),tempOSWA.getCurrentClientWehookVersion(),tempOSWA.getCurrentClientWechatVersion(),tempOSWA.getId());
    }

    @Override
    public PersonalNoOperationStockWechatAccount getByLogicId(Integer logicId) {
        return operationStockWechatAccountMapper.getByLogicId(logicId);
    }
}
