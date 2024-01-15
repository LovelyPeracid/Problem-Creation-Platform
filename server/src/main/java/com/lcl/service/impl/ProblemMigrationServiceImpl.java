package com.lcl.service.impl;

import com.lcl.context.BaseContext;
import com.lcl.entity.ProblemMigration;
import com.lcl.mapper.ProblemMigrationMapper;
import com.lcl.service.ProblemMigrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author LovelyPeracid
 */
@Service
public class ProblemMigrationServiceImpl implements ProblemMigrationService {
    @Autowired
    private ProblemMigrationMapper problemMigrationMapper;

    @Override
    public void transfer(Long sourceSpaceId, Long destinationSpaceId, Long problemId) {
        ProblemMigration problemMigration = new ProblemMigration();
        problemMigration.setProblemId(problemId);
        problemMigration.setUserId(BaseContext.getCurrentId());
        problemMigration.setPrevSpaceId(sourceSpaceId);
        //problemMigration.getPrevSpaceId()
        problemMigration.setNextSpaceId(destinationSpaceId);
        //problemMigration.se
        //problemMigration.getPrevSpaceId();
        problemMigration.setCreatedAt(LocalDateTime.now());
        problemMigrationMapper.save(problemMigration);
    }
}
