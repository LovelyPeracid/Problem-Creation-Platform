package com.lcl.service;

/**
 * @author LovelyPeracid
 */
public interface ProblemMigrationService {
    void transfer(Long sourceSpaceId, Long destinationSpaceId, Long problemId);
}
