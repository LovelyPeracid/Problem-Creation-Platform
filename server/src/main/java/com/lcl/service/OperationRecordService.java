package com.lcl.service;

import com.lcl.entity.OperationRecord;
import com.lcl.entity.Space;

import java.util.List;

/**
 * @author LovelyPeracid
 */
public interface OperationRecordService {
    void SpaceOperation(Space space, List<String> list,String type);
}
