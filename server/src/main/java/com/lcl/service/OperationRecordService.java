package com.lcl.service;

import com.lcl.entity.OperationRecord;
import com.lcl.entity.Problem;
import com.lcl.entity.Space;
import com.lcl.entity.SpaceUser;

import java.util.List;

/**
 * @author LovelyPeracid
 */
public interface OperationRecordService {

    void SpaceOperation(Space space, List<String> list,String type);

    void ProblemOperation(Problem problem, List<String> ip, String type);

    void SpaceUserOperation(SpaceUser spaceUser, List<String> list, String type);
}
