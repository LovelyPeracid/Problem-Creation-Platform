package com.lcl.service;

import com.lcl.dto.ActionDTO;
import com.lcl.entity.Problem;
import com.lcl.entity.Space;
import com.lcl.result.Result;
import com.lcl.vo.CommitInfoVO;
import org.gitlab4j.api.models.Commit;

import java.util.List;

/**
 * @author LovelyPeracid
 */
public interface GitlabService {
    Long CreateSpace(Space space);

    Long CreateProblem(Problem problem);

    void commit();

    Commit pushContent(Long gitlabId, Long id, String markdownContent);

    Commit updateContent(Long spaceId, Long problemId, String markdownContent);

    List<String> getContent(Long projectId, String commitSha, String fileName);

    List<String> getStruct(Long projectId);
    List<String> getStruct(Long projectId, String commitSha);

    Result<CommitInfoVO> getInfo(Long problemId);

    Commit pushContent(List<ActionDTO> actions, String author, String message,Long problemId);

    List<CommitInfoVO> fetchCommits(Long gitlabId);

    void updateProjectTitle(Long projectId,String title);

    Result getDiff(Long gitlabId);

    void updateSpaceTitle(Space space);
}
