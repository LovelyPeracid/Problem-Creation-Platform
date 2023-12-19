package com.lcl.service;

import com.lcl.entity.Problem;
import com.lcl.entity.Space;
import org.gitlab4j.api.models.Commit;

/**
 * @author LovelyPeracid
 */
public interface GitlabService {
    Long CreateSpace(Space space);

    Long CreateProblem(Problem problem);

    void commit();

    Commit pushContent(Long gitlabId, Long id, String markdownContent);

    Commit updateContent(Long spaceId, Long problemId, String markdownContent);
}
