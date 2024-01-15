package com.lcl.service;

import com.lcl.dto.*;
import com.lcl.result.PageResult;
import com.lcl.result.Result;
import com.lcl.vo.CommitInfoVO;
import com.lcl.vo.ProblemVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author LovelyPeracid
 */
public interface ProblemService {


    PageResult page(ProblemPageQueryDTO problemPageQueryDTO);

    ProblemVO getById(Long problemId);

    List<ProblemVO> getBySpaceId(Long spaceId);

    void save(ProblemCreateDTO problemCreateDTO, Long spaceId, HttpServletRequest request);

    void startOrStop(Long spaceId, ProblemStartOrStopDTO problemStartOrStopDTO, HttpServletRequest request);

    void update(Long spaceId, ProblemUpdateDTO problemUpdateDTO, HttpServletRequest request);

    void pushContent(Long spaceId, Long problemId, CommitDTO list, HttpServletRequest request);

    void transfer(Long sourceSpaceId, Long problemId, Long destinationSpaceId, HttpServletRequest request);

    Long clone(Long problemId, HttpServletRequest request, Long newSpaceId);

    Long quote(Long problemId, Long newSpaceId, HttpServletRequest request);

    List<String> getContent(Long projectId, String commitSha, String fileName);

    List<String> getStruct(Long problemId);

    Result<CommitInfoVO> getInfo(Long problemId);
}
