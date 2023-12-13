package com.lcl.service.impl;

import com.lcl.context.BaseContext;
import com.lcl.entity.OperationRecord;
import com.lcl.entity.Problem;
import com.lcl.entity.Space;
import com.lcl.entity.SpaceUser;
import com.lcl.mapper.OperationRecordMapper;
import com.lcl.service.OperationRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author LovelyPeracid
 */
@Service
public class OperationRecordServiceImpl implements OperationRecordService {
    @Autowired
    private OperationRecordMapper operationRecordMapper;

    @Override
    public void SpaceOperation(Space space, List<String> list, String type) {
       OperationRecord operationRecord= commonOperation(list, type);
       operationRecord.setSpaceId(space.getSpaceId());
        operationRecordMapper.save(operationRecord);
    }

    @Override
    public void ProblemOperation(Problem problem, List<String> list, String type) {
      OperationRecord operationRecord=  commonOperation(list, type);
        operationRecord.setProblemId(problem.getProblemId());
        operationRecord.setSpaceId(problem.getSpaceId());
        operationRecordMapper.save(operationRecord);
    }
    @Override
    public  void SpaceUserOperation(SpaceUser spaceUser, List<String> list, String type){
         OperationRecord operationRecord=  commonOperation(list, type);
        operationRecord.setUserId(spaceUser.getUserId());
        operationRecord.setSpaceId(spaceUser.getSpaceId());
        operationRecordMapper.save(operationRecord);
    }

    private OperationRecord commonOperation(List<String> list, String type) {
        OperationRecord operationRecord=new OperationRecord();
        LocalDateTime time = LocalDateTime.now();
        operationRecord.setOperationAt(time);
        operationRecord.setOperationType(type);
        operationRecord.setIp(list.get(0));
        StringUtils.truncate(list.get(1), 512);
        operationRecord.setUserAgent(list.get(1));
        operationRecord.setOperator(BaseContext.getCurrentId());
        return operationRecord;
    }
}
