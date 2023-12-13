package com.lcl.service;

import com.lcl.entity.Problem;
import com.lcl.entity.Space;

/**
 * @author LovelyPeracid
 */
public interface GitlabService {
    Long CreateSpace(Space space);

    Long CreateProblem(Problem problem);
}
