package com.lcl.service.impl;

import com.lcl.context.BaseContext;
import com.lcl.entity.OperationRecord;
import com.lcl.entity.Space;
import com.lcl.mapper.OperationRecordMapper;
import com.lcl.service.OperationRecordService;
import io.swagger.models.auth.In;
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
        OperationRecord operationRecord=new OperationRecord();
        LocalDateTime time = LocalDateTime.now();
        operationRecord.setOperationAt(time);
        operationRecord.setSpaceId(space.getSpaceId());
        operationRecord.setOperationType(type);
        operationRecord.setIp(list.get(0));
        StringUtils.truncate(list.get(1), 512);
        operationRecord.setUserAgent(list.get(1));
        operationRecord.setOperator(BaseContext.getCurrentId());
        operationRecordMapper.save(operationRecord);
    }
}
